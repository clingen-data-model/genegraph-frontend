(ns genegraph.frontend.shell
  (:require [re-frame.core :as rf]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.icon :as icon]
            [genegraph.frontend.common :as common]
            [genegraph.frontend.queries :as queries]
            [genegraph.frontend.display.features :as features]
            [genegraph.frontend.components.search :as search]
            [clojure.string :as s]
            [genegraph.frontend.view.sequence-feature]
            [genegraph.frontend.view.assertion]
            [genegraph.frontend.auth :as auth]))




(rf/reg-sub
 ::main
 :-> :main)

(rf/reg-sub
 ::secondary-view
 :-> :secondary-view)

(rf/reg-sub
 ::search-active?
 (fn [db _]
   (< 0 (count (::search/search-input db)))))

;; Part of pre-existing template
;; Not currently designed or used
(comment
  "Off-canvas menu for mobile, show/hide based on off-canvas menu state.")
#_(defn mobile-menu []
  [:div
   {:class "relative z-50 lg:hidden",
    :role "dialog",
    :aria-modal "true"}
   (comment
     "Off-canvas menu backdrop, show/hide based on off-canvas menu state.\n\n      Entering: \"transition-opacity ease-linear duration-300\"\n        From: \"opacity-0\"\n        To: \"opacity-100\"\n      Leaving: \"transition-opacity ease-linear duration-300\"\n        From: \"opacity-100\"\n        To: \"opacity-0\"")
   [:div {:class "fixed inset-0 bg-gray-900/80", :aria-hidden "true"}]
   [:div
    {:class "fixed inset-0 flex"}
    (comment
      "Off-canvas menu, show/hide based on off-canvas menu state.\n\n        Entering: \"transition ease-in-out duration-300 transform\"\n          From: \"-translate-x-full\"\n          To: \"translate-x-0\"\n        Leaving: \"transition ease-in-out duration-300 transform\"\n          From: \"translate-x-0\"\n          To: \"-translate-x-full\"")
    [:div
     {:class "relative mr-16 flex w-full max-w-xs flex-1"}
     (comment
       "Close button, show/hide based on off-canvas menu state.\n\n          Entering: \"ease-in-out duration-300\"\n            From: \"opacity-0\"\n            To: \"opacity-100\"\n          Leaving: \"ease-in-out duration-300\"\n            From: \"opacity-100\"\n            To: \"opacity-0\"")
     [:div
      {:class "absolute top-0 left-full flex w-16 justify-center pt-5"}
      [:button
       {:type "button", :class "-m-2.5 p-2.5"}
       [:span {:class "sr-only"} "Close sidebar"]
       [:svg
        {:class "size-6 text-white",
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
     [:div
      {:class
       "flex grow flex-col gap-y-5 overflow-y-auto bg-gray-900 px-6 pb-2 ring-1 ring-white/10"}
      [:div
       {:class "flex h-16 shrink-0 items-center"}
       [:img
        {:class "h-8 w-auto",
         :src
         "https://tailwindcss.com/plus-assets/img/logos/mark.svg?color=indigo&shade=500",
         :alt "Your Company"}]]
      [:nav
       {:class "flex flex-1 flex-col"}
       [:ul
        {:role "list", :class "-mx-2 flex-1 space-y-1"}
        [:li
         (comment
           "Current: \"bg-gray-800 text-white\", Default: \"text-gray-400 hover:text-white hover:bg-gray-800\"")
         [:a
          {:href "#",
           :class
           "group flex gap-x-3 rounded-md bg-gray-800 p-2 text-sm/6 font-semibold text-white"}
          [:svg
           {:class "size-6 shrink-0",
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
             "m2.25 12 8.954-8.955c.44-.439 1.152-.439 1.591 0L21.75 12M4.5 9.75v10.125c0 .621.504 1.125 1.125 1.125H9.75v-4.875c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125V21h4.125c.621 0 1.125-.504 1.125-1.125V9.75M8.25 21h8.25"}]]
          "Dashboard"]]
        [:li
         [:a
          {:href "#",
           :class
           "group flex gap-x-3 rounded-md p-2 text-sm/6 font-semibold text-gray-400 hover:bg-gray-800 hover:text-white"}
          [:svg
           {:class "size-6 shrink-0",
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
             "M15 19.128a9.38 9.38 0 0 0 2.625.372 9.337 9.337 0 0 0 4.121-.952 4.125 4.125 0 0 0-7.533-2.493M15 19.128v-.003c0-1.113-.285-2.16-.786-3.07M15 19.128v.106A12.318 12.318 0 0 1 8.624 21c-2.331 0-4.512-.645-6.374-1.766l-.001-.109a6.375 6.375 0 0 1 11.964-3.07M12 6.375a3.375 3.375 0 1 1-6.75 0 3.375 3.375 0 0 1 6.75 0Zm8.25 2.25a2.625 2.625 0 1 1-5.25 0 2.625 2.625 0 0 1 5.25 0Z"}]]
          "Team"]]
        [:li
         [:a
          {:href "#",
           :class
           "group flex gap-x-3 rounded-md p-2 text-sm/6 font-semibold text-gray-400 hover:bg-gray-800 hover:text-white"}
          [:svg
           {:class "size-6 shrink-0",
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
             "M2.25 12.75V12A2.25 2.25 0 0 1 4.5 9.75h15A2.25 2.25 0 0 1 21.75 12v.75m-8.69-6.44-2.12-2.12a1.5 1.5 0 0 0-1.061-.44H4.5A2.25 2.25 0 0 0 2.25 6v12a2.25 2.25 0 0 0 2.25 2.25h15A2.25 2.25 0 0 0 21.75 18V9a2.25 2.25 0 0 0-2.25-2.25h-5.379a1.5 1.5 0 0 1-1.06-.44Z"}]]
          "Projects"]]
        [:li
         [:a
          {:href "#",
           :class
           "group flex gap-x-3 rounded-md p-2 text-sm/6 font-semibold text-gray-400 hover:bg-gray-800 hover:text-white"}
          [:svg
           {:class "size-6 shrink-0",
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
             "M6.75 3v2.25M17.25 3v2.25M3 18.75V7.5a2.25 2.25 0 0 1 2.25-2.25h13.5A2.25 2.25 0 0 1 21 7.5v11.25m-18 0A2.25 2.25 0 0 0 5.25 21h13.5A2.25 2.25 0 0 0 21 18.75m-18 0v-7.5A2.25 2.25 0 0 1 5.25 9h13.5A2.25 2.25 0 0 1 21 11.25v7.5"}]]
          "Calendar"]]
        [:li
         [:a
          {:href "#",
           :class
           "group flex gap-x-3 rounded-md p-2 text-sm/6 font-semibold text-gray-400 hover:bg-gray-800 hover:text-white"}
          [:svg
           {:class "size-6 shrink-0",
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
             "M15.75 17.25v3.375c0 .621-.504 1.125-1.125 1.125h-9.75a1.125 1.125 0 0 1-1.125-1.125V7.875c0-.621.504-1.125 1.125-1.125H6.75a9.06 9.06 0 0 1 1.5.124m7.5 10.376h3.375c.621 0 1.125-.504 1.125-1.125V11.25c0-4.46-3.243-8.161-7.5-8.876a9.06 9.06 0 0 0-1.5-.124H9.375c-.621 0-1.125.504-1.125 1.125v3.5m7.5 10.375H9.375a1.125 1.125 0 0 1-1.125-1.125v-9.25m12 6.625v-1.875a3.375 3.375 0 0 0-3.375-3.375h-1.5a1.125 1.125 0 0 1-1.125-1.125v-1.5a3.375 3.375 0 0 0-3.375-3.375H9.75"}]]
          "Documents"]]
        [:li
         [:a
          {:href "#",
           :class
           "group flex gap-x-3 rounded-md p-2 text-sm/6 font-semibold text-gray-400 hover:bg-gray-800 hover:text-white"}
          [:svg
           {:class "size-6 shrink-0",
            :fill "none",
            :viewBox "0 0 24 24",
            :stroke-width "1.5",
            :stroke "currentColor",
            :aria-hidden "true",
            :data-slot "icon"}
           [:path
            {:stroke-linecap "round",
             :stroke-linejoin "round",
             :d "M10.5 6a7.5 7.5 0 1 0 7.5 7.5h-7.5V6Z"}]
           [:path
            {:stroke-linecap "round",
             :stroke-linejoin "round",
             :d "M13.5 10.5H21A7.5 7.5 0 0 0 13.5 3v7.5Z"}]]
          "Reports"]]]]]]]])

