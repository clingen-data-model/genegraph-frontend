(ns genegraph.frontend.page.find
  (:require [clojure.set :as set]
            [re-frame.core :as rf]
            [re-graph.core :as re-graph]
            [genegraph.frontend.filters :as filters]))


;; create event handlers in re-frame that execute filters-query usig re-graph and recieves the result, persisting it in the application db
(def filters-query "
{
  filters {
    id
    label
    description
    display
    options {
      id
      label
      description
    }
  }
}
")


;; Event to trigger the filters query
(rf/reg-event-fx
 ::fetch-filters
 (fn [_ _]
   (js/console.log "Requesting filter list")
   {:fx [[:dispatch
          [:re-graph/query
           {:id :filters-query
            :query filters-query
            :callback [::return-filters-query]}]]]}))

(rf/reg-event-db
 ::add-filter
 (fn [db _]
   (update db ::filters conj {})))

;; Success handler - stores result in app-db
(rf/reg-event-db
 ::return-filters-query
 (fn [db [_ result]]
   (js/console.log "recieved filters")
   (let [all-filters (get-in result [:response :data :filters])
         type-filter (filterv #(= "proposition_type" (:id %)) all-filters)
         remaining-filters (into [] (remove #(= "proposition_type" (:id %)) all-filters))]
     (assoc db
            ::available-filters remaining-filters
            ::filters type-filter))))

;; Subscription to access filters
(rf/reg-sub
 ::available-filters
 :-> ::available-filters)

(def filters
  {:proposition_type
   {:type :radio
    :label "Assertion Type"
    :options [{:label "Variant Pathogenicity"
               :description "CNV Variant Pathogenicity records from ClinVar"
               :id "CG:VariantPathogenicityProposition"}
              {:label "Gene Validity"
               :description "Gene Validity assertions from ClinGen and GenCC"
               :id "CG:GeneValidityProposition"}
              {:label "Genetic Condition Mechanism"
               :description "Genetic Condition Mechanism assertions from ClinGen Gene Dosage Curation"
               :id "CG:GeneticConditionMechanismProposition"}]}
   })


(rf/reg-event-fx
 ::init
 (fn [{:keys [db]} _]
   (js/console.log "init find")
   {:fx [[:dispatch
          [::re-graph/query
           {:id :filters-query
            :query filters-query
            :callback [::return-filters-query]}]]]
    :db (assoc db ::filters [(:proposition_type filters)
                             {}])}))

(rf/reg-sub
 ::filters
 :-> ::filters)

(rf/reg-event-db
 ::update-filter-value
 (fn [db [_ filter value]]
   (js/console.log "update-filter-value")
   (assoc-in db [::filters (:index filter) :argument] value)))

(rf/reg-event-db
 ::set-filter
 (fn [db [_ {:keys [index]} new-filter]]
   (assoc-in db [::filters index] (assoc new-filter :index index))))

(defn radio-filter-div [filter]
  ;; why aren't any of the radio buttons below selectable?
  [:fieldset
   {:aria-label (:label filter)}
   [:div
    {:class "py-2 text-lg/6 font-medium"}
    (:label filter)]
   [:div
    {:class "space-y-5"}
    (for [{:keys [label id description]} (:options filter)]
      ^{:key [filter id]}
      [:div
       {:class "relative flex items-start"}
       [:div
        {:class "flex h-6 items-center"}
        [:input
         {:id id
          :type "radio",
          :checked (= id (:argument filter))
          :on-change #(rf/dispatch [::update-filter-value filter id])
          :name #_(:label filter) "radioopts"
          :class
          "relative size-4 appearance-none rounded-full border border-gray-300 bg-white before:absolute before:inset-1 before:rounded-full before:bg-white not-checked:before:hidden checked:border-indigo-600 checked:bg-indigo-600 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600 disabled:border-gray-300 disabled:bg-gray-100 disabled:before:bg-gray-400 dark:border-white/10 dark:bg-white/5 dark:checked:border-indigo-500 dark:checked:bg-indigo-500 dark:focus-visible:outline-indigo-500 dark:disabled:border-white/5 dark:disabled:bg-white/10 dark:disabled:before:bg-white/20 forced-colors:appearance-auto forced-colors:before:hidden"}]]
       [:div
        {:class "ml-3 text-sm/6"}
        [:label
         {:for id
          :class "font-medium text-gray-900 dark:text-white"}
         label]
        [:p
         {:class "text-gray-500 dark:text-gray-400"}
         description]]])]]



  )

(defn selected? [option filter]
  (= (:id option) (:argument filter)))

(defn list-div [filter]
  ^{:key filter}
  [:ul
   {:role "list"
    :class "divide-y divide-gray-100 dark:divide-white/5"}
   (for [o (:options filter)]
     ^{:key [filter o]}
     [:li
      {:class "py-2"
       :on-click #(rf/dispatch [::update-filter-value filter (:id o)])}
      [:div
       {:class "flex gap-4"}
       [:div
        {:class (if (selected? o filter)
                  "text-sm/6 font-semibold text-sky-700 dark:text-white"
                  (if (:argument filter)
                    "text-sm/6 font-semibold text-gray-400 dark:text-white"
                    "text-sm/6 font-semibold text-gray-900 dark:text-white"))}
        (:label o)]
       [:div
        {:class "text-sm/6 font-light text-gray-700"}
        (:description o)]]])])

(defn default-display [filter]
  ^{:key filter}
  [:ul
   {:role "list"
    :class "divide-y divide-gray-100 dark:divide-white/5"}
   (for [f @(rf/subscribe [::available-filters])]
     ^{:key [filter f]}
     [:li
      {:class "py-2"
       :on-click #(rf/dispatch [::set-filter filter f])}
      [:div
       {:class "flex gap-4"}
       [:div
        {:class "text-sm/6 font-semibold text-gray-900 dark:text-white"}
        (:label f)]
       [:div
        {:class "text-sm/6 font-light text-gray-700"}
        (:description f)]]])])

(def filter->display
  {:proposition_type radio-filter-div})

(defn filter-div [filter]
  (case (:display filter)
    :radio (radio-filter-div filter)
    "list" (list-div filter)
    (default-display filter)))

(defn add-index [filter-list]
  (map-indexed
   (fn [idx i]
     (assoc i :index idx))
   filter-list))

(defn filter-list [filters]
  [:ul
   {:role "list", :class "divide-y divide-gray-100 dark:divide-white/5"}
   (doall
    (for [f filters]
      ^{:key f}
      [:li
       {:class "flex items-center justify-between gap-x-6 py-5"}
       (filter-div f)]))])

(defn button [text action]
  [:button
   {:type "button",
    :on-click action
    :class
    "rounded-md bg-white px-3.5 py-2.5 my-8 text-sm font-semibold text-gray-900 shadow-xs inset-ring inset-ring-gray-300 hover:bg-gray-50 dark:bg-white/10 dark:text-white dark:shadow-none dark:inset-ring-white/5 dark:hover:bg-white/20"}
   text])

(defn prepare-filters-for-query [filters]
  (mapv #(-> %
             (set/rename-keys {:id :filter})
             (select-keys [:filter :argument]))
        filters))

(defn find-div []
  (let [filters (add-index @(rf/subscribe [::filters]))]
    [:div
     {:class "py-12 px-12"}
     (filter-list filters)
     (button "Add Filter" #(rf/dispatch [::add-filter]))
     (button "Run" #(rf/dispatch [::filters/send-query
                                  (prepare-filters-for-query filters)]))
     (button "Reset" #(rf/dispatch [::init]))
     [:pre (with-out-str (cljs.pprint/pprint filters))]
     [:pre (with-out-str (cljs.pprint/pprint @(rf/subscribe [::available-filters])))]]))
