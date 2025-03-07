(ns genegraph.frontend.nav
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.user :as user]
            [genegraph.frontend.queries :as queries]))

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

(defn nav-link [name text current-route]
  [:a
   {:href (rfe/href name),
    :class
    (if (= name (get-in current-route [:data :name]))
      "inline-flex items-center border-b-2 border-indigo-500 px-1 pt-1 text-sm font-medium text-gray-900"
      "inline-flex items-center border-b-2 border-transparent px-1 pt-1 text-sm font-medium text-gray-500 hover:border-gray-300 hover:text-gray-700")}
   text])

(defn query-label [query]
  [:span
   {:class
    "inline-flex items-center border-b-2 border-transparent px-1 pt-1 text-sm font-medium text-gray-500 hover:border-gray-300 hover:text-gray-700"}
   (:label query)])

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

(defn query-select []
  (for [query queries/queries]
    ^{:key query}
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
       link-arrow]]]))

(defn query-edit []
  )

(defn user []
  [:div
   {:class "hidden sm:ml-6 sm:flex sm:items-center"}
   [:button
    {:type "button",
     :class
     "relative rounded-full bg-white p-1 text-gray-400 hover:text-gray-500 focus:outline-hidden focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"}
    [:span {:class "absolute -inset-1.5"}]
    [:span {:class "sr-only"} "View notifications"]
    (get
     user/users
     @(re-frame/subscribe [::user/current-user]))]
   [:div
    {:class "relative ml-3"}
    [:div
     [:button
      {:type "button",
       :class
       "relative flex rounded-full bg-white text-sm focus:outline-hidden focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2",
       :id "user-menu-button",
       :aria-expanded "false",
       :aria-haspopup "true"}
      [:span {:class "absolute -inset-1.5"}]
      [:span {:class "sr-only"} "Open user menu"]]]
]])

(defn mobile-menu-button []
  [:div
   {:class "-mr-2 flex items-center sm:hidden"}
   (comment "Mobile menu button")
   [:button
    {:type "button",
     :class
     "relative inline-flex items-center justify-center rounded-md p-2 text-gray-400 hover:bg-gray-100 hover:text-gray-500 focus:outline-hidden focus:ring-2 focus:ring-inset focus:ring-indigo-500",
     :aria-controls "mobile-menu",
     :aria-expanded "false"}
    [:span {:class "absolute -inset-0.5"}]
    [:span {:class "sr-only"} "Open main menu"]
    (comment
      "Icon when menu is closed.\n\n            Menu open: \"hidden\", Menu closed: \"block\"")
    [:svg
     {:class "block h-6 w-6",
      :fill "none",
      :viewBox "0 0 24 24",
      :stroke-width "1.5",
      :stroke "currentColor",
      :aria-hidden "true",
      :data-slot "icon"}
     [:path
      {:stroke-linecap "round",
       :stroke-linejoin "round",
       :d "M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"}]]
    (comment
      "Icon when menu is open.\n\n            Menu open: \"block\", Menu closed: \"hidden\"")
    [:svg
     {:class "hidden h-6 w-6",
      :fill "none",
      :viewBox "0 0 24 24",
      :stroke-width "1.5",
      :stroke "currentColor",
      :aria-hidden "true",
      :data-slot "icon"}
     [:path
      {:stroke-linecap "round",
       :stroke-linejoin "round",
       :d "M6 18 18 6M6 6l12 12"}]]]])


