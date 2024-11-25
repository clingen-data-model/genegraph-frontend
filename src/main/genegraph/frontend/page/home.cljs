(ns genegraph.frontend.page.home
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]))

(def clock-icon
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :fill "none",
    :viewBox "0 0 24 24",
    :stroke-width "1.5",
    :stroke "currentColor",
    :class "size-6"}
   [:path
    {:stroke-linecap "round",
     :stroke-linejoin "round",
     :d "M12 6v6h4.5m4.5 0a9 9 0 1 1-18 0 9 9 0 0 1 18 0Z"}]])

(def cg-root
  "http://dataexchange.clinicalgenome.org/terms/")

(def users
  {1 "Tasha"
   2 "Tracy"
   3 "Tristan"}) 

(def conflicts-query
  "{
  conflicts {
    date
    iri
    curie
    contributions {
      agent {
        curie
        label
      }
      role {
        curie
        label
      }
      date
    }
    conflictingAssertions {
      curie
      iri
      contributions {
        role {
          curie
          label
        }
        date
      }
      subject {
        curie
        ... on GeneticConditionMechanismProposition {
          feature {
            curie
            label
          }
          mechanism {
            label
            curie
          }
          condition {
            label
            curie
          }
        }
      }
    }
    subject {
      curie
      iri
      ... on VariantPathogenicityProposition {
        variant {
          __typename
          curie
          label
          ... on CanonicalVariant {
            copyChange {
              label
              curie
            }
            includedVariants {
              ... on CopyNumberChange {
                copyChange {
                  label
                  curie
                }
              }
            }
          }
        }
      }
    }
    classification {
      curie
      label
    }
  }
}
")


(re-frame/reg-event-db
 ::recieve-conflict-list
 (fn [db [_ result]]
   (js/console.log "Recieved conflict list")
   (assoc db ::conflicts (get-in result [:response :data :conflicts]))))

(re-frame/reg-event-fx
 ::request-conflict-list
 (fn [_ _]
   (js/console.log "Requesting conflict list ")
   {:fx [[:dispatch
          [::re-graph/query
           {:id ::conflict-list
            :query conflicts-query
            :variables {}
            :callback [::recieve-conflict-list]}]]]}))

(re-frame/reg-event-db
 ::curate-assertion
 (fn [db [_ assertion]]
   (assoc db ::currently-curating assertion)))

(def save-curation-mutation
  "mutation (
  $subject: String
  $agent: String
  $description: String
  $classification: String
  $evidence: [String]
) {
  createAssertionAnnotation(
    subject: $subject
    agent: $agent
    description: $description
    classification: $classification
    evidence: $evidence
  ) {
    iri
  }
}
")

(re-frame/reg-event-db
 ::recieve-save-result
 (fn [db [_ result]]
   (assoc db ::save-result result)))

