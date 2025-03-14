(ns genegraph.frontend.page.home
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.display :as display]
            [genegraph.frontend.user :as user]
            [genegraph.frontend.icon :as icon]
            [genegraph.frontend.display.annotation :as annotation]))

(re-frame/reg-sub
 ::query-data
 :-> :query-data)

(re-frame/reg-event-db
 ::set-currently-annotating
 (fn [db [_ i]]
   (assoc db ::currently-annotating i)))

(re-frame/reg-sub
 ::currently-annotating
 :-> ::currently-annotating)

(re-frame/reg-sub
 ::annotation
 :=> (fn [db iri] (get-in db [::annotations iri])))

(defn evaluation [assertion]
  (->> (:contributions assertion)
       #_(filter #(= "CG:Evaluator" (get-in % [:role :curie])))
       first))

(defn annotation-div [assertion]
  [:div
   {:class "flex p-1"
    :on-click #(re-frame/dispatch [::annotation/set-currently-annotating (:iri assertion)])}
   icon/document-text-outline])

(defn assertion-list-item [assertion current-user currently-annotating]
  [assertion]
  ^{:key assertion}
  [:li
   {:class (if currently-annotating
             "px-4 py-5 bg-gray-100 rounded-xl"
             "px-4 py-5")}
   [:div
    {:class "relative flex flex-1 justify-between gap-x-6 hover:bg-gray-50"}
    [:div
     {:class "relative flex p-1"}
     (when (seq (:annotations assertion))
       [:div
        {:class "gray-500"}
        icon/document-micro])]
    [:div
     (display/list-title (:subject assertion))
     [:p
      {:class "text-sm leading-6 text-gray-500"}
      (get-in assertion [:classification :label])]]
    (if-let [e (evaluation assertion)]
      [:div
       {:class "flex flex-1 flex-col items-end text-sm text-gray-500"}
       [:p
        (get-in e [:agent :label])]
       [:p
        (:date e)]]
      [:div "donkey"])
    (when current-user
      (annotation-div assertion))]
   (when currently-annotating
     (annotation/annotation-dialog assertion))])

(defn home []
  (let [current-user @(re-frame/subscribe [::user/current-user])
        currently-annotating @(re-frame/subscribe [::annotation/currently-annotating])]
    [:div
     {:class "py-10 px-10"}
     [:ul
      (doall
       (for [i @(re-frame/subscribe [::query-data])]
         (if (= currently-annotating (:iri i))
           (assertion-list-item i current-user currently-annotating)
           (assertion-list-item i current-user false))))]]))

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
 
