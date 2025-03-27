(ns genegraph.frontend.page.gencc-list
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.display :as display]
            [genegraph.frontend.user :as user]
            [genegraph.frontend.icon :as icon]
            [genegraph.frontend.display.annotation :as annotation]
            [genegraph.frontend.queries.assertion :as assertion]
            [clojure.string :as s]))

(def list-query
  "query ($filters: [Filter] $propType: String) {
  sequenceFeatures(filters: $filters) {
    __typename
    iri
    label
    assertions(proposition_type: $propType) {
      __typename
      curie
      label
      evidenceStrength {
        curie
      }
    }
  }
}
")

(re-frame/reg-event-db
 ::recieve-list
 (fn [db [_ result]]
   (js/console.log "recieved list")
   (assoc db :gencc/gene-list (get-in result [:response :data :sequenceFeatures]))))

(re-frame/reg-event-fx
 ::fetch-list
 (fn [{:keys [db]} _]
   (js/console.log "fetching genes list")
   {:fx [[:dispatch
          [::re-graph/query
           {:id ::query
            :query list-query
            :variables {:filters {:filter "has_assertion"
                                  :argument "CG:GeneValidityProposition"}
                        :propType "CG:GeneValidityProposition"}
            :callback [::recieve-list]}]]]}))

(re-frame/reg-sub
  ::gene-list
  :-> :gencc/gene-list)


(def menu-items
  [{:href "https://search.thegencc.org/genes",
    :label "Genes"
    :icon icon/beaker-micro}
   {:href "https://search.thegencc.org/submitters",
    :label "Submitters"
    :icon icon/building-office-micro}
   {:href "https://search.thegencc.org/statistics",
    :label "Statistics"
    :icon icon/chart-pie-micro}
   {:href "https://search.thegencc.org/download",
    :label "Download"
    :icon icon/document-arrow-down-micro}
   {:href "https://thegencc.org/faq",
    :label "FAQ"
    :icon icon/question-mark-circle-micro}
   {:href "mailto:gencc@thegencc.org",
    :label "Contact"
    :icon icon/chat-bubble-bottom-center-text-micro}
   {:href "https://thegencc.org/",
    :label "About"
    :icon icon/home-micro}
   {:href  "https://creationproject.us7.list-manage.com/subscribe?u=47520fb4e4a2c9edfc44a61af&id=7ccf9c9b09",
    :label "Stay Informed"
    :icon icon/envelope-micro}])
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
       :src
       "https://tailwindcss.com/plus-assets/img/logos/mark.svg?color=indigo&shade=600",
       :alt "Your Company"}]]
    [:div
     {:class "hidden sm:ml-6 sm:flex sm:space-x-8"}
     (comment
      "Current: \"border-indigo-500 text-gray-900\", Default: \"border-transparent text-gray-500 hover:border-gray-300 hover:text-gray-700\"")
     [:a
      {:href "#",
       :class
       "inline-flex items-center border-b-2 border-indigo-500 px-1 pt-1 text-sm font-medium text-gray-900"}
      "Dashboard"]
     [:a
      {:href "#",
       :class
       "inline-flex items-center border-b-2 border-transparent px-1 pt-1 text-sm font-medium text-gray-500 hover:border-gray-300 hover:text-gray-700"}
      "Team"]
     [:a
      {:href "#",
       :class
       "inline-flex items-center border-b-2 border-transparent px-1 pt-1 text-sm font-medium text-gray-500 hover:border-gray-300 hover:text-gray-700"}
      "Projects"]
     [:a
      {:href "#",
       :class
       "inline-flex items-center border-b-2 border-transparent px-1 pt-1 text-sm font-medium text-gray-500 hover:border-gray-300 hover:text-gray-700"}
      "Calendar"]]]
   [:div
    {:class "hidden sm:ml-6 sm:flex sm:items-center"}
    [:button
     {:type "button",
      :class
      "relative rounded-full bg-white p-1 text-gray-400 hover:text-gray-500 focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 focus:outline-hidden"}
     [:span {:class "absolute -inset-1.5"}]
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
        "M14.857 17.082a23.848 23.848 0 0 0 5.454-1.31A8.967 8.967 0 0 1 18 9.75V9A6 6 0 0 0 6 9v.75a8.967 8.967 0 0 1-2.312 6.022c1.733.64 3.56 1.085 5.455 1.31m5.714 0a24.255 24.255 0 0 1-5.714 0m5.714 0a3 3 0 1 1-5.714 0"}]]]
    (comment "Profile dropdown")
    [:div
     {:class "relative ml-3"}
     [:div
      [:button
       {:type "button",
        :class
        "relative flex rounded-full bg-white text-sm focus:ring-2 focus:ring-indigo-500 focus:ring-offset-2 focus:outline-hidden",
        :id "user-menu-button",
        :aria-expanded "false",
        :aria-haspopup "true"}
       [:span {:class "absolute -inset-1.5"}]
       [:span {:class "sr-only"} "Open user menu"]
       [:img
        {:class "size-8 rounded-full",
         :src
         "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80",
         :alt ""}]]]
     (comment
      "Dropdown menu, show/hide based on menu state.\n\n            Entering: \"transition ease-out duration-200\"\n              From: \"transform opacity-0 scale-95\"\n              To: \"transform opacity-100 scale-100\"\n            Leaving: \"transition ease-in duration-75\"\n              From: \"transform opacity-100 scale-100\"\n              To: \"transform opacity-0 scale-95\"")
     [:div
      {:class
       "absolute right-0 z-10 mt-2 w-48 origin-top-right rounded-md bg-white py-1 ring-1 shadow-lg ring-black/5 focus:outline-hidden",
       :role "menu",
       :aria-orientation "vertical",
       :aria-labelledby "user-menu-button",
       :tabindex "-1"}
      (comment
       "Active: \"bg-gray-100 outline-hidden\", Not Active: \"\"")
      [:a
       {:href "#",
        :class "block px-4 py-2 text-sm text-gray-700",
        :role "menuitem",
        :tabindex "-1",
        :id "user-menu-item-0"}
       "Your Profile"]
      [:a
       {:href "#",
        :class "block px-4 py-2 text-sm text-gray-700",
        :role "menuitem",
        :tabindex "-1",
        :id "user-menu-item-1"}
       "Settings"]
      [:a
       {:href "#",
        :class "block px-4 py-2 text-sm text-gray-700",
        :role "menuitem",
        :tabindex "-1",
        :id "user-menu-item-2"}
       "Sign out"]]]]]]
]


