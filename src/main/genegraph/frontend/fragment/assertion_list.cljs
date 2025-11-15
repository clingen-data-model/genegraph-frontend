(ns genegraph.frontend.fragment.assertion-list
  (:require [re-frame.core :as rf]
            [genegraph.frontend.filters :as filters]
            [genegraph.frontend.common :as common]
            [genegraph.frontend.icon :as icon]))


(defn contrib-div [contrib]
  [:div
   {:class "flex shrink-0 items-center gap-x-4"}
   [:div
    {:class "hidden sm:flex sm:flex-col sm:items-end"}
    [:p {:class "text-sm/6 text-gray-900"}
     (or (get-in contrib [:agent :label])
         (get-in contrib [:agent :curie]))]
    [:p
     {:class "mt-1 text-xs/5 text-gray-500"}
     [:time {:dateTime (:date contrib)}
      (if (:date contrib)
        (subs (:date contrib) 0 10)
        "")]]]
   icon/arrow])

(defn contrib-with-role [assertion role]
  (first (filter
          #(= role (get-in % [:role :curie]))
          (:contributions assertion))))

(defn priority-contrib [assertion]
  (some #(contrib-with-role assertion %)
        ["CG:Evaluator" "CG:Creator" "CG:Submitter"]))

(defn assertion-div [result]
  ^{:key result}
  [:li
   {:class
    "relative flex justify-between gap-x-6 px-4 py-5 hover:bg-gray-50 sm:px-6"}
   [:div
    {:class "text-sm/6 font-semibold text-gray-900 flex flex-col"}
    [:div
     {:class "flex gap-4"}
     [:div (get-in result [:subject :variant :copyChange :label])]
     [:a
      {:href (common/resource-href result)}
      (:label result)]]
    [:div (get-in result [:classification :label])]]
   (contrib-div (priority-contrib result ))])

(defn assertion-list-div [assertions]
  [:div
   [:ul
    {:role "list",
     :class
     "divide-y divide-gray-100 overflow-hidden bg-white shadow-xs ring-1 ring-gray-900/5 sm:rounded-xl"}
    (for [r (take 5 assertions)]
      (assertion-div r))]
   [:pre (with-out-str (cljs.pprint/pprint assertions))]])
