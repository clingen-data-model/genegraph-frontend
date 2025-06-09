(ns genegraph.frontend.components.search
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.user :as user]
            [genegraph.frontend.queries :as queries]
            [genegraph.frontend.icon :as icon]
            [genegraph.frontend.components.assertion :as assertion]
            [clojure.string :as str]))

(def text-search-query
  "query ($symbol: String) {
  sequenceFeatureQuery(symbol: $symbol) {
    __typename
    iri
    curie
    label
    type {
      curie
      label
    }
    assertions {
      curie
      iri
      evidenceStrength {
        curie
      }
      subject {
        __typename
        type {
          curie
          label
        }
        ...geneValidityProp
        ...mechanismProp
      }
      contributions {
        role {
          curie
        }
        date
        agent {
          curie
          label
        }
      }
    }
  }
}

fragment geneValidityProp on GeneValidityProposition {
  modeOfInheritance {
    curie
    label
  }
  disease {
    curie
    label
  }
}

fragment mechanismProp on GeneticConditionMechanismProposition {
  mechanism {
    curie
  }
  condition {
    label
    curie
  }
}"

)

(re-frame/reg-event-db
 ::select-entity-type
 (fn [db [_ type]]
   (js/console.log "setting entity type " type)
   (assoc db ::selected-entity-type type)))

(re-frame/reg-sub
 ::selected-entity-type
 :-> ::selected-entity-type)
    :push-state {:route :routes/resource
                 :params {:path {:id "CG:12345"}}}
#_(re-frame/reg-event-db
 ::recieve-text-search-result
 (fn [db [_ result]]
   (js/console.log "recieved result")
   (assoc db
          :main
          (get-in result
                  [:response
                   :data
                   :sequenceFeatureQuery]))))


;; seems to be working now
;; still makes me nervous
(re-frame/reg-event-fx
 ::recieve-text-search-result
 (fn [db [_ result]]
   (let [feature (get-in result [:response :data :sequenceFeatureQuery])]
     (js/console.log "recieved result")
     (js/console.log (:curie feature))
     {:db (assoc db :main feature)
      :push-state [:routes/resource {:id (:curie feature)}]})))

(re-frame/reg-event-fx
 ::text-search
 (fn [{:keys [db]} [_ text]]
   (js/console.log "searching for " (::search-input db))
   {:fx [[:dispatch
          [::re-graph/query
           {:id ::query
            :query text-search-query
            :variables {:symbol (str/upper-case (::search-input db))} ; change 
            :callback [::recieve-text-search-result]}]]]}))

(defn text-search-div []
  [:div
   {:class "mt-2 grid grid-cols-1"}
   [:input
    {:type "text",
     :name "search-text",
     :id "search-text",
     :on-key-down (fn [e]
                    (case (.-key e)
                      "Enter" (re-frame/dispatch
                               [::text-search
                                (-> e .-target .-value)])
                      nil))
     :class
     "col-start-1 row-start-1 block w-full rounded-md bg-white py-1.5 pr-3 pl-10 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:pl-9 sm:text-sm/6",
     :placeholder "gene symbol"}]
   [:div
    {:class
     "pointer-events-none col-start-1 row-start-1 ml-3 size-5 self-center text-gray-400 sm:size-4",
     :viewBox "0 0 16 16",
     :fill "currentColor",
     :aria-hidden "true",
     :data-slot "icon"}
    icon/magnifying-glass]])

;; searchable entity types, in order of priority
(def entity-types
  [{:label "Genes"
    :value :genes}
   {:label "Variants"
    :value :variants}])


(defn select-entity-type-div []
  (let [selected-type @(re-frame/subscribe [::selected-entity-type])]
    [:fieldset
     {:aria-label "Choose an entity type "}
     [:div
      {:class "mt-2 grid grid-cols-3 gap-3 sm:grid-cols-6"}
      (comment
        "In Stock: \"cursor-pointer\", Out of Stock: \"opacity-25 cursor-not-allowed\"\n      Active: \"ring-2 ring-indigo-600 ring-offset-2\"\n      Checked: \"ring-0 bg-indigo-600 text-white hover:bg-indigo-500\", Not Checked: \"ring-1 ring-gray-300 bg-white text-gray-900 hover:bg-gray-50\"\n      Not Active and not Checked: \"ring-inset\"\n      Active and Checked: \"ring-2\"")
      (for [{:keys [label value]} entity-types]
        ^{:key value}
        [:label
         {:class
          (str 
           "flex cursor-pointer items-center justify-center rounded-md px-3 py-3 text-sm font-semibold focus:outline-hidden sm:flex-1 "
           (if (= value selected-type)
             "ring-0 bg-indigo-600 text-white hover:bg-indigo-500"
             "ring-2 ring-indigo-600 ring-offset-2"))}
         [:input
          {:type "radio",
           :name "memory-option",
           :value value
           :class "sr-only"
           :on-change #(re-frame/dispatch [::select-entity-type value])}]
         [:span label]])]]))

;; TODO enhance mouseover behavior
;; should be obvious that clicking on an item
;; causes an action
(defn query-list-div []
  [:div
   {:class "grid grid-cols-2 gap-4 mt-8 mb-8"}
   (for [{:keys [label description filters]} queries/queries]
     ^{:key label}
     [:div
      {:class "col-span-2 grid grid-cols-subgrid gap-8"
       :on-click #(do (re-frame/dispatch [::queries/send-query filters])
                      (re-frame/dispatch [:genegraph.frontend.view/push
                                          {:component :search-result}
                                          {:component :search-dialog}]))}
      [:div
       {:class "font-semibold"}
       label]
      [:div
       {:class "text-sm"}
       description]])])


(defn search-div []
  [:div
   {:class "flex flex-col"}
   #_(select-entity-type-div)
   (query-list-div)])

(defn search-result-div []
  [:ul
   (for [a @(re-frame/subscribe [::queries/query-result])]
     (assertion/assertion-list-item a {:component :search-result}))])

#_(defn search-div []
    [:div
     {:class "flex"}
     (text-search-div)])
