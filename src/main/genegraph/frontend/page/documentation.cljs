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

(defn term-href [term]
  (rfe/href :routes/documentation-term {:id (common/kw->iri-id term)}))

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
    (for [[k v] (sort-by key (get (schema/schema) (:type entity-class)))]
      ^{:key k}
      [:li
       {:class "py-4"}
       [:a
        {:href (term-href k)}
        k]])]])

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

(defn term-list [terms]
  (let [schema (schema/schema-by-id)]
    [:ul
     {:role "list", :class "divide-y divide-gray-200"}
     (for [t terms]
       ^{:key t}
       [:li {:class "py-4"}
        [:div
         {:class "flex"}
         [:div
          {:class "min-w-80"}
          [:a
           {:href (term-href t)}
           t]]
         [:div
          (get-in schema [t :dc/description])]]])]))



#_(defn term-list [terms]
    [:div
     {:role "list", :class "grid grid-cols-2"}
     (for [t terms]
       ^{:key t}
       [:div
        [:div {:class "py-4"}
         [:a
          {:href (term-href t)}
          t]]
        [:div
         "description"
         #_(:dc/description t)]])])

(defmulti entity-detail :rdf/type)

(defmethod entity-detail :owl/Class [entity entity-kw]
  [:div
   [:div
    {:class "border-b border-gray-200 pb-5 pt-10"}
    [:h3 {:class "text-base font-semibold text-gray-900"} "properties"]]
   (term-list (:properties entity))])

(defmethod entity-detail :rdf/Property [entity entity-kw]
  [:div
   [:div
    {:class "border-b border-gray-200 pb-5 pt-10"}
    [:h3 {:class "text-base font-semibold text-gray-900"} "classes using property"]]
   (term-list (:used-in entity))])

(defmethod entity-detail :cg/ValueSet [entity entity-kw]
  [:div
   [:div
    {:class "border-b border-gray-200 pb-5 pt-10"}
    [:h3 {:class "text-base font-semibold text-gray-900"}
     "members"]]
   (term-list (:skos/member entity))])

(defmethod entity-detail :skos/Concept [entity entity-kw]
  [:div
   [:div
    {:class "border-b border-gray-200 pb-5 pt-10"}
    [:h3 {:class "text-base font-semibold text-gray-900"}
     "member of value sets"]]
   (term-list (:skos/inScheme entity))])

(defmethod entity-detail :default [entity entity-kw]
  [:div])

(defn entity-title [entity entity-kw]
  [:div
   {:class "border-b border-gray-200 pb-5"}
   [:div
    {:class "pb-4"}
    [:h3
     {:class "text-xl font-semibold text-gray-900"}
     entity-kw]
    [:p (:rdf/type entity)]
    [:p (:type entity)]]
   [:p
    {:class "mt-2 max-w-4xl text-sm text-gray-500"}
    (or (:markup entity)
        (:dc/description entity))]])

(defn documentation-term []
  (let [entity-kw @(rf/subscribe [::current-entity])
        entity (get (schema/schema-by-id) entity-kw)]
    [:div
     {:class "px-12 py-12"}
     (entity-title entity entity-kw)
     (entity-detail entity entity-kw)]))


(defmethod common/secondary-view :documentation-list []
  [:div
   (for [t entity-types]
     (entity-list t))])