(comment "Static sidebar for desktop")
(def sections
  [{:route :routes/home
    :name "Home"
    :icon icon/home
    :active-on #{:routes/home}}
   {:route :routes/downloads
    :name "Downloads"
    :icon icon/arrow-down-tray
    :active-on #{:routes/downloads}}
   {:route :routes/documentation
    :name "Documentation"
    :icon icon/book-open
    :active-on #{:routes/documentation :routes/documentation-term}}
   {:route :routes/filter
    :name "Filter"
    :icon icon/filter-icon
    :requires-authentication true
    :active-on #{:routes/filter}}])

    (comment
        "Current: \"bg-gray-800 text-white\", Default: \"text-gray-400 hover:text-white hover:bg-gray-800\"")

(defn sidebar-section [section current-route]
  ^{:key {:place :sidebar :section section}}
  [:li
   [:a
    {:href (rfe/href (:route section))
     :class
     (if (get (:active-on section) (get-in current-route [:data :name]))
       "group flex gap-x-3 rounded-md bg-sky-600 p-3 text-sm/6 font-semibold text-sky-100"
       "group flex gap-x-3 rounded-md p-3 text-sm/6 font-semibold text-sky-400 hover:bg-sky-700 hover:text-sky-100")}
    (:icon section)
    [:span {:class "sr-only"} (:name section)]]])

(defn current-sections []
  (if @(rf/subscribe [::auth/current-user])
    sections
    (remove :requires-authentication sections)))

(defn sidebar []
  (let [current-route @(rf/subscribe [:genegraph.frontend.routes/current-route])]
    [:div
     {:class
      "hidden lg:fixed lg:inset-y-0 lg:left-0 lg:z-50 lg:block lg:w-20 lg:overflow-y-auto lg:bg-sky-900 lg:pb-4"}
     [:div
      {:class "flex h-16 shrink-0 items-center justify-center"}
      [:a
       {:href "https://clinicalgenome.org"}
       [:img
        {:class "h-8 w-auto",
         :src "img/clingen-logo-white.svg"
         :alt "Genegraph"}]]]
     [:nav
      {:class "mt-8"}
      [:ul
       {:role "list", :class "flex flex-col items-center space-y-1"}
       (for [s (current-sections)]
         (sidebar-section s current-route))]]]))

(defn search-form []
  [:form
   {:class "grid flex-1 grid-cols-1 my-1"
    :on-submit (fn [e]
                 (.preventDefault e)
                 (rf/dispatch
                  [::search/text-search]))}
   
   [:input
    {:type "search",
     :name "search",
     :aria-label "Search",
     :class "col-start-1 row-start-1 block size-full bg-white pl-8 text-base text-gray-900 outline-hidden placeholder:text-gray-400 sm:text-sm/6 border-none focus:ring-1 rounded-full",
     :placeholder "Search"
     :on-change #(rf/dispatch [::search/update-search-input (-> % .-target .-value)])
     :on-key-down (fn [e]
                    (case (.-key e)
                      "Enter" (rf/dispatch
                               [::search/text-search])
                      nil))}]
   [:svg
    {:class
     "pointer-events-none col-start-1 row-start-1 size-5 self-center text-gray-400",
     :viewBox "0 0 20 20",
     :fill "currentColor",
     :aria-hidden "true",
     :data-slot "icon"}
    [:path
     {:fill-rule "evenodd",
      :d
      "M9 3.5a5.5 5.5 0 1 0 0 11 5.5 5.5 0 0 0 0-11ZM2 9a7 7 0 1 1 12.452 4.391l3.328 3.329a.75.75 0 1 1-1.06 1.06l-3.329-3.328A7 7 0 0 1 2 9Z",
      :clip-rule "evenodd"}]]])

#_(defn search-form []
  [:div
   {:class "grid flex-1 grid-cols-1"}
   [:div
    {:class "col-start-1 row-start-1 block size-full bg-white pl-8 text-base text-gray-900 outline-hidden placeholder:text-gray-400 sm:text-sm/6"}
    ]])

(defn notifications []
  [:button
   {:type "button",
    :class "-m-2.5 p-2.5 text-gray-400 hover:text-gray-500"}
   [:span {:class "sr-only"} "View notifications"]
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
      :d
      "M14.857 17.082a23.848 23.848 0 0 0 5.454-1.31A8.967 8.967 0 0 1 18 9.75V9A6 6 0 0 0 6 9v.75a8.967 8.967 0 0 1-2.312 6.022c1.733.64 3.56 1.085 5.455 1.31m5.714 0a24.255 24.255 0 0 1-5.714 0m5.714 0a3 3 0 1 1-5.714 0"}]]])

(defn profile-dropdown []
  [:div
   {:class "relative"}
   [:button
    {:type "button",
     :class "-m-1.5 flex items-center p-1.5",
     :id "user-menu-button",
     :aria-expanded "false",
     :aria-haspopup "true"}
    [:span {:class "sr-only"} "Open user menu"]
    [:img
     {:class "size-8 rounded-full bg-gray-50",
      :src
      "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80",
      :alt ""}]
    [:span
     {:class "hidden lg:flex lg:items-center"}
     [:span
      {:class "ml-4 text-sm/6 font-semibold text-gray-900",
       :aria-hidden "true"}
      "Tom Cook"]
     [:svg
      {:class "ml-2 size-5 text-gray-400",
       :viewBox "0 0 20 20",
       :fill "currentColor",
       :aria-hidden "true",
       :data-slot "icon"}
      [:path
       {:fill-rule "evenodd",
        :d
        "M5.22 8.22a.75.75 0 0 1 1.06 0L10 11.94l3.72-3.72a.75.75 0 1 1 1.06 1.06l-4.25 4.25a.75.75 0 0 1-1.06 0L5.22 9.28a.75.75 0 0 1 0-1.06Z",
        :clip-rule "evenodd"}]]]]
   (comment
     "Dropdown menu, show/hide based on menu state.\n\n              Entering: \"transition ease-out duration-100\"\n                From: \"transform opacity-0 scale-95\"\n                To: \"transform opacity-100 scale-100\"\n              Leaving: \"transition ease-in duration-75\"\n                From: \"transform opacity-100 scale-100\"\n                To: \"transform opacity-0 scale-95\"")
   [:div
    {:class
     "absolute right-0 z-10 mt-2.5 w-32 origin-top-right rounded-md bg-white py-2 shadow-lg ring-1 ring-gray-900/5 focus:outline-hidden",
     :role "menu",
     :aria-orientation "vertical",
     :aria-labelledby "user-menu-button",
     :tabIndex "-1"}
    (comment
      "Active: \"bg-gray-50 outline-hidden\", Not Active: \"\"")
    [:a
     {:href "#",
      :class "block px-3 py-1 text-sm/6 text-gray-900",
      :role "menuitem",
      :tabIndex "-1",
      :id "user-menu-item-0"}
     "Your profile"]
    [:a
     {:href "#",
      :class "block px-3 py-1 text-sm/6 text-gray-900",
      :role "menuitem",
      :tabIndex "-1",
      :id "user-menu-item-1"}
     "Sign out"]]])

(defn open-sidebar-button []
  [:button
   {:type "button", :class "-m-2.5 p-2.5 text-gray-700 lg:hidden"}
   [:span {:class "sr-only"} "Open sidebar"]
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
      :d "M3.75 6.75h16.5M3.75 12h16.5m-16.5 5.25h16.5"}]]])