(re-frame/reg-event-fx
 ::save-curation
 (fn [{:keys [db]} [_ subject]]
   {:db (assoc db
               ::currently-curating nil
               ::conflicts (->> (::conflicts db)
                                (remove #(= subject (:iri %)))
                                (into [])))
    :fx [[:dispatch
          [::re-graph/mutate
           {:id ::save-curation
            :query save-curation-mutation
            :variables (assoc (get-in db [::curations subject])
                              :subject subject
                              :agent (str
                                      "https//clingen.app/users/"
                                      (::current-user db))
                              :evidence (->> (::conflicts db)
                                             (filter #(= subject (:iri %)))
                                             (mapcat #(map :iri (:conflictingAssertions %)))))
            :callback [::recieve-save-result]}]]]}))

(re-frame/reg-event-db
 ::edit-curation
 (fn [db [_ iri field value]]
   (assoc-in db [::curations iri field] value)))

(re-frame/reg-event-db
 ::set-current-user
 (fn [db [_ user-id]]
   (assoc db ::current-user user-id)))

(re-frame/reg-sub
 ::current-user
 :-> ::current-user)

(re-frame/reg-sub
 ::all-curations
 :-> ::curations)

(re-frame/reg-sub
 ::curation
 :=> (fn [db iri]
       (js/console.log "Returing curation sub ")
       (get-in db [::curations iri])))

(re-frame/reg-sub
 ::currently-curating
 :-> ::currently-curating)

(re-frame/reg-sub
 ::conflicts
 :-> ::conflicts)

#_(defn home []
    [:div
     [:h2 "Welcome to frontend"]

     [:button
      {:type "button"
       :on-click #(rfe/push-state ::item {:id 3})}
      "Item 3"]

     [:button
      {:type "button"
       :on-click #(rfe/replace-state ::item {:id 4})}
      "Replace State Item 4"]])


(defn curation-dialog [assertion]
  (let [curation @(re-frame/subscribe [::curation (:iri assertion)])]
    [:form
     {:class "mb-8"}
     [:div
      {:class "w-80 mt-4"}
      [:label
       {:for "error",
        :class "sr-only"}
       "error"]
      [:select
       {:class
        "mt-2 block w-full rounded-md border-0 py-1.5 pl-3 pr-10 text-gray-900 ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-indigo-600 sm:text-sm sm:leading-6"
        :on-change (fn [e] (re-frame/dispatch [::edit-curation
                                               (:iri assertion)
                                               :classification
                                               (-> e .-target .-value)]))
        :value (get curation :classification (str cg-root "NoAssessment"))}
       [:option
        {:value (str cg-root "NoAssessment")}
        "no assessment"]
       [:option
        {:value (str cg-root "DosageMapConflict")}
        "incorrect due to dosage map conflict"]
       [:option
        {:value (str cg-root "ErrorInVariantDescription")}
        "error in variant description"] 
       [:option
        {:value (str cg-root "ClassificationCorrect")}
        "classification is correct"]]]
     [:div
      [:label
       {:for "comment",
        :class "block text-sm font-medium leading-6 text-gray-900 sr-only"}
       "Add your comment"]
      [:div
       {:class "mt-2"}
       [:textarea
        {:rows "4",
         :name "comment",
         :id "comment",
         :placeholder "comment"
         :class
         "block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
         :on-change (fn [e] (re-frame/dispatch [::edit-curation
                                                (:iri assertion)
                                                :description
                                                (-> e .-target .-value)]))
         #_#_:value (:description curation)}]]] ; results in laggy input
     [:div
      [:button
       {:type "button",
        :on-click #(re-frame/dispatch [::save-curation (:iri assertion)])
        :class
        "mt-2 rounded-md bg-indigo-600 px-3.5 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"}
       "save"]
      [:button
       {:type "button",
        :on-click #(re-frame/dispatch [::curate-assertion
                                       nil])
        :class
        "mt-2 ml-4 rounded-md bg-gray-400 px-3.5 py-2.5 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"}
       "cancel"]]]))

(defn approval-date [c]
  (->> (:contributions c)
       (filter #(= "CG:Approver" (get-in % [:role :curie])))
       (map :date)
       first))

(defn old-dosage-curation? [c]
  (-> c
      approval-date
      (compare "2020-01-01")
      pos-int?
      not))

(defn dosage-curation-card [a]
  (let [is-old (old-dosage-curation? a)]
    [:div
     (if is-old
         {:class "font-medium text-gray-400 flex"}
       {:class "font-medium text-rose-900 flex"})
     (get-in a [:subject :feature :label])
     (when is-old clock-icon)]))

(def pencil-square-icon
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :fill "none",
    :viewBox "0 0 24 24",
    :stroke-width "1.5",
    :stroke "currentColor",
    :class "size-6"}
   [:path
    {:stroke-linecap "round",
     :stroke-linejoin "round",
     :d
     "m16.862 4.487 1.687-1.688a1.875 1.875 0 1 1 2.652 2.652L10.582 16.07a4.5 4.5 0 0 1-1.897 1.13L6 18l.8-2.685a4.5 4.5 0 0 1 1.13-1.897l8.932-8.931Zm0 0L19.5 7.125M18 14v4.75A2.25 2.25 0 0 1 15.75 21H5.25A2.25 2.25 0 0 1 3 18.75V8.25A2.25 2.25 0 0 1 5.25 6H10"}]])

(defn submitter [i]
  (->> (:contributions i)
       (map :agent)
       first))

(defn conflict-item [i]
  [:div
   {:class "flex gap-x-6"
    :on-click  #(re-frame/dispatch [::curate-assertion
                                    (:iri i)])}
   [:div
    {:class "flex w-1/2 gap-x-4"}
    [:div
     {:class "min-w-0 flex-auto"}
     [:p
      {:class "text-sm font-semibold leading-6 text-gray-900"}
      [:a {:href (:iri i) :target "_blank"}
       (get-in i [:subject :variant :label])]]
     [:p
      {:class "text-sm leading-6 text-gray-500"}
      (get-in i [:classification :label])]
     [:div
      {:class "mt-1 truncate text-xs leading-5 text-gray-500"}
      [:span
       {:class "flex gap-8"}
       [:div (:date i)]
       [:div (:label (submitter i))]]]]]
   ;; gene grid
   [:div
    {:class "w-1/4"}
    [:div
     {:class "text-gray-700 text-sm font-light"}
     "Dosage conflicts"]
    [:div
     {:class #_"grid grid-cols-1 gap-2 py-1 sm:grid-cols-4"
      "flex gap-2 py-1 flex-wrap"}
     (doall
      (for [a (take 20 (:conflictingAssertions i))]
        (with-meta
          (dosage-curation-card a)
          {:key [i a]})))]]
   [:div
    {:on-click #(re-frame/dispatch [::curate-assertion
                                    (:iri i)])}
    [:span pencil-square-icon]]])

(defn conflict-list []
  (let [currently-curating @(re-frame/subscribe [::currently-curating])
        conflicts @(re-frame/subscribe [::conflicts])]
    [:ul
     {:role "list", :divide "class-y divide-gray-100"}
     (doall
      (for [i (->> conflicts
                   (filter #(get-in % [:classification :label]))
                   (sort-by #(get-in % [:subject :variant :label]))
                   (take 50))]
        ^{:key i}
        [:li
         {:class "py-8"}
         (conflict-item i)
         (when (= (:iri i) currently-curating)
           (curation-dialog i))]))]))

(defn show-conflict-list []
  [:div
   {:class "py-10"}
   [:header
    [:div
     {:class "mx-auto max-w-7xl px-4 sm:px-6 lg:px-8"}
     [:h1
      {:class
       "text-3xl font-bold leading-tight tracking-tight text-gray-900"}
      "Conflict List"]]]
   [:main
    [:div
     {:class "mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8"}
     (conflict-list)]
    #_[:div [:pre (with-out-str (cljs.pprint/pprint @(re-frame/subscribe [::conflicts])))]]]])

(defn login []
  [:div
   {:class "py-10"}
   [:header
    [:div
     {:class "mx-auto max-w-7xl px-4 sm:px-6 lg:px-8"}
     [:h1
      {:class
       "text-3xl font-bold leading-tight tracking-tight text-gray-900"}
      "Login"]]]
   [:main
    [:div
     {:class "mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8"}
     [:ul
      (for [[id name] users]
        ^{:key id}
        [:li
         {:on-click #(re-frame/dispatch [::set-current-user id])}
         name])]]]])

(defn home []
  (if @(re-frame/subscribe [::current-user])
    (show-conflict-list)
    (login)))


