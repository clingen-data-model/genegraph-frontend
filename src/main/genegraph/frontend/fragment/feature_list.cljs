(ns genegraph.frontend.fragment.feature-list
  (:require [genegraph.frontend.fragment.assertion-list :as assertion-list]
            [genegraph.frontend.common :as common]))

(defn feature-div [feature]
  ^{:key feature}
  [:li
   {:class "py-1"}
   [:a
    {:class "font-medium"
     :href (common/resource-href feature)}
    (:label feature "feature!")]])

(defn feature-list-div [features]
  [:ul
   {:role "list",
    :class
    "divide-y divide-gray-100 overflow-hidden bg-white shadow-xs ring-1 ring-gray-900/5 sm:rounded-xl"}
   (for [f features]
     (feature-div f))])
