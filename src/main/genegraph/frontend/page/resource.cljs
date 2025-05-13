(ns genegraph.frontend.page.resource
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.user :as user]
            [genegraph.frontend.queries :as queries]
            [genegraph.frontend.icon :as icon]))

(defn resource-div []
  [:div
   {:class "flex"
    "resource div"}])
