(ns genegraph.frontend.page.home
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.display :as display]
            [genegraph.frontend.user :as user]
            [genegraph.frontend.icon :as icon]
            [genegraph.frontend.display.annotation :as annotation]
            [genegraph.frontend.queries.assertion :as assertion]
            [clojure.string :as s]))

(re-frame/reg-sub
 ::query-data
 :-> :query-data)

(re-frame/reg-event-db
 ::select
 (fn [db [_ i]]
   (assoc db ::selected i)))

(re-frame/reg-sub
 ::selected
 :-> ::selected)

(defn evaluation [assertion]
  (->> (:contributions assertion)
       #_(filter #(= "CG:Evaluator" (get-in % [:role :curie])))
       first))

(defn annotation-div [assertion]
  [:div
   {:class "flex p-1"
    :on-click #(re-frame/dispatch [::annotation/set-currently-annotating (:iri assertion)])}
   icon/document-text-outline])

(defn classification-pill [classification]
  (let [base-classes "p-px px-1 rounded-md "
        [label color-classes]
        (case (:curie classification)
          "CG:Pathogenic" ["P" "bg-red-700 text-red-100"]
          "CG:LikelyPathogenic" ["LP" "bg-red-700 text-red-100"]
          "CG:UncertainSignificance" ["VUS" "bg-blue-900 text-blue-50"]
          "CG:LikelyBenign" ["LB" "bg-green-900 text-green-100"]
          "CG:Benign" ["B" "bg-green-900 text-green-100"]
          ["N" "bg-gray-500 text-gray-50"])]
    [:div
     {:class (str base-classes color-classes)}
     label]))

(defn variant-small-label [variant]
  (-> (:label variant)
      (s/replace #"GRCh\d\d/hg\d\d" "")
      (s/replace #"\(.*\)" "")))

(defn assertion-list-item [assertion current-user selected]
  [assertion]
  ^{:key assertion}
  (if-let [e (evaluation assertion)]
    [:li
     {:class (if selected
               "p-2 bg-gray-200 rounded-xl"
               "p-2")
      :on-click #(re-frame/dispatch [::select (:iri assertion)])}
     [:div
      {:class "flex items-center"}
      [:div
       {:class "pr-3 text-xs font-semibold"}
       (classification-pill (:classification assertion))]
      [:div
       {:class "flex-1 text-sm/6 font-semibold text-gray-900"}
       (variant-small-label (get-in assertion [:subject :variant]))]]
     [:p
      {:class "text-xs text-gray-500"}
      (get-in e [:agent :label])]
     [:p
      {:class "text-xs text-gray-500"}
      (:date e)]]
    [:li "error"]))

(defn home []
  (let [current-user @(re-frame/subscribe [::user/current-user])
        selected @(re-frame/subscribe [::selected])
        currently-annotating @(re-frame/subscribe [::annotation/currently-annotating])]
    [:div
     {:class "flex"}
     [:div
      {:class "flex flex-1 py-10 px-10"}
      [:ul
       (doall
        (for [i @(re-frame/subscribe [::query-data])]
          (if (= selected (:iri i))
            (assertion-list-item i current-user selected)
            (assertion-list-item i current-user false))))]]
     [:div
      {:class "flex flex-1"}]]))

;; TODO reimplement with hybrid-resource methods
;; Also only works for ClinVar submisisons
;; Consdier other use cases

(defmethod display/list-title  "VariantPathogenicityProposition"
  [prop]
  [:p
   {:class "text-sm/6 font-semibold text-gray-900"}
   [:a
    {:href (get-in prop [:variant :iri])
     :target "_blank"}
    (get-in prop [:variant :label])]])
 
