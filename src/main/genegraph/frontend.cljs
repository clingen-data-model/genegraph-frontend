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
            [genegraph.frontend.page.conflict-list :as conflict-list]
            [genegraph.frontend.display.variant]
            [genegraph.frontend.display.assertion]
            [genegraph.frontend.view :as view]
            ["firebase/app" :as firebase]
            ["firebase/auth" :as auth]
            [genegraph.frontend.auth :as gg-auth]))

(enable-console-print!)

(defonce root (createRoot (gdom/getElement "app")))

(goog-define BACKEND_WS "ws://localhost:8888/ws")
(goog-define BACKEND_HTTP "http://localhost:8888/api")
(goog-define ENV "dev")

(js/console.log (str ENV))

(defonce match (reagent/atom nil))

(defn firebase-init
  []
  (firebase/initializeApp
   #js {:apiKey "AIzaSyD_TLzPJT3IxX59b_7raOyLka11kLYGYg0",
        :authDomain "som-clingen-projects.firebaseapp.com",
        :projectId "som-clingen-projects",
        :storageBucket "som-clingen-projects.firebasestorage.app",
        :messagingSenderId "653902215137",
        :appId "1:653902215137:web:0fb88da97e58e7ab2644a0"})
  #_(firebase/app)
  #_(fb-auth/on-auth-state-changed))


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

(re-frame/reg-event-db
 ::initialize-db
 (fn [db _]
   (if db
     db
     {:current-route nil})))

(defn ^:dev/after-load render-root []
  (println "[main] reloaded lib:")
  (routes/init-routes!)  
  (.render root (reagent/as-element [routes/router-component
                               {:router routes/router}])))

(re-frame/reg-event-db
 ::initialize-firebase
 (fn [db _]
   (let [app (firebase-init)]
     (assoc db
            :firebase-app app
            :firebase-auth (auth/getAuth)))))

(re-frame/reg-event-fx
 :update-user-token
 (fn [_ [_ token]]
   (js/console.log token)
   #_{}
   {:fx [[:dispatch
          [::re-graph/init
           {:ws nil #_{:url BACKEND_WS}
            :http {:url BACKEND_HTTP
                   :impl {:headers {"Access-Control-Allow-Credentials" true
                                    "Authorization" token}}}}]]]}))

(defn ^:export init []
  (js/console.log (str "ENV" ENV))
  (js/console.log (str "BACKEND_WS" BACKEND_WS))
  (js/console.log (str "BACKEND_HTTP" BACKEND_HTTP))
  #_(firebase-init)
  (re-frame/clear-subscription-cache!)
  (re-frame/dispatch [::re-graph/init
                      {:ws nil #_{:url BACKEND_WS}
                       :http {:url BACKEND_HTTP
                              :impl {:headers {"Access-Control-Allow-Credentials" true}}}}])
  (re-frame/dispatch-sync [::initialize-db])
  (re-frame/dispatch-sync [::initialize-firebase])
  (re-frame/dispatch-sync [::gg-auth/init-auth])
  (re-frame/dispatch-sync [::view/init])
  (render-root))