(defn gencc-nav []
  [:nav
   {:class "mx-auto flex max-w-7xl justify-between p-4 lg:px-8 shadow"
    #_"pb-2 bg-white fixed w-full z-30 top-0 shadow"}
   [:div
    {:class
     "w-full container mx-auto flex flex-wrap items-center mt-0 pt-3 pb-3 md:pb-0"}
    [:div
     {:class "flex flex-1"}
     [:a
      {:class
       "text-gray-900 text-base xl:text-xl no-underline hover:no-underline font-bold",
       :href "https://thegencc.org/"}
      [:img {:src "/img/genecc-logo.jpg", :class "h-16"}]]]
    [:div
     {:class "flex"}
     [:div
      {:class "flex text-sm"}
      (for [i menu-items]
        ^{:key i}
        [:a
         {:href (:href i)}
         [:div
          {:class "flex items-center text-gray-800 p-3"}
          [:div
           {:class "text-gray-400 pr-1"}
           (:icon i)]
          (:label i)]])]]]])

;; will need to clean this up some
(defn mobile-menu []
  [:div
   {:class "block lg:hidden pr-4"}
   [:div
    {}
    [:button
     {:id "nav-toggle",
      :class
      "flex items-center px-3 py-2 border rounded text-gray-500 border-gray-600 hover:text-gray-900 hover:border-teal-500 appearance-none focus:outline-none"}
     [:svg
      {:class "fill-current h-3 w-3",
       :viewBox "0 0 20 20",
       :xmlns "http://www.w3.org/2000/svg"}
      [:title "Menu"]
      [:path {:d "M0 3h20v2H0V3zm0 6h20v2H0V9zm0 6h20v2H0v-2z"}]]]]
   [:div
    {:class
     "z-10 origin-top-right absolute right-0 mt-2 rounded-md shadow-lg"}
    [:div
     {:class "rounded-md bg-white shadow-xs"}
     [:div
      {:class "py-2",
       :role "menu",
       :aria-orientation "vertical",
       :aria-labelledby "options-menu"}
      [:a
       {:href "https://search.thegencc.org/genes",
        :class
        "whitespace-no-wrap block px-4 py-2 text-sm leading-5 text-gray-700"}
       [:i {:class "fas fa-dna text-gray-400"}]
       "Genes"]
      [:a
       {:href "https://search.thegencc.org/submitters",
        :class
        "whitespace-no-wrap block px-4 py-2 text-sm leading-5 text-gray-700"}
       [:i {:class "far fa-building text-gray-400"}]
       "Submitters"]
      [:a
       {:href "https://search.thegencc.org/statistics",
        :class
        "whitespace-no-wrap block px-4 py-2 text-sm leading-5 text-gray-700"}
       [:i {:class "fas fa-chart-bar"}]
       "Statistics"]
      [:a
       {:href "https://search.thegencc.org/download",
        :class
        "whitespace-no-wrap block px-4 py-2 text-sm leading-5 text-gray-700"}
       [:i {:class "fas fa-file-code"}]
       "Download"]
      [:a
       {:href "https://thegencc.org/faq",
        :target "_blank",
        :class
        "whitespace-no-wrap block px-4 py-2 text-sm leading-5 text-gray-700"}
       [:i {:class "far fa-question-circle"}]
       "FAQ"]
      [:a
       {:href "mailto:gencc@thegencc.org",
        :class
        "whitespace-no-wrap block px-4 py-2 text-sm leading-5 text-gray-700"}
       [:i {:class "far fa-comment"}]
       "Contact"]
      [:a
       {:href "https://thegencc.org/",
        :target "_blank",
        :class
        "whitespace-no-wrap block px-4 py-2 text-sm leading-5 text-gray-700"}
       [:i {:class "far fa-comment"}]
       "About Us"]
      [:a
       {:id "click-signup",
        :href
        "https://creaationproject.us7.list-manage.com/subscribe/post?u=47520fb4e4a2c9edfc44a61af&id=7ccf9c9b09",
        :target "_blank",
        :class
        "whitespace-no-wrap block px-4 py-2 text-sm leading-5 text-gray-700"}
       [:i {:class "fas fa-mail-bulk"}] 
       "Stay Informed"]]]]])

(def classification-pill-props
  {"CG:Definitive" {:bg "bg-green-800 text-green-50"}
   "CG:Strong" {:bg "bg-green-600 text-green-50"}
   "CG:Moderate" {:bg "bg-green-200 text-green-900"}
   "CG:Supportive" {:bg "bg-sky-500 text-sky-50"}
   "CG:Limited" {:bg "bg-red-300 text-red-900"}
   "CG:Disputed" {:bg "bg-red-600 text-red-50"}
   "CG:Refuted" {:bg "bg-red-800 text-red-50"}
   "CG:AnimalModelOnly" {:bg "bg-yellow-400"}
   "CG:NoKnownDiseaseRelationship" {:bg "bg-gray-400 text-gray-50"}})

(defn classification-pill [class-counts classification]
  (let [count (get class-counts classification 0)
        css-class (if (< 0 count)
                    (get-in classification-pill-props [classification :bg])
                    "bg-gray-300 text-gray-200")]
    [:div
     {:class (str "flex place-content-center items-center px-1 rounded-full w-8 " css-class)}
     count]))

(defn assertion-bubbles [g]
  (let [class-counts (->> (:assertions g)
                          (map #(get-in % [:evidenceStrength :curie]))
                          frequencies)]
    [:div
     {:class "flex  gap-2"}
     [:div
      {:class "flex bg-gray-200 px-2 py-1 rounded-full gap-1"}
      (classification-pill class-counts "CG:Definitive")
      (classification-pill class-counts "CG:Strong")
      (classification-pill class-counts "CG:Moderate")]
     (classification-pill class-counts "CG:Supportive")
     [:div
      {:class "flex bg-gray-200 px-2 py-1 rounded-full gap-1"}
      (classification-pill class-counts "CG:Limited")
      (classification-pill class-counts "CG:Disputed")
      (classification-pill class-counts "CG:Refuted")
      (classification-pill class-counts "CG:AnimalModelOnly")
      (classification-pill class-counts "CG:NoKnownDiseaseRelationship")]]))

(defn gene-list []
  [:div
   {:class "p-8"}
   (for [l @(re-frame/subscribe [::gene-list])]
     ^{:key l}
     [:div
      {:class "flex py-3 items-center"}
      [:div
       {:class "flex flex-1 px-4 font-semibold"}
       (:label l)]
      [:div
       {:class "flex px-4"}
       (count (:assertions l))]
      [:div
       {:class "flex px-4"}
       (assertion-bubbles l)]])])

(defn home []
  [:div
   (gencc-nav)
   (gene-list)])
