(ns genegraph.frontend.page.filter
  (:require [re-frame.core :as rf]
            [genegraph.frontend.filters :as filters]))

(defn display-results-div [results]
  [:div
   [:div
    {:on-click #(rf/dispatch [::filters/clear-query-result])}
    "clear"]
   [:pre (with-out-str (cljs.pprint/pprint results))]])

(defn select-filter-div []
  [:div
   {:class "py-12 px-16"}
   [:div
    {:class "px-4 sm:px-0"}
    [:h3
     {:class "text-base/7 font-semibold text-gray-900 dark:text-white"}
     "Filters"]
    [:p
     {:class "mt-1 max-w-2xl text-sm/6 text-gray-500 dark:text-gray-400"}
     "Select sets of assertions by limiting to those that match specific criteria."]]
   [:div
    {:class "mt-6 border-t border-gray-100 dark:border-white/10"}
    [:dl
     {:class "divide-y divide-gray-100 dark:divide-white/10"}
     (for [f filters/queries]
       ^{:key f}
       [:div
        {:class "px-4 py-6 sm:grid sm:grid-cols-3 sm:gap-4 sm:px-0"
         :on-click #(rf/dispatch [::filters/send-query (:filters f)])}
        [:dt
         {:class "text-sm/6 font-medium text-gray-900 dark:text-gray-100"}
         (:label f)]
        [:dd
         {:class
          "mt-1 text-sm/6 text-gray-700 sm:col-span-2 sm:mt-0 dark:text-gray-400"}
         (:description f)]])]]])

(defn filter-div []
  (if-let [results @(rf/subscribe [::filters/query-result])]
    (display-results-div results)
    (select-filter-div)))
