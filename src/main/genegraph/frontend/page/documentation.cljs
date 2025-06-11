(ns genegraph.frontend.page.documentation
  (:require [re-frame.core :as rf]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.icon :as icon]
            [genegraph.frontend.common :as common]
            [genegraph.schema :as schema]
            [clojure.string :as s]))

(rf/reg-event-db
 ::set-current-entity
 (fn [db [_ entity]]
   (assoc db ::current-entity entity)))

(rf/reg-sub
 ::current-entity
 :-> ::current-entity)

(def entity-types
  [{:type :classes
    :label "Classes"}
   {:type :properties
    :label "Properties"}
   {:type :value-sets
    :label "Value Sets"}
   {:type :concepts
    :label "Concepts"}])

(defn entity-list [entity-class]
  ^{:key entity-class}
  [:div
   {:class "pt-12"}
   [:div
    {:class "border-b border-gray-200 pb-5"}
    [:h3
     {:class "text-base font-semibold text-gray-900"}
     (:label entity-class)]]
   [:ul
    {:role "list", :class "divide-y divide-gray-200"}
    (for [[k v] (sort-by key (get schema/schema (:type entity-class)))]
      ^{:key k}
      [:li
       {:class "py-4"}
       [:a
        {:href (rfe/href :routes/documentation-term {:id (common/kw->iri-id k)})}
        (str k)]])]])

(defn documentation []
  [:div
   {:class "px-12 py-12"}
   [:div
    {:class "min-w-0 flex-1"}
    [:h2
     {:class
      "text-2xl/7 font-bold text-gray-900 sm:truncate sm:text-3xl sm:tracking-tight"}
     "Documentation"]]
   (for [t entity-types]
     (entity-list t))])

#_(defn kw->name [kw]
)



(defn documentation-term []
  (let [entity-kw @(rf/subscribe [::current-entity])
        entity (get schema/schema-by-id entity-kw)]
    [:div
     {:class "px-12 py-12"}
     [:div
      {:class "border-b border-gray-200 pb-5"}
      [:h3
       {:class "text-base font-semibold text-gray-900"}
       entity-kw]
      [:p
       {:class "mt-2 max-w-4xl text-sm text-gray-500"}
       (:dc/description entity)]]
     [:div
      [:div
       {:class "border-b border-gray-200 pb-5 pt-10"}
       [:h3 {:class "text-base font-semibold text-gray-900"} "properties"]] 
      [:ul
       {:role "list", :class "divide-y divide-gray-200"}
       (for [p (:properties entity)]
         [:li {:class "py-4"}
          [:div p]])]]
     #_[:pre (with-out-str (cljs.pprint/pprint schema/schema-by-id))]]))
