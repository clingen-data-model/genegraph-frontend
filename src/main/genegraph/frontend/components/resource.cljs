(ns genegraph.frontend.components.resource
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.user :as user]
            [genegraph.frontend.queries :as queries]
            [genegraph.frontend.icon :as icon]
            [genegraph.frontend.page.sequence-feature :as sequence-feature]))

(re-frame/reg-sub
 ::current-resource
 :-> :current-resource)

#_[:pre (with-out-str (cljs.pprint/pprint r))]
(defn resource-div []
  (if-let [r @(re-frame/subscribe [::current-resource])]
    (case (:__typename r)
          "SequenceFeature" (sequence-feature/sequence-feature-div r))))
