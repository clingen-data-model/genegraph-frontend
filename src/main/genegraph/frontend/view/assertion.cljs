(ns genegraph.frontend.view.assertion
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.icon :as icon]
            [genegraph.frontend.common :as common]
            [genegraph.frontend.fragment.assertion-list :as assertion-list]
            [clojure.string :as str]))



(defn approval-div [approval]
  [:div
   {:class "flex shrink-0 items-center gap-x-4"}
   [:div
    {:class "hidden sm:flex sm:flex-col sm:items-end"}
    [:p {:class "text-sm/6 text-gray-900"}
     (or (get-in approval [:agent :label])
         (get-in approval [:agent :curie]))]
    [:p
     {:class "mt-1 text-xs/5 text-gray-500"}
     [:time {:dateTime (:date approval)}
      (subs (:date approval) 0 10)]]]
   icon/arrow])

#_[:pre (with-out-str (cljs.pprint/pprint stack))]
(defn mechanism-assertion [assertion]
  (let [condition (get-in assertion [:subject :condition])
        approval (-> assertion :contributions first)]
    ^{:key (:iri assertion)}
    [:li
     {:class
      "relative flex justify-between gap-x-6 px-4 py-5 hover:bg-gray-50 sm:px-6"}
     [:div
      {:class "flex min-w-0 gap-x-4"}
      [:div
       {:class "min-w-0 flex-auto"}
       [:div
        {:class "text-sm/6 font-semibold text-gray-900 flex"}
        [:div
         {:class "pr-4"}
         [:a
          {:href (common/resource-href assertion)}
          [:span {:class "absolute inset-x-0 -top-px bottom-0"}]
          (str/replace 
           (or (:label condition)
               (:curie condition))
           #"obsolete_"
           "")]]
        [:span
         {:class
          "inline-flex items-center rounded-md bg-gray-100 px-2 py-1 text-xs font-medium text-gray-600"}
         (get-in assertion [:evidenceStrength :curie])]]
       [:p
        {:class "mt-1 flex text-xs/5 text-gray-500"}
        [:a
         {:href (common/resource-href assertion)
          :class "relative truncate hover:underline"}
         (get-in assertion [:subject :mechanism :curie])]]]]
     (approval-div approval)]))

(defn header [assertion]
  (let [disease (get-in assertion [:subject :disease])
        gene (get-in assertion [:subject :gene])
        moi (get-in assertion [:subject :modeOfInheritance])]
    [:header
     [:div
      {:class "mx-auto max-w-7xl px-4 pt-12 sm:px-6 lg:px-8"}
      [:div
       {:class
        "mx-auto flex max-w-2xl items-center justify-between gap-x-8 lg:mx-0 lg:max-w-none"}
       [:div
        {:class "flex items-center  gap-x-6"}
        [:div
         {:class "font-semibold text-4xl"}
         (:label gene)]
        [:div
         [:div
          {:class "font-normal"}
          (:label disease)]
         [:div
          {:class "font-light text-gray-500"}
          (:label moi)]]]
       [:div
        {:class "text-right"}
        [:div
         {:class "text-base font-bold text-gray-900"}
         (get-in assertion [:evidenceStrength :label])]
        [:div
         {:class "text-sm/6 text-gray-500"}
         (get-in assertion [:specifiedBy :label])]]]]]))

