(ns genegraph.frontend.display.annotation
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.user :as user]
            [genegraph.frontend.icon :as icon]))

(def save-annotation-mutation
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
 ::set-currently-annotating
 (fn [db [_ i]]
   (assoc db ::currently-annotating i)))

(re-frame/reg-sub
 ::currently-annotating
 :-> ::currently-annotating)

(re-frame/reg-event-db
 ::recieve-save-result
 (fn [db [_ result]]
   (assoc db ::save-result result)))

;; TODO, update local DB with annotation
;; *or* update local DB with new query data
(re-frame/reg-event-fx
 ::save-annotation
 (fn [{:keys [db]} [_ subject]]
   (js/console.log "saving annotation")
   {:db (assoc db
               ::currently-annotating nil
               ::conflicts (->> (::conflicts db)
                                (remove #(= subject (:iri %)))
                                (into [])))
    :fx [[:dispatch
          [::re-graph/mutate
           {:id ::save-annotation
            :query save-annotation-mutation
            :variables (assoc (get-in db [::annotations subject])
                              :subject subject
                              :agent (str
                                      "https//clingen.app/users/"
                                      (::current-user db))
                              :evidence (->> (::conflicts db)
                                             (filter #(= subject (:iri %)))
                                             (mapcat #(map :iri (:conflictingAssertions %)))))
            :callback [::recieve-save-result]}]]]}))



(def cg-root
  "http://dataexchange.clinicalgenome.org/terms/")

(re-frame/reg-sub
 ::annotation
 :=> (fn [db iri] (get-in db [::annotations iri])))

(re-frame/reg-event-db
 ::edit-annotation
 (fn [db [_ iri field value]]
   (assoc-in db [::annotations iri field] value)))

(defn annotation-dialog [assertion]
  (let [annotation @(re-frame/subscribe [::annotation (:iri assertion)])]
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
        :on-change (fn [e] (re-frame/dispatch [::edit-annotation
                                               (:iri assertion)
                                               :classification
                                               (-> e .-target .-value)]))}
       [:option
        {:value ""}
        "choose an option"]
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
         "block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-xs ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
         :on-change (fn [e] (re-frame/dispatch [::edit-annotation
                                                (:iri assertion)
                                                :description
                                                (-> e .-target .-value)]))
         #_#_:value (:description curation)}]]] ; results in laggy input
     [:div
      [:button
       {:type "button",
        :on-click #(re-frame/dispatch [::save-annotation (:iri assertion)])
        :class
        "mt-2 rounded-md bg-indigo-600 px-3.5 py-2.5 text-sm font-semibold text-white shadow-xs hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"}
       "save"]
      [:button
       {:type "button",
        :on-click #(re-frame/dispatch [::currently-annotating
                                       nil])
        :class
        "mt-2 ml-4 rounded-md bg-gray-400 px-3.5 py-2.5 text-sm font-semibold text-white shadow-xs hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"}
       "cancel"]]]))
