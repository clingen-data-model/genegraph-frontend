(ns genegraph.frontend.routes
  (:require [reitit.frontend :as rf]
            [reitit.frontend.easy :as rfe]
            [re-frame.core :as re-frame]
            [genegraph.frontend.page.home :as home]
            [genegraph.frontend.page.conflict-list :as conflict-list]))



(def routes
  [["/"
    {:name ::frontpage
     :view home/home-page}]

   #_["/about"
      {:name ::about
       :view about-page}]
   
   #_["/item/:id"
      {:name ::item
       :view item-page
       :parameters {:path {:id int?}}}]])

(def router (rf/router routes))

(defn on-navigate [new-match]
  (when new-match
    (re-frame/dispatch [::navigated new-match])))

(defn init-routes! []
  (rfe/start!
   router
   on-navigate
   {:use-fragment true}))