(defn filter-evidence-lines-by-criteria [criteria evidence-lines]
  (filter #(= criteria (get-in % [:specifiedBy :curie])) evidence-lines))

(defn evidence-line-div [label score]
  [:tr
   {:class "border-b border-gray-100"}
   [:td
    {:class "max-w-0 px-0 py-5 align-top"}
    [:div
     {:class "truncate font-medium text-gray-900"}
     label]
    [:div
     {:class "truncate text-gray-500 hidden"}
     "Variant Observations, case control studies, and family segregations."]]
   [:td
    {:class
     "hidden py-5 pr-0 pl-8 text-right align-top text-gray-700 tabular-nums sm:table-cell"}
    #_"5"]
   [:td
    {:class
     "py-5 pr-0 pl-8 text-right align-top text-gray-700 tabular-nums"}
    score]])

(defn gene-validity-assertion-evidence-detail [assertion]
  [:div
   {:class
    "shadow-xs sm:mx-0 sm:rounded-lg sm:pb-14 lg:col-span-2 lg:row-span-2 lg:row-end-2 xl:pb-20 px-2"}
   [:div
    {:class "text-sm/7 font-light"}
    (:description assertion)]
   [:table
    {:class "mt-16 w-full text-left text-sm/6 whitespace-nowrap"}
    [:colgroup [:col {:class "w-full"}] [:col] [:col]]
    [:thead
     {:class "border-b border-gray-200 text-gray-900"}
     [:tr
      [:th
       {:scope "col", :class "px-0 py-3 font-semibold"}
       "Evidence"]
      [:th
       {:scope "col",
        :class
        "hidden py-3 pr-0 pl-8 text-right font-semibold sm:table-cell"}
       #_"Count"]
      [:th
       {:scope "col",
        :class "py-3 pr-0 pl-8 text-right font-semibold"}
       "Score"]]]
    [:tbody
     (evidence-line-div
      "Genetic Evidence"
      (-> (filter-evidence-lines-by-criteria
           "CG:GeneValidityOverallGeneticEvidenceCriteria"
           (:evidence assertion))
          first
          :strengthScore))
     (evidence-line-div
      "Experimental Evidence"
      (-> (filter-evidence-lines-by-criteria
           "CG:GeneValidityOverallExperimentalEvidenceCriteria"
           (:evidence assertion))
          first
          :strengthScore))]
    [:tfoot
     [:tr
      [:th
       {:scope "row",
        :class "pt-4 font-semibold text-gray-900 sm:hidden"}
       "Total"]
      [:th
       {:scope "row",
        :colSpan "2",
        :class
        "hidden pt-4 text-right font-semibold text-gray-900 sm:table-cell"}
       "Total"]
      [:td
       {:class
        "pt-4 pr-0 pb-0 pl-8 text-right font-semibold text-gray-900 tabular-nums"}
       (:strengthScore assertion)]]]]])

(defn filter-by-role [role contribs]
  (->> contribs (filter #(= role (get-in % [:role :curie])))))

(defn activity-date [activity]
  (if-let [date (:date activity)]
    (subs date 0 10)
    ""))

(defn contributions-div [assertion]
  (let [contribs (:contributions assertion)
        approval (first (filter-by-role "CG:Approver" contribs))
        publish (first (filter-by-role "CG:Publisher" contribs))
        secondary-contributors (filter-by-role "CG:SecondaryContributor" contribs)]
    [:div
     {:class "rounded-lg bg-gray-50 shadow-xs ring-1 ring-gray-900/5"}
     [:dl
      {:class "flex flex-wrap gap-2"}
      [:div
       {:class
        "flex w-full flex-none gap-x-4  px-6 pt-4"}
       [:dt
        {:class "flex-none w-1/3 text-sm/6 font-extralight"}
        [:span "Approver"]]
       [:dd
        {:class "text-sm/6 font-medium text-gray-900"}
        (get-in approval [:agent :label])]]
      [:div
       {:class "flex w-full flex-none gap-x-4 px-6"}
       [:dt
        {:class "flex-none w-1/3 text-sm/6 font-extralight"}
        "Approved"]
       [:dd
        {:class "text-sm/6 text-gray-900 font-medium"}
        [:time (activity-date approval)]]]
      [:div
       {:class "flex w-full flex-none gap-x-4 px-6"}
       [:dt
        {:class "flex-none w-1/3 text-sm/6 font-extralight"}
        "Published"]
       [:dd
        {:class "text-sm/6 text-gray-500"}
        [:time (activity-date publish)]]]
      [:div
       {:class "flex w-full flex-none gap-x-4 px-6 pb-4"}
       [:dt
        {:class "flex-none w-1/3 text-sm/6 font-extralight"}
        "Version"]
       [:dd
        {:class "text-sm/6 text-gray-500"}
        [:time (:version assertion)]]]
      (when (seq secondary-contributors)
        [:div
         {:class #_"mt-6 border-t border-gray-900/5 px-6 py-6"
          "mt-4 py-4 border-t border-gray-900/5 flex w-full flex-none gap-x-4 "}
         (for [c secondary-contributors]
           [:div
            {:class "flex w-full flex-none gap-x-4 px-6"}
            [:dt
             {:class "flex-none w-1/3 text-sm/6 font-extralight"}
             "Secondary Contrbutor"]
            [:dd
             {:class "text-sm/6 text-gray-900"}
             (get-in c [:agent :label])]])])]]
    #_[:pre (with-out-str (cljs.pprint/pprint approval))]))

(defn approval-date [assertion]
  (if-let [approval
           (first (filter-by-role "CG:Approver" (:contributions assertion)))]
    (activity-date approval)
    ""))

(defn versions [assertion]
  [:div
   {:class "lg:col-start-3"}
   [:h2 {:class "text-sm/6 font-semibold text-gray-900"} "Versions"]
   [:ul
    {:role "list", :class "mt-6 space-y-6"}
    (for [v (->> (:versions assertion) (sort-by :version) reverse)]
      ^{:key v}
      [:li
       {:class "relative flex gap-x-4"}
       [:div
        {:class
         "absolute top-0 -bottom-6 left-0 flex w-6 justify-center"}
        (when (not= "1.0" (:version v))
          [:div {:class "w-px bg-gray-200"}])]
       [:div
        {:class
         "relative flex size-6 flex-none items-center justify-center bg-white"}
        [:div
         {:class "text-xs font-semibold"}
         (:version v)]]
       [:div
        {:class "flex-auto py-0.5 text-xs/5 text-gray-500"}
        [:div
         [:span
          {:class "font-medium text-gray-900"}
          (get-in v [:evidenceStrength :label])]]
        (for [r (:curationReasons v)]
          ^{:key [v r]}
          [:div (:label r)])]
       [:time
        {:dateTime "2023-01-23T10:32",
         :class "flex-none py-0.5 text-xs/5 text-gray-500"}
        (approval-date v)]])]])

(defn validity-assertion [assertion]
  (let [disease (get-in assertion [:subject :disease])
        gene (get-in assertion [:subject :gene])
        moi (get-in assertion [:subject :modeOfInheritance])]
    [:main
     (header assertion)
     [:div
      {:class "mx-auto max-w-7xl py-16 sm:px-6"}
      [:div
       {:class
        "mx-auto grid max-w-2xl grid-cols-1 grid-rows-1 items-start gap-x-8 gap-y-8 lg:mx-0 lg:max-w-none lg:grid-cols-3"}
       [:div
        {:class "lg:col-start-3 lg:row-end-1"}
        [:h2 {:class "sr-only"} "Summary"]
        (contributions-div assertion)]
       (gene-validity-assertion-evidence-detail assertion)
       (versions assertion)]]
     [:pre (with-out-str (cljs.pprint/pprint assertion))]]))

(defn variant-path-header [assertion]
  [:header
   [:div
    {:class "mx-auto max-w-7xl pt-12 "}
    [:div
     {:class
      "mx-auto flex max-w-2xl items-center justify-between gap-x-8 lg:mx-0 lg:max-w-none"}
     [:div
      {:class "flex items-center  gap-x-6"}
      [:div
       {:class "font-semibold text-lg"}
       (get-in assertion [:subject :variant :label])]
      #_[:div
       [:div
        {:class "font-normal"}
        (:label disease)]
       [:div
        {:class "font-light text-gray-500"}
        (:label moi)]]]
     [:div
      {:class "text-right"}
      [:div
       {:class "text-base font-bold text-gray-900"}
       (get-in assertion [:classification :label])]
      [:div
       {:class "text-sm/6 text-gray-500"}
       (get-in assertion [:specifiedBy :label])]]]]])

(defn conflicts-div [])

(defn variant-pathogenicity-assertion [assertion]
  [:main
   {:class "px-4 sm:px-6 lg:px-8"}
   (variant-path-header assertion)
   [:div
    [:h3 {:class "font-semibold text-lg py-4"}
     "conflicting assertions"]
    (assertion-list/assertion-list-div (:conflictingAssertions assertion))]
   
   #_[:div
      {:class "mx-auto max-w-7xl py-16 sm:px-6"}
      [:div
       {:class
        "mx-auto grid max-w-2xl grid-cols-1 grid-rows-1 items-start gap-x-8 gap-y-8 lg:mx-0 lg:max-w-none lg:grid-cols-3"}
       [:div
        {:class "lg:col-start-3 lg:row-end-1"}
        [:h2 {:class "sr-only"} "Summary"]
        (contributions-div assertion)]
       (gene-validity-assertion-evidence-detail assertion)
       (versions assertion)]]
   [:pre (with-out-str (cljs.pprint/pprint assertion))]])

(defmethod common/main-view "EvidenceStrengthAssertion" [assertion]
  (case (get-in assertion [:subject :__typename])
    "GeneValidityProposition" (validity-assertion assertion)
    "GeneticConditionMechanismProposition" (mechanism-assertion assertion)
    "VariantPathogenicityProposition" (variant-pathogenicity-assertion assertion)))

