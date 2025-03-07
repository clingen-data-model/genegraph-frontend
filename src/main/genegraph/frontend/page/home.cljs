(ns genegraph.frontend.page.home
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.display :as display]
            [genegraph.frontend.user :as user]))

(re-frame/reg-sub
 ::query-data
 :-> :query-data)

(defn home []
  [:div
   {:class "py-10 px-10"}
   [:h4
    {:class
     "text-xl font-bold leading-tight tracking-tight text-gray-900"}
    "Query count: "
    (count @(re-frame/subscribe [::query-data]))]
   [:ul
    (for [i @(re-frame/subscribe [::query-data])]
      (display/list-item i))]])

;; TODO reimplement with hybrid-resource methods
;; Also only works for ClinVar submisisons
;; Consdier other use cases
(defn evaluation [assertion]
  (->> (:contributions assertion)
       #_(filter #(= "CG:Evaluator" (get-in % [:role :curie])))
       first))
 
(defmethod display/list-item "EvidenceStrengthAssertion"
  [assertion]
  ^{:key assertion}
  [:li
   {:class "relative flex justify-between gap-x-6 px-4 py-5 hover:bg-gray-50"}
   [:div
    (display/list-title (:subject assertion))
    [:p
     {:class "text-sm leading-6 text-gray-500"}
     (get-in assertion [:classification :label])]]
   (if-let [e (evaluation assertion)]
     [:div
      {:class "flex flex-col items-end text-sm text-gray-500"}
      [:p
       (get-in e [:agent :label])]
      [:p
       (:date e)]]
     [:div "donkey"])])

(defmethod display/list-title  "VariantPathogenicityProposition"
  [prop]
  [:p
   {:class "text-sm/6 font-semibold text-gray-900"}
   [:a
    {:href (get-in prop [:variant :iri])
     :target "_blank"}
    (get-in prop [:variant :label])]])
 
