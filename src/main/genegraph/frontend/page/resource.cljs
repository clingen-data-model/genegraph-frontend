(ns genegraph.frontend.page.resource
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.user :as user]
            [genegraph.frontend.queries :as queries]
            [genegraph.frontend.icon :as icon]
            [genegraph.frontend.common :as common]))

(re-frame/reg-event-db
 ::set-current-resource
 (fn [db [_ r]]
   (js/console.log "setting current resource")
   (js/console.log r)
   (assoc db ::current-resource r)))

(re-frame/reg-sub
 ::current-resource
 :-> ::current-resource)

;; temp for now, gets us back to the display of genes
(re-frame/reg-sub
 ::main
 :-> :main)

(defn resource []
  (let [current @(re-frame/subscribe [::main])]
    [:div
     {:class "px-4 py-10 sm:px-6 lg:px-8 lg:py-6"}
     (if current
       (common/main-view current)
       [:div])
     #_[:pre (with-out-str (cljs.pprint/pprint current))]]))

#_(defn resource []
  (let [current-route @(re-frame/subscribe [::current-resource])
        current @(re-frame/subscribe [::main])]
    [:div
     [:pre (with-out-str (cljs.pprint/pprint current))]]))
