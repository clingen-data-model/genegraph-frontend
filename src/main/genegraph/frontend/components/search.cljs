(ns genegraph.frontend.components.search
  (:require [re-frame.core :as rf]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.common :as common]
            [genegraph.frontend.user :as user]
            [genegraph.frontend.queries :as queries]
            [genegraph.frontend.icon :as icon]
            [genegraph.frontend.components.assertion :as assertion]
            [clojure.string :as str]))

(def text-index-query
  "query ($query: String) {
  textSearch(query: $query) {
    __typename
    iri
    curie
    label
    type {
      __typename
      curie
      label
      type {
        curie
      }
    }
  }
}")


(rf/reg-event-db
 ::recieve-result
 (fn [db [_ result]]
   (assoc db
          ::result
          (get-in result [:response :data :textSearch]))))

(rf/reg-event-fx
 ::update-search-input
 (fn [{:keys [db]} [_ value]]
   {:db (assoc db ::search-input value)
    :fx [[:dispatch
          [::re-graph/query
           {:id ::query
            :query text-index-query
            :variables {:query value}
            :callback [::recieve-result]}]]]}))

(rf/reg-event-fx
 ::text-search
 (fn [{:keys [db]} [_ text]]
   (js/console.log "searching for " (::search-input db))
   {:fx [[:dispatch
          [::re-graph/query
           {:id ::query
            :query text-index-query
            :variables {:symbol (str/upper-case (::search-input db))} ; change 
            :callback [::recieve-text-search-result]}]]]}))

(rf/reg-event-db
 ::reset-search
 (fn [db _]
   (assoc db ::search-input nil)))

(rf/reg-sub
 ::result
 :-> ::result)

(defn search-result-div []
  (let [result @(rf/subscribe [::result])]
    [:ul
     {:role "list", :class "divide-y divide-gray-100 px-12 pt-8"}
     (for [r result]
       ^{:key r}
       [:li
        {:class "flex items-center justify-between gap-x-6 py-5"}
        [:div
         {:class "min-w-0"}
         [:div
          {:class "flex items-start gap-x-3"}
          [:a
           {:class "text-sm/6 font-semibold text-gray-900"
            :href (common/resource-href r)
            :on-click #(rf/dispatch [::reset-search])}
           (:label r)]
          (for [t (:type r)]
            ^{:key [r t]}
            [:p
             {:class
              "mt-0.5 rounded-md bg-green-50 px-1.5 py-0.5 text-xs font-medium whitespace-nowrap text-green-700 ring-1 ring-green-600/20 ring-inset"}
             (:label t)])]]])]))