;; Currently based off of Tailwind-ui template
;; Should update later when implementing mobile display
(defn mobile-menu []
  (comment "Mobile menu, show/hide based on menu state.")
  [:div
   {:class "sm:hidden", :id "mobile-menu"}
   [:div
    {:class "space-y-1 pb-3 pt-2"}
    (comment
      "Current: \"bg-indigo-50 border-indigo-500 text-indigo-700\", Default: \"border-transparent text-gray-500 hover:bg-gray-50 hover:border-gray-300 hover:text-gray-700\"")
    [:a
     {:href "#",
      :class
      "block border-l-4 border-indigo-500 bg-indigo-50 py-2 pl-3 pr-4 text-base font-medium text-indigo-700"}
     "Dashboard"]
    [:a
     {:href "#",
      :class
      "block border-l-4 border-transparent py-2 pl-3 pr-4 text-base font-medium text-gray-500 hover:border-gray-300 hover:bg-gray-50 hover:text-gray-700"}
     "Team"]
    [:a
     {:href "#",
      :class
      "block border-l-4 border-transparent py-2 pl-3 pr-4 text-base font-medium text-gray-500 hover:border-gray-300 hover:bg-gray-50 hover:text-gray-700"}
     "Projects"]
    [:a
     {:href "#",
      :class
      "block border-l-4 border-transparent py-2 pl-3 pr-4 text-base font-medium text-gray-500 hover:border-gray-300 hover:bg-gray-50 hover:text-gray-700"}
     "Calendar"]]
   [:div
    {:class "border-t border-gray-200 pb-3 pt-4"}
    [:div
     {:class "flex items-center px-4"}
     [:div
      {:class "shrink-0"}
      [:img
       {:class "h-10 w-10 rounded-full",
        :src
        "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80",
        :alt ""}]]
     [:div
      {:class "ml-3"}
      [:div {:class "text-base font-medium text-gray-800"} "Tom Cook"]
      [:div
       {:class "text-sm font-medium text-gray-500"}
       "tom@example.com"]]
     [:button
      {:type "button",
       :class
       "relative ml-auto shrink-0 rounded-full bg-white p-1 text-gray-400 hover:text-gray-500 focus:outline-hidden focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2"}
      [:span {:class "absolute -inset-1.5"}]
      [:span {:class "sr-only"} "View notifications"]
      [:svg
       {:class "h-6 w-6",
        :fill "none",
        :viewBox "0 0 24 24",
        :stroke-width "1.5",
        :stroke "currentColor",
        :aria-hidden "true",
        :data-slot "icon"}
       [:path
        {:stroke-linecap "round",
         :stroke-linejoin "round",
         :d
         "M14.857 17.082a23.848 23.848 0 0 0 5.454-1.31A8.967 8.967 0 0 1 18 9.75V9A6 6 0 0 0 6 9v.75a8.967 8.967 0 0 1-2.312 6.022c1.733.64 3.56 1.085 5.455 1.31m5.714 0a24.255 24.255 0 0 1-5.714 0m5.714 0a3 3 0 1 1-5.714 0"}]]]]
    [:div
     {:class "mt-3 space-y-1"}
     [:a
      {:href "#",
       :class
       "block px-4 py-2 text-base font-medium text-gray-500 hover:bg-gray-100 hover:text-gray-800"}
      "Your Profile"]
     [:a
      {:href "#",
       :class
       "block px-4 py-2 text-base font-medium text-gray-500 hover:bg-gray-100 hover:text-gray-800"}
      "Settings"]
     [:a
      {:href "#",
       :class
       "block px-4 py-2 text-base font-medium text-gray-500 hover:bg-gray-100 hover:text-gray-800"}
      "Sign out"]]]])

