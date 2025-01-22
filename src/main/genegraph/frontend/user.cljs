(ns genegraph.frontend.user
  (:require [re-frame.core :as re-frame]))

(def users
  {1 "Tasha"
   2 "Tracy"
   3 "Tristan"})

(re-frame/reg-event-db
 ::set-current-user
 (fn [db [_ user-id]]
   (assoc db ::current-user user-id)))

(re-frame/reg-sub
 ::current-user
 :-> ::current-user)
