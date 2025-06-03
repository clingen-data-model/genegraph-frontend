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

#_[:pre (with-out-str (cljs.pprint/pprint stack))]

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
       (component-nav-icon c))]))

#_(defn app-div []
  (let [stack @(re-frame/subscribe [::stack])]
    [:div
     {:class "container"}
     [:div
      (nav-bar)
      (if-let [previous-component (->> stack reverse second)]
        [:div
         [:div
          {:class "fixed top-0 left-10 bottom-0 w-150  overflow-scroll"}
          (render-component previous-component)]
         [:div
          {:class "fixed top-0 left-170 bottom-0 w-150 overflow-scroll"}
          (render-component (last stack))]]

        [:div
         {:class "fixed top-0 left-10 w-100 overflow-scroll"}
         (render-component (last stack))])]]))


(defn app-div []
  (let [stack @(re-frame/subscribe [::stack])
        previous-component(->> stack reverse second)]
    [:div
     {:class "container"}
     [:div
      {:class "fixed top-0 left-0 bottom-0 overflow-y-auto"}
      (nav-bar)]
     (if previous-component
       [:div
        {:class "fixed top-0 left-10 bottom-0 w-120  overflow-y-auto"}
        (render-component previous-component)]
       [:div
        {:class "fixed top-0 left-10 bottom-0 w-150 overflow-scroll"}
        (render-component (last stack))])
     (if previous-component
       [:div
        {:class "fixed top-0 bottom-0 left-130 w-full overflow-scroll"}
        (render-component (last stack))]
       [:div])]))
