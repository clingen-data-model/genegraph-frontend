(ns genegraph.frontend.page.sequence-feature
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.user :as user]
            [genegraph.frontend.icon :as icon]
            [genegraph.frontend.common :as common]))

(def activity-priority
  ["CG:Approver"
   "CG:Evaluator"
   "CG:Publisher"])

(defn contrib-by-activity [contributions]
  (reduce (fn [m c] (assoc m (get-in c [:role :curie]) c))
          {}
          contributions))

(defn primary-contribution [contributions]
  (let [by-activity (contrib-by-activity contributions)]
    (some #(get by-activity %) activity-priority)))

(defn assertion-date [contributions]
  (let [by-activity (contrib-by-activity contributions)]
    #_(some #(get by-activity %) activity-priority)
    (if-let [contrib (some #(get by-activity %) activity-priority)]
      (subs (:date contrib) 0 4)
      "")))

(defn assertion-div [a]
  ^{:key a}
  [:div
   {:class "flex"}
   (common/pill (get-in a [:subject :__typename]))
   (common/pill (get-in a [:evidenceStrength :curie]))
   [:div (assertion-date (:contributions a))]])

(defn sequence-feature-div [sequence-feature]
  [:div
   {:class "px-8"}
   [:div
    (:label sequence-feature)]
   (for [a (:assertions sequence-feature)]
     (assertion-div a))])
