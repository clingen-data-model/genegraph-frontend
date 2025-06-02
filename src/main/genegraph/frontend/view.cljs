(ns genegraph.frontend.view
    (:require [re-frame.core :as re-frame]
              [re-graph.core :as re-graph]
              [reitit.frontend.easy :as rfe]
              [genegraph.frontend.user :as user]
              [genegraph.frontend.queries :as queries]
              [genegraph.frontend.icon :as icon]
              [genegraph.frontend.components.search :as search]
              [genegraph.frontend.components.resource :as resource]
              [genegraph.frontend.components.assertion :as assertion]
              [clojure.string :as str]))

(re-frame/reg-event-db
 ::init
 (fn [db _]
   (assoc db ::stack [{:component :search-dialog}])))

(re-frame/reg-sub
 ::stack
 :-> ::stack)

(re-frame/reg-event-db
 ::push
 (fn [db [_ component source]]
   (let [base-stack (into [] (take-while #(not= (or source component) %)
                                         (::stack db)))]
     (assoc db
            ::stack
            (if source
              (conj base-stack source component)
              (conj base-stack component))))))

(defn render-component [component-def]
  (case (:component component-def)
    :search-dialog (search/search-div)
    :search-result (search/search-result-div)
    :assertion (assertion/assertion-detail-div component-def)))

(def component-type->icon
  {:search-dialog icon/magnifying-glass
   :search-result icon/list-bullet})

(defn component-nav-icon [component]
  ^{:key component}
  [:div
   {:on-click #(re-frame/dispatch [::push component])}
   (component-type->icon (:component component))])

(defn nav-bar []
  (let [stack @(re-frame/subscribe [::stack])]
    [:div
     {:class "flex-col"}
     (for [c (drop-last 2 stack)]
       (component-nav-icon c))
     #_[:pre (with-out-str (cljs.pprint/pprint stack))]]))

(defn app-div []
  (let [stack @(re-frame/subscribe [::stack])]
    [:div
     {:class "container"}
     [:div
      {:class "mt-8 mx-auto max-w-7xl sm:px-6 lg:px-8 flex"}
      (nav-bar)
      (if-let [previous-component (->> stack reverse second)]
        (render-component previous-component)
        [:div])
      (render-component (last stack))]])
  )
