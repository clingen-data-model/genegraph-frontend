(ns genegraph.frontend.view.sequence-feature
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.icon :as icon]
            [genegraph.frontend.common :as common]
            [clojure.string :as str]))


(defn feature-actions []
  [:div
   {:class "mt-4 flex md:mt-0 md:ml-4"}
   [:button
    {:type "button",
     :class
     "inline-flex items-center rounded-md bg-white px-3 py-2 text-sm font-semibold text-gray-900 shadow-xs ring-1 ring-gray-300 ring-inset hover:bg-gray-50"}
    "Edit"]
   [:button
    {:type "button",
     :class
     "ml-3 inline-flex items-center rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-xs hover:bg-indigo-700 focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600"}
    "Publish"]])

(defn feature-title [feature]
  [:div
   {:class "md:flex md:items-center md:justify-between"}
   [:div
    {:class "min-w-0 flex-1"}
    [:div
     {:class
      "text-2xl/7 font-bold text-gray-900 sm:truncate sm:text-3xl sm:tracking-tight"}
     (:label feature)]]
   [:div {:class "px-4 py-5 sm:p-6 flex"}
    (for [t (:type feature)]
      ^{:key [feature t]}
      [:span
       {:class
        "inline-flex items-center rounded-md bg-gray-50 px-2 py-1 text-xs font-medium text-gray-600 ring-1 ring-gray-500/10 ring-inset"}
       (str/replace  (:label t) #"_" " ")])]])

(defn feature-types [feature]
  [:div
   {:class "overflow-hidden rounded-lg bg-white shadow-sm"}
   [:div {:class "px-4 py-5 sm:p-6 flex"}
    (for [t (:type feature)]
      ^{:key [feature t]}
      [:span
       {:class
        "inline-flex items-center rounded-md bg-gray-50 px-2 py-1 text-xs font-medium text-gray-600 ring-1 ring-gray-500/10 ring-inset"}
       (str/replace  (:label t) #"_" " ")])]])

(defn classification-badge [assertion]
  [:span
   {:class
    "inline-flex items-center rounded-md bg-gray-100 px-2 py-1 text-xs font-medium text-gray-600"}
   "Badge"])

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

(defn mechanism-assertions [assertions]
  (if (seq assertions)
    [:div
     {:class "overflow-hidden rounded-md bg-white shadow-sm"}
     [:div
      {:class "border-b border-gray-200 bg-white px-4 py-5 sm:px-6"}
      [:h3 {:class "text-base font-semibold text-gray-900"} "Mechanism of Disease"]]
     [:ul
      {:role "list", :class "divide-y divide-gray-200"}
      (for [a assertions]
        (mechanism-assertion a))]]
    nil))

(defn overlapping-feature [feature]
  ^{:key (:curie feature)}
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
        {:href (common/resource-href feature)}
        [:span {:class "absolute inset-x-0 -top-px bottom-0"}]
        (:label feature)]]
      #_[:span
       {:class
        "inline-flex items-center rounded-md bg-gray-100 px-2 py-1 text-xs font-medium text-gray-600"}
         (count (:assertions feature))]]
     [:p
      {:class "mt-1 flex text-xs/5 text-gray-500"}
      [:a
       {#_#_:href (common/resource-href assertion)
        :class "relative truncate hover:underline"}
       [:span (count (:assertions feature)) " assertions"]
       #_(get-in assertion [:subject :mechanism :curie])]]]]
   #_(approval-div approval)])

(defn overlapping-features [features]
  (if (seq features)
    [:div
     {:class "overflow-hidden rounded-md bg-white shadow-sm"}
     [:div
      {:class "border-b border-gray-200 bg-white px-4 py-5 sm:px-6"}
      [:h3 {:class "text-base font-semibold text-gray-900"} "Overlapping Features"]]
     [:ul
      {:role "list", :class "divide-y divide-gray-200"}
      (for [f (->> features
                   (sort-by #(count (:assertions %)))
                   reverse)]
        (overlapping-feature f))]]
    nil))


(defn overlapping-variant [variant]
  ^{:key (:curie variant)}
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
        {:href (:iri variant)
         :target "_blank"}
        [:span {:class "absolute inset-x-0 -top-px bottom-0"}]
        (:label variant)]]
      #_[:span
         {:class
          "inline-flex items-center rounded-md bg-gray-100 px-2 py-1 text-xs font-medium text-gray-600"}
         (count (:assertions feature))]]
     #_[:p
        {:class "mt-1 flex text-xs/5 text-gray-500"}
        [:a
         {:href (common/resource-href assertion)
          :class "relative truncate hover:underline"}
         [:span (count (:assertions feature)) " assertions"]
         (get-in assertion [:subject :mechanism :curie])]]]]
   #_(approval-div (-> assertion :contributions first))
   (for [a (:assertions variant)]
     ^{:key a}
     [:div (get-in a [:classification :curie])])])

(defn counts-div [variants]
  (let [counts (->> variants
                    (mapcat :assertions)
                    (map #(get-in % [:classification :curie]))
                    frequencies)]
    [:pre (with-out-str (cljs.pprint/pprint counts))]))

(defn overlapping-variants [label variants]
  (if (seq variants)
    [:div
     {:class "overflow-hidden rounded-md bg-white shadow-sm"}
     [:div
      {:class "border-b border-gray-200 bg-white px-4 py-5 sm:px-6 flex items-center justify-between"}
      [:h3 {:class "text-base font-semibold text-gray-900"}
       label]
      (counts-div variants)]
     [:ul
      {:role "list", :class "divide-y divide-gray-200"}
      (for [v variants]
        (overlapping-variant v))]]
    nil))

;; TODO maybe lighter gray for classifications
(defn validity-assertion [assertion]
  (let [disease (get-in assertion [:subject :disease])
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
           (or (:label disease)
               (:curie disease))
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
         (get-in assertion [:subject :modeOfInheritance :label])]]]]
     (approval-div approval)]))


(defn validity-assertions [assertions]
  (if (seq assertions)
    [:div
     {:class "overflow-hidden rounded-md bg-white shadow-sm"}
     [:div
      {:class "border-b border-gray-200 bg-white px-4 py-5 sm:px-6"}
      [:h3 {:class "text-base font-semibold text-gray-900"}
       "Disease Association"]]
     [:ul
      {:role "list",
       :class
       "divide-y divide-gray-100 overflow-hidden bg-white shadow-xs ring-1 ring-gray-900/5 sm:rounded-xl"}
      (for [a assertions]
        (validity-assertion a))]]
    nil))


(defmethod common/main-view "SequenceFeature" [feature]
  (let [grouped-assertions (group-by #(get-in % [:subject :__typename])
                                     (:assertions feature))
        grouped-variants (group-by #(get-in % [:copyChange :curie])
                                   (:overlappingVariants feature))]
    [:div
     {:class "flex flex-col gap-6"}
     (feature-title feature)
     #_(feature-types feature)
     (mechanism-assertions (get grouped-assertions
                                "GeneticConditionMechanismProposition"))
     (validity-assertions (get grouped-assertions
                               "GeneValidityProposition"))
     (overlapping-features (:overlappingFeatures feature))
     (overlapping-variants "Overlapping Copy Number Loss"
                           (get grouped-variants "EFO:0030067"))
     (overlapping-variants "Overlapping Copy Number Gain"
                           (get grouped-variants "EFO:0030070"))
     [:div
      [:pre (with-out-str (cljs.pprint/pprint feature))]]]))
