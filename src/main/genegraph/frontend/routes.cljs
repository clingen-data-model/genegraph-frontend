(ns genegraph.frontend.routes
  (:require [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [reitit.core :as r]
            [reitit.frontend.controllers :as rfc]
            [re-frame.core :as re-frame]
            [genegraph.frontend.common :as common]
            [genegraph.frontend.nav :as nav]
            [genegraph.frontend.page.home :as home]
            [genegraph.frontend.page.documentation :as documentation]
            [genegraph.frontend.page.downloads :as downloads]
            [genegraph.frontend.page.resource :as resource-page]
            [genegraph.frontend.page.gencc-home :as gencc-home]
            [genegraph.frontend.page.gencc-list :as gencc-list]
            [genegraph.frontend.page.annotations :as annotations]
            [genegraph.frontend.page.conflict-list :as conflict-list]
            [genegraph.frontend.view :as view]
            [genegraph.frontend.components.search :as search]
            [genegraph.frontend.components.resource :as resource]
            [genegraph.frontend.shell :as shell]))

;;; Effects ;;;

;; Triggering navigation from events.

(re-frame/reg-fx :push-state
  (fn [route]
    (apply rfe/push-state route)))

;;; Events ;;;

(re-frame/reg-event-fx ::push-state
  (fn [_ [_ & route]]
    {:push-state route}))

(re-frame/reg-event-db ::navigated
  (fn [db [_ new-match]]
    (let [old-match   (:current-route db)
          controllers (rfc/apply-controllers (:controllers old-match) new-match)]
      (assoc db :current-route (assoc new-match :controllers controllers)))))

;;; Subscriptions ;;;

(re-frame/reg-sub ::current-route
  (fn [db]
    (:current-route db)))

;;; Views ;;;

(defn home-page []
  [:div
   [:h1 "This is home page"]
   [:button
    ;; Dispatch navigate event that triggers a (side)effect.
    {:on-click #(re-frame/dispatch [::push-state ::sub-page2])}
    "Go to sub-page 2"]])

(defn sub-page1 []
  [:div
   [:h1 "This is sub-page 1"]])

(defn sub-page2 []
  [:div
   [:h1 "This is sub-page 2"]])

;;; Routes ;;;

(defn href
  "Return relative url for given route. Url can be used in HTML links."
  ([k]
   (href k nil nil))
  ([k params]
   (href k params nil))
  ([k params query]
   (rfe/href k params query)))

(def routes
  ["/"
   [""
    {:name      :routes/home
     :view      home/home
     :link-text "Home"
     :controllers
     [{;; Do whatever initialization needed for home page
       ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
       :start (fn [& params]
                (js/console.log "Entering home page")
                (re-frame/dispatch
                 [::common/set-secondary-view nil]))
       ;; Teardown can be done here.
       :stop  (fn [& params]
                (js/console.log "Leaving home page"))}]}]
   ["downloads"
    {:name      :routes/downloads
     :view      downloads/downloads
     :link-text "Downloads"
     :controllers
     [{;; Do whatever initialization needed for home page
       ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
       :start (fn [& params]
                (js/console.log "Entering downloads page")
                (re-frame/dispatch
                 [::common/set-secondary-view nil]))
       ;; Teardown can be done here.
       :stop  (fn [& params]
                (js/console.log "Leaving downloads page"))}]}]
   ["documenation"
    {:name      :routes/documentation
     :view      documentation/documentation
     :link-text "Documentation"
     :controllers
     [{;; Do whatever initialization needed for home page
       ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
       :start (fn [& params]
                (js/console.log "Entering documentation page")
                (re-frame/dispatch
                 [::common/set-secondary-view nil]))
       ;; Teardown can be done here.
       :stop  (fn [& params]
                (js/console.log "Leaving documentation page"))}]}]
   ["documenation/:id"
    {:name      :routes/documentation-term
     :view      documentation/documentation-term
     :link-text "Documentation Term"
     :controllers
     [{;; Do whatever initialization needed for home page
       ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
       :parameters {:path [:id]}
       :start (fn [params]
                (re-frame/dispatch
                 [::documentation/set-current-entity
                  (common/iri-id->kw (get-in params [:path :id]))])
                (re-frame/dispatch
                 [::common/set-secondary-view {:__typename :documentation-list}])
                (js/console.log "Entering documentation page for entity"))
       ;; Teardown can be done here.
       :stop  (fn [& params]
                (js/console.log "Leaving documentation page"))}]}]
   ["r/:id"
    {:name      :routes/resource
     :view      resource-page/resource
     :link-text "Resource"
     :controllers
     [{ ;; Do whatever initialization needed for home page
       ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
       :parameters {:path [:id]}
       :start (fn [params]
                (re-frame/dispatch
                 [::resource-page/nav-to
                  (get-in params [:path :id])])
                (js/console.log "Entering resource page "
                                (get-in params [:path :id])))
       ;; Teardown can be done here.
       :stop  (fn [& params]
                (js/console.log "Leaving resource page"))}]}]
   ["gencc-home"
    {:name      :routes/gencc-home
     :view      gencc-home/home
     :link-text "GenCC"
     :controllers
     [{ ;; Do whatever initialization needed for home page
       ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
       :start (fn [& params]
                (re-frame/dispatch [::nav/nav-state :hidden])
                (js/console.log "Entering gencc page")
                #_(re-frame/dispatch [::home/request-conflict-list]))
       ;; Teardown can be done here.
       :stop  (fn [& params]
                (js/console.log "Leaving gencc page"))}]}]
   ["gencc-list"
    {:name      :routes/gencc-list
     :view      gencc-list/home
     :link-text "GenCC Genes"
     :controllers
     [{;; Do whatever initialization needed for home page
       ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
       :start (fn [& params]
                (re-frame/dispatch [::nav/nav-state :hidden])
                (re-frame/dispatch [::gencc-list/fetch-list])
                (js/console.log "Entering gencc list page")
                #_(re-frame/dispatch [::home/request-conflict-list]))
       ;; Teardown can be done here.
       :stop  (fn [& params]
                (js/console.log "Leaving gencc page"))}]}]
   ["annotations"
    {:name      :routes/annotations
     :view      annotations/annotations
     :link-text "Annotations"
     :controllers
     [{ ;; Do whatever initialization needed for home page
       ;; I.e (re-frame/dispatch [::events/load-something-with-ajax])
       :start (fn [& params]
                (js/console.log "Entering annotations page")
                (re-frame/dispatch [::annotations/request-annotation-list]))
       ;; Teardown can be done here.
       :stop  (fn [& params]
                (js/console.log "Leaving home page"))}]}]
   ["sub-page1"
    {:name      ::sub-page1
     :view      sub-page1
     :link-text "Sub page 1"
     :controllers
     [{:start (fn [& params] (js/console.log "Entering sub-page 1"))
       :stop  (fn [& params] (js/console.log "Leaving sub-page 1"))}]}]
   ["sub-page2"
    {:name      ::sub-page2
     :view      sub-page2
     :link-text "Sub-page 2"
     :controllers
     [{:start (fn [& params] (js/console.log "Entering sub-page 2"))
       :stop  (fn [& params] (js/console.log "Leaving sub-page 2"))}]}]])

(defn on-navigate [new-match]
  (when new-match
    (re-frame/dispatch [::navigated new-match])))

(def router
  (rf/router routes))

(defn init-routes! []
  (js/console.log "initializing routes")
  (rfe/start!
    router
    on-navigate
    {:use-fragment true}))

(defn nav [{:keys [router current-route]}]
  [:ul
   (for [route-name (r/route-names router)
         :let       [route (r/match-by-name router route-name)
                     text (-> route :data :link-text)]]
     [:li {:key route-name}
      (when (= route-name (-> current-route :data :name))
        "> ")
      ;; Create a normal links that user can click
      [:a {:href (href route-name)} text]])])

#_"min-h-full mx-auto max-w-7xl sm:px-6 lg:px-8"
#_(defn router-component [{:keys [router]}]
  (let [current-route @(re-frame/subscribe [::current-route])]
    [:div {:class "container"}
     #_[nav/nav]
     (when current-route
       [(-> current-route :data :view)])]))

#_(defn router-component [{:keys [router]}]
  (let [current-route @(re-frame/subscribe [::current-route])]
    (view/app-div)))

(defn router-component [{:keys [router]}]
  (let [current-route @(re-frame/subscribe [::current-route])]
    (shell/shell)))
