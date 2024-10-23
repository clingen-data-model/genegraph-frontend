(ns genegraph.frontend.page.conflict-list
  (:require [reitit.frontend.easy :as rfe]))


(defn conflict-list []
  [:div
   [:ul
    [:li [:a.text-blue-600 {:href (rfe/href ::frontpage)} "Conflict Listicle"]]
    [:li [:a {:href (rfe/href ::about)} "About"]]
    [:li [:a {:href (rfe/href ::item {:id 1})} "Item 1"]]
    [:li [:a {:href (rfe/href ::item {:id 2} {:foo "bar"})} "Item 2"]]]])