(defn query-display []
  (let [current-query @(re-frame/subscribe [::current-query])]
    [:div
     {:class "flex h-16 justify-between"}
     [:div
      {:class "flex"}
      [:div
       {:class "flex shrink-0 items-center"}
       [:img
        {:class "h-8 w-auto",
         :src "/img/clingen-logo.svg"
         :alt "ClinGen"}]]
      [:div
       {:class "hidden sm:ml-6 sm:flex sm:space-x-8"
        :on-click #(re-frame/dispatch [::set-mode :select-query])}
       (query-label current-query)
       #_(nav-link :routes/home "Conflicts" current-route)
       #_(nav-link :routes/annotations "Annotations" current-route)]]
     (user)
     (mobile-menu-button)]))

(defn nav []
  (let [current-query @(re-frame/subscribe [::current-query])
        current-mode @(re-frame/subscribe [::current-mode])
        current-route @(re-frame/subscribe
                        [:genegraph.frontend.routes/current-route])
        show-user-menu @(re-frame/subscribe [::show-user-menu])]
    [:div
     [:nav
      {:class "bg-white shadow-sm flex"}
      [:div
       {:class "mx-auto max-w-7xl px-4 sm:px-6 lg:px-8 flex jusftify-"}
       (query-display)]
      [:div
       {:class "flex justify-end"}
       ]
      (mobile-menu)]
     (if (or (not current-query)
             (= :select-query current-mode))
       (query-select)
       [:div])]))

(comment
  [:header
   {:class "bg-white"}
   [:nav
    {:class
     "mx-auto flex max-w-7xl items-center justify-between p-6 lg:px-8",
     :aria-label "Global"}
    [:div
     {:class "flex flex-1"}
     [:div
      {:class "hidden lg:flex lg:gap-x-12"}
      [:a
       {:href "#", :class "text-sm/6 font-semibold text-gray-900"}
       "Product"]
      [:a
       {:href "#", :class "text-sm/6 font-semibold text-gray-900"}
       "Features"]
      [:a
       {:href "#", :class "text-sm/6 font-semibold text-gray-900"}
       "Company"]]
     [:div
      {:class "flex lg:hidden"}
      [:button
       {:type "button",
        :class
        "-m-2.5 inline-flex items-center justify-center rounded-md p-2.5 text-gray-700"}
       [:span {:class "sr-only"} "Open main menu"]
       [:svg
        {:class "size-6",
         :fill "none",
         :viewBox "0 0 24 24",
         :stroke-width "1.5",
         :stroke "currentColor",
         :aria-hidden "true",
         :data-slot "icon"}
        [:path
         {:stroke-linecap "round",
          :stroke-linejoin "round",
          :d "M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"}]]]]]
    [:a
     {:href "#", :class "-m-1.5 p-1.5"}
     [:span {:class "sr-only"} "Your Company"]
     [:img
      {:class "h-8 w-auto",
       :src
       "https://tailwindcss.com/plus-assets/img/logos/mark.svg?color=indigo&shade=600",
       :alt ""}]]
    [:div
     {:class "flex flex-1 justify-end"}
     [:a
      {:href "#", :class "text-sm/6 font-semibold text-gray-900"}
      "Log in"
      [:span {:aria-hidden "true"} "→"]]]]
   (comment "Mobile menu, show/hide based on menu open state.")
   [:div
    {:class "lg:hidden", :role "dialog", :aria-modal "true"}
    (comment "Background backdrop, show/hide based on slide-over state.")
    [:div {:class "fixed inset-0 z-10"}]
    [:div
     {:class
      "fixed inset-y-0 left-0 z-10 w-full overflow-y-auto bg-white px-6 py-6"}
     [:div
      {:class "flex items-center justify-between"}
      [:div
       {:class "flex flex-1"}
       [:button
        {:type "button", :class "-m-2.5 rounded-md p-2.5 text-gray-700"}
        [:span {:class "sr-only"} "Close menu"]
        [:svg
         {:class "size-6",
          :fill "none",
          :viewBox "0 0 24 24",
          :stroke-width "1.5",
          :stroke "currentColor",
          :aria-hidden "true",
          :data-slot "icon"}
         [:path
          {:stroke-linecap "round",
           :stroke-linejoin "round",
           :d "M6 18 18 6M6 6l12 12"}]]]]
      [:a
       {:href "#", :class "-m-1.5 p-1.5"}
       [:span {:class "sr-only"} "Your Company"]
       [:img
        {:class "h-8 w-auto",
         :src
         "https://tailwindcss.com/plus-assets/img/logos/mark.svg?color=indigo&shade=600",
         :alt ""}]]
      [:div
       {:class "flex flex-1 justify-end"}
       [:a
        {:href "#", :class "text-sm/6 font-semibold text-gray-900"}
        "Log in"
        [:span {:aria-hidden "true"} "→"]]]]
     [:div
      {:class "mt-6 space-y-2"}
      [:a
       {:href "#",
        :class
        "-mx-3 block rounded-lg px-3 py-2 text-base/7 font-semibold text-gray-900 hover:bg-gray-50"}
       "Product"]
      [:a
       {:href "#",
        :class
        "-mx-3 block rounded-lg px-3 py-2 text-base/7 font-semibold text-gray-900 hover:bg-gray-50"}
       "Features"]
      [:pa
       {:href "#",
        :class
        "-mx-3 block rounded-lg px-3 py-2 text-base/7 font-semibold text-gray-900 hover:bg-gray-50"}
       "Company"]]]]])