#_(defn main []
  (let [current @(rf/subscribe [::main])]
    [:main
     ;; Main area
     {:class "xl:pl-96"}
     [:div
      {:class "px-4 py-10 sm:px-6 lg:px-8 lg:py-6"}
      (if current
        (common/main-view current)
        [:div])
      #_[:pre (with-out-str (cljs.pprint/pprint current))]]]))

(defn main []
  (let [current-route @(rf/subscribe [:genegraph.frontend.routes/current-route])
        secondary-view-item @(rf/subscribe [::secondary-view])]
    [:main
     ;; Main area
     {:class (if secondary-view-item "xl:pl-96" "")}
     (when current-route
       [(-> current-route :data :view)])]))

(defn secondary []
  (if-let [secondary-view-item @(rf/subscribe [::secondary-view])]
    [:aside
     ;; Secondary column (hidden on smaller screens)
     {:class
      "fixed top-16 bottom-0 left-20 hidden w-96 overflow-y-auto border-r border-gray-200 px-4 py-6 sm:px-6 lg:px-8 xl:block"}
     (common/secondary-view secondary-view-item)]
    [:div]))

(defn profile []
  [:div
   {:class "flex items-center gap-x-4 lg:gap-x-6"}
   (notifications)
   (comment "Separator")
   [:div
    {:class "hidden lg:block lg:h-6 lg:w-px lg:bg-gray-900/10",
     :aria-hidden "true"}]
   (comment "Profile dropdown")
   (profile-dropdown)])

(defn shell []
  (let [search-active @(rf/subscribe [::search-active?])
        current-user @(rf/subscribe [::auth/current-user])]
    [:div
     #_(mobile-menu)
     (sidebar)
     [:div
      {:class "lg:pl-20"}
      [:div
       {:class
        "sticky top-0 z-40 flex h-16 shrink-0 items-center gap-x-4  px-4 shadow-xs sm:gap-x-6 sm:px-6 lg:px-8 bg-white"}
       #_(open-sidebar-button)
       (comment "Separator")
       [:div
        {:class "h-6 w-px bg-gray-900/10 lg:hidden", :aria-hidden "true"}]
       [:div
        {:class "flex flex-1 gap-x-4 self-stretch lg:gap-x-6 bg-white"}
        (if current-user
          (search-form)
          [:div])
        #_(profile)]
       (auth/login-header-div)]
      (if search-active
        (search/search-result-div)
        (main))      
      #_[:pre (with-out-str (cljs.pprint/pprint queries/compiled-base-query))]]
     (secondary)]))
