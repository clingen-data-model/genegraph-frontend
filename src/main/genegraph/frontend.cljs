(ns genegraph.frontend
  (:require ["react-dom/client" :refer [createRoot]]
            [reagent.core :as reagent]
            [goog.dom :as gdom]
            [reagent.dom :as rdom]
            [reagent.core :as r]
            [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [genegraph.frontend.routes :as routes]
            [genegraph.frontend.page.conflict-list :as conflict-list]))

(enable-console-print!)

(defonce root (createRoot (gdom/getElement "app")))

(goog-define BACKEND_WS "ws://localhost:8888/ws")
(goog-define BACKEND_HTTP "http://localhost:8888/api")

(defonce match (reagent/atom nil))

(defn home-page []
  [:div
   [:h2 "Welcome to frontend"]

   [:button
    {:type "button"
     :on-click #(rfe/push-state ::item {:id 3})}
    "Item 3"]

   [:button
    {:type "button"
     :on-click #(rfe/replace-state ::item {:id 4})}
    "Replace State Item 4"]])

(defn about-page []
  [:div
   [:h2 "About frontend"]
   [:ul
    [:li [:a {:href "http://google.com"} "external link"]]
    [:li [:a {:href (rfe/href ::foobar)} "Missing route"]]
    [:li [:a {:href (rfe/href ::item)} "Missing route params"]]]

   [:div
    {:content-editable true
     :suppressContentEditableWarning true}
    [:p "Link inside contentEditable element is ignored."]
    [:a {:href (rfe/href ::frontpage)} "Link"]]])

(defn item-page [match]
  (let [{:keys [path query]} (:parameters match)
        {:keys [id]} path]
    [:div
     [:h2 "Selected item " id]
     (if (:foo query)
       [:p "Optional foo query param: " (:foo query)])]))

(defn current-page []
  [:div
   [:ul
    [:li [:a.text-blue-600 {:href (rfe/href ::frontpage)} "Frontpage"]]
    [:li [:a {:href (rfe/href ::about)} "About"]]
    [:li [:a {:href (rfe/href ::item {:id 1})} "Item 1"]]
    [:li [:a {:href (rfe/href ::item {:id 2} {:foo "bar"})} "Item 2"]]]
   (if @match
     (let [view (:view (:data @match))]
       [view @match]))
   [:pre @match]])

(defn ^:dev/after-load render-root []
  (println "[main] reloaded lib:")
  (routes/init-routes!)  
  (.render root (r/as-element [conflict-list/conflict-list])))

(defn ^:export init []
  (re-frame/dispatch [::re-graph/init
                      {:ws nil #_{:url BACKEND_WS}
                       :http {:url BACKEND_HTTP
                              :impl {:headers {"Access-Control-Allow-Credentials" true}}}}])
  (render-root))
