(ns genegraph.frontend.nav
  (:require [re-frame.core :as re-frame]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.user :as user]))


(def queries
  [{:label "All Gene Validity Assertions"}
   {:label "All Variant Pathogenicity Assertions"}])

(def filters
  {:proposition_type {:label "Proposition Type"
                      :description "Use this to choose between Gene Validity, Variant Pathogenicity, or Condition Mechansim."
                      :argument-list [{:label "Gene Validity"
                                       :value "CG:GeneValidityProposition"}
                                      {:label "Variant Pathogenicity"
                                       :value "CG:VariantPathogenicityProposition"}]}
   :resource_type {:label "Resource Type"
                   :description "Evidence Strength Assertion, Variant, Gene, Disease,or Organization."
                   :argument-list [{:label "Evidence Strength Assertion"
                                    :value "CG:EvidenceStrengthAssertion"}]}})

(re-frame/reg-sub
 ::current-query
 :-> ::current-query)

(re-frame/reg-sub
 ::current-mode
 :-> :current-mode)

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

(defn query-display []
  )

(defn query-select []
  [:ul
 {:role "list", :class "divide-y divide-gray-100"}
 [:li
  {:class "relative flex justify-between py-5"}
  [:div
   {:class "flex gap-x-4 pr-6 sm:w-1/2 sm:flex-none"}
   [:img
    {:class "size-12 flex-none rounded-full bg-gray-50",
     :src
     "https://images.unsplash.com/photo-1494790108377-be9c29b29330?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80",
     :alt ""}]
   [:div
    {:class "min-w-0 flex-auto"}
    [:p
     {:class "text-sm/6 font-semibold text-gray-900"}
     [:a
      {:href "#"}
      [:span {:class "absolute inset-x-0 -top-px bottom-0"}]
      "Leslie Alexander"]]
    [:p
     {:class "mt-1 flex text-xs/5 text-gray-500"}
     [:a
      {:href "mailto:leslie.alexander@example.com",
       :class "relative truncate hover:underline"}
      "leslie.alexander@example.com"]]]]
  [:div
   {:class
    "flex items-center justify-between gap-x-4 sm:w-1/2 sm:flex-none"}
   [:div
    {:class "hidden sm:block"}
    [:p {:class "text-sm/6 text-gray-900"} "Co-Founder / CEO"]
    [:p
     {:class "mt-1 text-xs/5 text-gray-500"}
     "Last seen"
     [:time {:datetime "2023-01-23T13:23Z"} "3h ago"]]]
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
      :clip-rule "evenodd"}]]]]
 [:li
  {:class "relative flex justify-between py-5"}
  [:div
   {:class "flex gap-x-4 pr-6 sm:w-1/2 sm:flex-none"}
   [:img
    {:class "size-12 flex-none rounded-full bg-gray-50",
     :src
     "https://images.unsplash.com/photo-1519244703995-f4e0f30006d5?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80",
     :alt ""}]
   [:div
    {:class "min-w-0 flex-auto"}
    [:p
     {:class "text-sm/6 font-semibold text-gray-900"}
     [:a
      {:href "#"}
      [:span {:class "absolute inset-x-0 -top-px bottom-0"}]
      "Michael Foster"]]
    [:p
     {:class "mt-1 flex text-xs/5 text-gray-500"}
     [:a
      {:href "mailto:michael.foster@example.com",
       :class "relative truncate hover:underline"}
      "michael.foster@example.com"]]]]
  [:div
   {:class
    "flex items-center justify-between gap-x-4 sm:w-1/2 sm:flex-none"}
   [:div
    {:class "hidden sm:block"}
    [:p {:class "text-sm/6 text-gray-900"} "Co-Founder / CTO"]
    [:p
     {:class "mt-1 text-xs/5 text-gray-500"}
     "Last seen"
     [:time {:datetime "2023-01-23T13:23Z"} "3h ago"]]]
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
      :clip-rule "evenodd"}]]]]
 [:li
  {:class "relative flex justify-between py-5"}
  [:div
   {:class "flex gap-x-4 pr-6 sm:w-1/2 sm:flex-none"}
   [:img
    {:class "size-12 flex-none rounded-full bg-gray-50",
     :src
     "https://images.unsplash.com/photo-1506794778202-cad84cf45f1d?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80",
     :alt ""}]
   [:div
    {:class "min-w-0 flex-auto"}
    [:p
     {:class "text-sm/6 font-semibold text-gray-900"}
     [:a
      {:href "#"}
      [:span {:class "absolute inset-x-0 -top-px bottom-0"}]
      "Dries Vincent"]]
    [:p
     {:class "mt-1 flex text-xs/5 text-gray-500"}
     [:a
      {:href "mailto:dries.vincent@example.com",
       :class "relative truncate hover:underline"}
      "dries.vincent@example.com"]]]]
  [:div
   {:class
    "flex items-center justify-between gap-x-4 sm:w-1/2 sm:flex-none"}
   [:div
    {:class "hidden sm:block"}
    [:p {:class "text-sm/6 text-gray-900"} "Business Relations"]
    [:div
     {:class "mt-1 flex items-center gap-x-1.5"}
     [:div
      {:class "flex-none rounded-full bg-emerald-500/20 p-1"}
      [:div {:class "size-1.5 rounded-full bg-emerald-500"}]]
     [:p {:class "text-xs/5 text-gray-500"} "Online"]]]
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
      :clip-rule "evenodd"}]]]]
 [:li
  {:class "relative flex justify-between py-5"}
  [:div
   {:class "flex gap-x-4 pr-6 sm:w-1/2 sm:flex-none"}
   [:img
    {:class "size-12 flex-none rounded-full bg-gray-50",
     :src
     "https://images.unsplash.com/photo-1517841905240-472988babdf9?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80",
     :alt ""}]
   [:div
    {:class "min-w-0 flex-auto"}
    [:p
     {:class "text-sm/6 font-semibold text-gray-900"}
     [:a
      {:href "#"}
      [:span {:class "absolute inset-x-0 -top-px bottom-0"}]
      "Lindsay Walton"]]
    [:p
     {:class "mt-1 flex text-xs/5 text-gray-500"}
     [:a
      {:href "mailto:lindsay.walton@example.com",
       :class "relative truncate hover:underline"}
      "lindsay.walton@example.com"]]]]
  [:div
   {:class
    "flex items-center justify-between gap-x-4 sm:w-1/2 sm:flex-none"}
   [:div
    {:class "hidden sm:block"}
    [:p {:class "text-sm/6 text-gray-900"} "Front-end Developer"]
    [:p
     {:class "mt-1 text-xs/5 text-gray-500"}
     "Last seen"
     [:time {:datetime "2023-01-23T13:23Z"} "3h ago"]]]
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
      :clip-rule "evenodd"}]]]]
 [:li
  {:class "relative flex justify-between py-5"}
  [:div
   {:class "flex gap-x-4 pr-6 sm:w-1/2 sm:flex-none"}
   [:img
    {:class "size-12 flex-none rounded-full bg-gray-50",
     :src
     "https://images.unsplash.com/photo-1438761681033-6461ffad8d80?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80",
     :alt ""}]
   [:div
    {:class "min-w-0 flex-auto"}
    [:p
     {:class "text-sm/6 font-semibold text-gray-900"}
     [:a
      {:href "#"}
      [:span {:class "absolute inset-x-0 -top-px bottom-0"}]
      "Courtney Henry"]]
    [:p
     {:class "mt-1 flex text-xs/5 text-gray-500"}
     [:a
      {:href "mailto:courtney.henry@example.com",
       :class "relative truncate hover:underline"}
      "courtney.henry@example.com"]]]]
  [:div
   {:class
    "flex items-center justify-between gap-x-4 sm:w-1/2 sm:flex-none"}
   [:div
    {:class "hidden sm:block"}
    [:p {:class "text-sm/6 text-gray-900"} "Designer"]
    [:p
     {:class "mt-1 text-xs/5 text-gray-500"}
     "Last seen"
     [:time {:datetime "2023-01-23T13:23Z"} "3h ago"]]]
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
      :clip-rule "evenodd"}]]]]
 [:li
  {:class "relative flex justify-between py-5"}
  [:div
   {:class "flex gap-x-4 pr-6 sm:w-1/2 sm:flex-none"}
   [:img
    {:class "size-12 flex-none rounded-full bg-gray-50",
     :src
     "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80",
     :alt ""}]
   [:div
    {:class "min-w-0 flex-auto"}
    [:p
     {:class "text-sm/6 font-semibold text-gray-900"}
     [:a
      {:href "#"}
      [:span {:class "absolute inset-x-0 -top-px bottom-0"}]
      "Tom Cook"]]
    [:p
     {:class "mt-1 flex text-xs/5 text-gray-500"}
     [:a
      {:href "mailto:tom.cook@example.com",
       :class "relative truncate hover:underline"}
      "tom.cook@example.com"]]]]
  [:div
   {:class
    "flex items-center justify-between gap-x-4 sm:w-1/2 sm:flex-none"}
   [:div
    {:class "hidden sm:block"}
    [:p {:class "text-sm/6 text-gray-900"} "Director of Product"]
    [:div
     {:class "mt-1 flex items-center gap-x-1.5"}
     [:div
      {:class "flex-none rounded-full bg-emerald-500/20 p-1"}
      [:div {:class "size-1.5 rounded-full bg-emerald-500"}]]
     [:p {:class "text-xs/5 text-gray-500"} "Online"]]]
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
      :clip-rule "evenodd"}]]]]])

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
    (comment
      "Dropdown menu, show/hide based on menu state.\n\n            Entering: \"transition ease-out duration-200\"\n              From: \"transform opacity-0 scale-95\"\n              To: \"transform opacity-100 scale-100\"\n            Leaving: \"transition ease-in duration-75\"\n              From: \"transform opacity-100 scale-100\"\n              To: \"transform opacity-0 scale-95\"")
    #_[:div
       {:class
        "absolute right-0 z-10 mt-2 w-48 origin-top-right rounded-md bg-white py-1 shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-hidden",
        :role "menu",
        :aria-orientation "vertical",
        :aria-labelledby "user-menu-button",
        :tabIndex "-1"}
       (comment "Active: \"bg-gray-100\", Not Active: \"\"")
       [:a
        {:href "#",
         :class "block px-4 py-2 text-sm text-gray-700",
         :role "menuitem",
         :tabIndex "-1",
         :id "user-menu-item-0"}
        "Your Profile"]
       [:a
        {:href "#",
         :class "block px-4 py-2 text-sm text-gray-700",
         :role "menuitem",
         :tabIndex "-1",
         :id "user-menu-item-1"}
        "Settings"]
       [:a
        {:href "#",
         :class "block px-4 py-2 text-sm text-gray-700",
         :role "menuitem",
         :tabIndex "-1",
         :id "user-menu-item-2"}
        "Sign out"]]]])

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

(defn nav []
  (let [current-query @(re-frame/subscribe [::current-query])
        current-mode @(re-frame/subscribe [::current-mode])
        current-route @(re-frame/subscribe
                        [:genegraph.frontend.routes/current-route])]
    [:nav
     {:class "bg-white shadow-sm"}
     [:div
      {:class "mx-auto max-w-7xl px-4 sm:px-6 lg:px-8"}
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
         {:class "hidden sm:ml-6 sm:flex sm:space-x-8"}
         (nav-link :routes/home "Conflicts" current-route)
         (nav-link :routes/annotations "Annotations" current-route)]]
       (user)
       (mobile-menu-button)]]
     (mobile-menu)]))


