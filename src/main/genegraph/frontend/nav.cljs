(ns genegraph.frontend.nav
  "DEPRECATED
  Old nav for ClinGen filter-based queries. Left as example for now"
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.user :as user]
            [genegraph.frontend.filters :as queries] ;; Refactor at some point
            [genegraph.frontend.icon :as icon]))

(re-frame/reg-event-db
 ::recieve-query-result
 (fn [db [_ result]]
   (js/console.log "recieved query")
   (assoc db :query-data (get-in result [:response :data :assertions]))))

(re-frame/reg-event-fx
 ::execute-query
 (fn [{:keys [db]} [_ query]]
   (js/console.log "running query")
   {:db (assoc db
               ::current-query query
               ::current-mode :view-data)
    :fx [[:dispatch
          [::re-graph/query
           {:id ::query
            :query queries/graphql-query
            :variables {:filters (:filters query)}
            :callback [::recieve-query-result]}]]]}))

;; copy number gain EFO:0030070
;; copy number loss EFO:0030067

(re-frame/reg-event-db
 ::set-current-query
 (fn [db [_ query]]
   (assoc db
          ::current-query query
          ::current-mode :view-data)))

(re-frame/reg-event-db
 ::set-mode
 (fn [db [_ mode]]
   (js/console.log "Setting mode " (str mode))
   (assoc db ::current-mode mode)))

(re-frame/reg-sub
 ::current-query
 :-> ::current-query)

(re-frame/reg-sub
 ::current-mode
 :-> ::current-mode)

(re-frame/reg-event-db
 ::set-show-user-menu
 (fn [db [_ show-menu]]
   (assoc db ::show-user-menu show-menu)))

(re-frame/reg-sub
 ::show-user-menu
 :-> ::show-user-menu)

(def link-arrow
  [:svg
   {:class "size-5 flex-none text-gray-400",
    :viewBox "0 0 20 20",
    :fill "currentColor",
    :aria-hidden "true",
    :data-slot "icon"}
   [:path
    {:fill-rule "evenodd",
     :d
     "M8.22 5.22a.75.75 0 0 1 1.06 0l4.25 4.25a.75.75 0 0 1 0 1.06l-4.25 4.25a.75.75 0 0 1-1.06-1.06L11.94 10 8.22 6.28a.75.75 0 0 1 0-1.06Z",
     :clip-rule "evenodd"}]])

(defn select-query []
  (for [query queries/queries]
    ^{:key query}
    [:div
     {:class "pt-1 pb-8"}
     [:ul
      {:role "list", :class "divide-y divide-gray-100"}
      [:li
       {:class "relative flex justify-between py-5"
        :on-click #(re-frame/dispatch [::execute-query query])}
       [:div
        {:class "flex gap-x-4 pr-6 sm:w-1/2 sm:flex-none"}
        [:div
         {:class "min-w-0 flex-auto"}
         [:p
          {:class "text-sm/6 font-semibold text-gray-900"}
          [:a
           {:href "#"}
           [:span {:class "absolute inset-x-0 -top-px bottom-0"}]
           (:label query)]]]]
       [:div
        {:class
         "flex items-center justify-between gap-x-4 sm:w-1/2 sm:flex-none"}
        [:p
         {:class "text-sm/5"}
         (:description query)]
        link-arrow]]]]))

(def clingen-logo
  [:div
   {:class "flex items-center"}
   [:img
    {:class "h-8 w-auto",
     :src "/img/clingen-logo.svg"
     :alt "ClinGen"}]])




(defn query-label []
  [:div
   {:class "flex flex-1 justify-end text-sm font-medium text-gray-900 hover:text-gray-500"
    :on-click #(re-frame/dispatch [::set-mode :select-query])}
   (if-let [label (:label @(re-frame/subscribe [::current-query]))]
     label
     "select query")])

(defn user-nav []
  (let [user (get user/users @(re-frame/subscribe [::user/current-user]))]
    [:div
     {:class "flex flex-1 justify-end"}
     [:a
      {:href "#", :class "text-sm font-medium text-gray-900 hover:text-gray-500"
       :on-click #(re-frame/dispatch [::set-mode :select-user])}
      (if user
        user
        "Log in")]]))

(defn select-user []
  [:div
   {:class "flex justify-end text-sm"}
   [:ul
    (for [[id label] user/users]
      ^{:key id}
      [:li
       {:on-click #(doseq [e [[::user/set-current-user id]
                              [::set-mode nil]]]
                    (re-frame/dispatch e))}
       label])]])

(defn nav-menu []
  (case @(re-frame/subscribe [::current-mode])
    :select-query (select-query)
    :select-user (select-user)
    [:div]))

(re-frame/reg-event-db
 ::nav-state
 (fn [db [_ state]]
   (assoc db ::nav-state state)))

(re-frame/reg-sub
 ::nav-state
 :-> ::nav-state)

#_(defn search-div []
  [:div
   {:class "flex flex-1 ml-6"}
   icon/magnifying-glass])

(defn search-div []
  [:div
   {:class "mt-2 grid grid-cols-1"}
   [:input
    {:type "email",
     :name "email",
     :id "email",
     :class
     "col-start-1 row-start-1 block w-full rounded-md bg-white py-1.5 pr-3 pl-10 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:pl-9 sm:text-sm/6",
     :placeholder "gene symbol"}]
   [:div
    {:class
     "pointer-events-none col-start-1 row-start-1 ml-3 size-5 self-center text-gray-400 sm:size-4",
     :viewBox "0 0 16 16",
     :fill "currentColor",
     :aria-hidden "true",
     :data-slot "icon"}
    icon/magnifying-glass]])

(defn nav []
  (if-not (= :hidden @(re-frame/subscribe [::nav-state]))
    [:header
     {:class "bg-white p-6 px-8"}
     [:nav
      {:class
       "mx-auto flex max-w-7xl items-center justify-between ",
       :aria-label "Global"}
      clingen-logo
      (search-div)
      (query-label)
      (user-nav)]
     [:div
      (nav-menu)]]
    [:div]))


