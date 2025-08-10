(ns genegraph.frontend.view.assertion
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.icon :as icon]
            [genegraph.frontend.common :as common]
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
        {:class "text-sm/6 text-gray-500"}
        "Gene Disease Validity"]]]]))

;; example invoice summary

(defn validity-assertion [assertion]
  (let [disease (get-in assertion [:subject :disease])
        gene (get-in assertion [:subject :gene])
        moi (get-in assertion [:subject :modeOfInheritance])]
    [:main
     (header assertion)
     [:div
      {:class "mx-auto max-w-7xl px-4 py-16 sm:px-6 lg:px-8"}
      [:div
       {:class
        "mx-auto grid max-w-2xl grid-cols-1 grid-rows-1 items-start gap-x-8 gap-y-8 lg:mx-0 lg:max-w-none lg:grid-cols-3"}
       
       [:div
        {:class "lg:col-start-3 lg:row-end-1"}
        [:h2 {:class "sr-only"} "Summary"]
        [:div
         {:class "rounded-lg bg-gray-50 shadow-xs ring-1 ring-gray-900/5"}
         [:dl
          {:class "flex flex-wrap"}
          [:div
           {:class "flex-auto pt-6 pl-6"}
           [:dt {:class "text-sm/6 font-semibold text-gray-900"} "Evidence Strength"]
           [:dd
            {:class "mt-1 text-base font-semibold text-gray-900"}
            (get-in assertion [:evidenceStrength :curie])]]
          [:div
           {:class
            "mt-6 flex w-full flex-none gap-x-4 border-t border-gray-900/5 px-6 pt-6"}
           [:dt
            {:class "flex-none"}
            [:span {:class "sr-only"} "Client"]
            [:svg
             {:viewBox "0 0 20 20",
              :fill "currentColor",
              :data-slot "icon",
              :aria-hidden "true",
              :class "h-6 w-5 text-gray-400"}
             [:path
              {:d
               "M18 10a8 8 0 1 1-16 0 8 8 0 0 1 16 0Zm-5.5-2.5a2.5 2.5 0 1 1-5 0 2.5 2.5 0 0 1 5 0ZM10 12a5.99 5.99 0 0 0-4.793 2.39A6.483 6.483 0 0 0 10 16.5a6.483 6.483 0 0 0 4.793-2.11A5.99 5.99 0 0 0 10 12Z",
               :clip-rule "evenodd",
               :fill-rule "evenodd"}]]]
           [:dd
            {:class "text-sm/6 font-medium text-gray-900"}
            "Alex Curren"]]
          [:div
           {:class "my-4 flex w-full flex-none gap-x-4 px-6"}
           [:dt
            {:class "flex-none"}
            [:span {:class "sr-only"} "Due date"]
            [:svg
             {:viewBox "0 0 20 20",
              :fill "currentColor",
              :data-slot "icon",
              :aria-hidden "true",
              :class "h-6 w-5 text-gray-400"}
             [:path
              {:d
               "M5.25 12a.75.75 0 0 1 .75-.75h.01a.75.75 0 0 1 .75.75v.01a.75.75 0 0 1-.75.75H6a.75.75 0 0 1-.75-.75V12ZM6 13.25a.75.75 0 0 0-.75.75v.01c0 .414.336.75.75.75h.01a.75.75 0 0 0 .75-.75V14a.75.75 0 0 0-.75-.75H6ZM7.25 12a.75.75 0 0 1 .75-.75h.01a.75.75 0 0 1 .75.75v.01a.75.75 0 0 1-.75.75H8a.75.75 0 0 1-.75-.75V12ZM8 13.25a.75.75 0 0 0-.75.75v.01c0 .414.336.75.75.75h.01a.75.75 0 0 0 .75-.75V14a.75.75 0 0 0-.75-.75H8ZM9.25 10a.75.75 0 0 1 .75-.75h.01a.75.75 0 0 1 .75.75v.01a.75.75 0 0 1-.75.75H10a.75.75 0 0 1-.75-.75V10ZM10 11.25a.75.75 0 0 0-.75.75v.01c0 .414.336.75.75.75h.01a.75.75 0 0 0 .75-.75V12a.75.75 0 0 0-.75-.75H10ZM9.25 14a.75.75 0 0 1 .75-.75h.01a.75.75 0 0 1 .75.75v.01a.75.75 0 0 1-.75.75H10a.75.75 0 0 1-.75-.75V14ZM12 9.25a.75.75 0 0 0-.75.75v.01c0 .414.336.75.75.75h.01a.75.75 0 0 0 .75-.75V10a.75.75 0 0 0-.75-.75H12ZM11.25 12a.75.75 0 0 1 .75-.75h.01a.75.75 0 0 1 .75.75v.01a.75.75 0 0 1-.75.75H12a.75.75 0 0 1-.75-.75V12ZM12 13.25a.75.75 0 0 0-.75.75v.01c0 .414.336.75.75.75h.01a.75.75 0 0 0 .75-.75V14a.75.75 0 0 0-.75-.75H12ZM13.25 10a.75.75 0 0 1 .75-.75h.01a.75.75 0 0 1 .75.75v.01a.75.75 0 0 1-.75.75H14a.75.75 0 0 1-.75-.75V10ZM14 11.25a.75.75 0 0 0-.75.75v.01c0 .414.336.75.75.75h.01a.75.75 0 0 0 .75-.75V12a.75.75 0 0 0-.75-.75H14Z"}]
             [:path
              {:d
               "M5.75 2a.75.75 0 0 1 .75.75V4h7V2.75a.75.75 0 0 1 1.5 0V4h.25A2.75 2.75 0 0 1 18 6.75v8.5A2.75 2.75 0 0 1 15.25 18H4.75A2.75 2.75 0 0 1 2 15.25v-8.5A2.75 2.75 0 0 1 4.75 4H5V2.75A.75.75 0 0 1 5.75 2Zm-1 5.5c-.69 0-1.25.56-1.25 1.25v6.5c0 .69.56 1.25 1.25 1.25h10.5c.69 0 1.25-.56 1.25-1.25v-6.5c0-.69-.56-1.25-1.25-1.25H4.75Z",
               :clip-rule "evenodd",
               :fill-rule "evenodd"}]]]
           [:dd
            {:class "text-sm/6 text-gray-500"}
            [:time {:datetime "2023-01-31"} "January 31, 2023"]]]]]]
       (comment "Invoice")
       [:div
        {:class
         "-mx-4 px-4 py-8 shadow-xs ring-1 ring-gray-900/5 sm:mx-0 sm:rounded-lg sm:px-8 sm:pb-14 lg:col-span-2 lg:row-span-2 lg:row-end-2 xl:px-16 xl:pt-16 xl:pb-20"}
        [:table
         {:class "mt-16 w-full text-left text-sm/6 whitespace-nowrap"}
         [:colgroup [:col {:class "w-full"}] [:col] [:col]]
         [:thead
          {:class "border-b border-gray-200 text-gray-900"}
          [:tr
           [:th
            {:scope "col", :class "px-0 py-3 font-semibold"}
            "Evidence Lines"]
           [:th
            {:scope "col",
             :class
             "hidden py-3 pr-0 pl-8 text-right font-semibold sm:table-cell"}
            "Count"]
           [:th
            {:scope "col",
             :class "py-3 pr-0 pl-8 text-right font-semibold"}
            "Score"]]]
         [:tbody
          [:tr
           {:class "border-b border-gray-100"}
           [:td
            {:class "max-w-0 px-0 py-5 align-top"}
            [:div
             {:class "truncate font-medium text-gray-900"}
             "Logo redesign"]
            [:div
             {:class "truncate text-gray-500"}
             "New logo and digital asset playbook."]]
           [:td
            {:class
             "hidden py-5 pr-0 pl-8 text-right align-top text-gray-700 tabular-nums sm:table-cell"}
            "5"]
           [:td
            {:class
             "py-5 pr-0 pl-8 text-right align-top text-gray-700 tabular-nums"}
            "11"]]]
         [:tfoot
          [:tr
           [:th
            {:scope "row",
             :class "pt-4 font-semibold text-gray-900 sm:hidden"}
            "Total"]
           [:th
            {:scope "row",
             :colspan "2",
             :class
             "hidden pt-4 text-right font-semibold text-gray-900 sm:table-cell"}
            "Total"]
           [:td
            {:class
             "pt-4 pr-0 pb-0 pl-8 text-right font-semibold text-gray-900 tabular-nums"}
            "15.5"]]]]]
       [:div
        {:class "lg:col-start-3"}
        [:h2 {:class "text-sm/6 font-semibold text-gray-900"} "Versions"]
        [:ul
         {:role "list", :class "mt-6 space-y-6"}
         [:li
          {:class "relative flex gap-x-4"}
          [:div
           {:class
            "absolute top-0 -bottom-6 left-0 flex w-6 justify-center"}
           [:div {:class "w-px bg-gray-200"}]]
          [:div
           {:class
            "relative flex size-6 flex-none items-center justify-center bg-white"}
           [:div
            {:class
             "size-1.5 rounded-full bg-gray-100 ring-1 ring-gray-300"}]]
          [:p
           {:class "flex-auto py-0.5 text-xs/5 text-gray-500"}
           [:span {:class "font-medium text-gray-900"} "Chelsea Hagon"]
           "created the invoice."]
          [:time
           {:datetime "2023-01-23T10:32",
            :class "flex-none py-0.5 text-xs/5 text-gray-500"}
           "7d ago"]]
         [:li
          {:class "relative flex gap-x-4"}
          [:div
           {:class
            "absolute top-0 -bottom-6 left-0 flex w-6 justify-center"}
           [:div {:class "w-px bg-gray-200"}]]
          [:div
           {:class
            "relative flex size-6 flex-none items-center justify-center bg-white"}
           [:div
            {:class
             "size-1.5 rounded-full bg-gray-100 ring-1 ring-gray-300"}]]
          [:p
           {:class "flex-auto py-0.5 text-xs/5 text-gray-500"}
           [:span {:class "font-medium text-gray-900"} "Chelsea Hagon"]
           "edited the invoice."]
          [:time
           {:datetime "2023-01-23T11:03",
            :class "flex-none py-0.5 text-xs/5 text-gray-500"}
           "6d ago"]]
         [:li
          {:class "relative flex gap-x-4"}
          [:div
           {:class
            "absolute top-0 -bottom-6 left-0 flex w-6 justify-center"}
           [:div {:class "w-px bg-gray-200"}]]
          [:div
           {:class
            "relative flex size-6 flex-none items-center justify-center bg-white"}
           [:div
            {:class
             "size-1.5 rounded-full bg-gray-100 ring-1 ring-gray-300"}]]
          [:p
           {:class "flex-auto py-0.5 text-xs/5 text-gray-500"}
           [:span {:class "font-medium text-gray-900"} "Chelsea Hagon"]
           "sent the invoice."]
          [:time
           {:datetime "2023-01-23T11:24",
            :class "flex-none py-0.5 text-xs/5 text-gray-500"}
           "6d ago"]]
         [:li
          {:class "relative flex gap-x-4"}
          [:div
           {:class
            "absolute top-0 -bottom-6 left-0 flex w-6 justify-center"}
           [:div {:class "w-px bg-gray-200"}]]
          [:img
           {:src
            "https://images.unsplash.com/photo-1550525811-e5869dd03032?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80",
            :alt "",
            :class
            "relative mt-3 size-6 flex-none rounded-full bg-gray-50"}]
          [:div
           {:class
            "flex-auto rounded-md p-3 ring-1 ring-gray-200 ring-inset"}
           [:div
            {:class "flex justify-between gap-x-4"}
            [:div
             {:class "py-0.5 text-xs/5 text-gray-500"}
             [:span {:class "font-medium text-gray-900"} "Chelsea Hagon"]
             "commented"]
            [:time
             {:datetime "2023-01-23T15:56",
              :class "flex-none py-0.5 text-xs/5 text-gray-500"}
             "3d ago"]]
           [:p
            {:class "text-sm/6 text-gray-500"}
            "Called client, they reassured me the invoice would be paid by the 25th."]]]
         [:li
          {:class "relative flex gap-x-4"}
          [:div
           {:class
            "absolute top-0 -bottom-6 left-0 flex w-6 justify-center"}
           [:div {:class "w-px bg-gray-200"}]]
          [:div
           {:class
            "relative flex size-6 flex-none items-center justify-center bg-white"}
           [:div
            {:class
             "size-1.5 rounded-full bg-gray-100 ring-1 ring-gray-300"}]]
          [:p
           {:class "flex-auto py-0.5 text-xs/5 text-gray-500"}
           [:span {:class "font-medium text-gray-900"} "Alex Curren"]
           "viewed the invoice."]
          [:time
           {:datetime "2023-01-24T09:12",
            :class "flex-none py-0.5 text-xs/5 text-gray-500"}
           "2d ago"]]
         [:li
          {:class "relative flex gap-x-4"}
          [:div
           {:class "absolute top-0 left-0 flex h-6 w-6 justify-center"}
           [:div {:class "w-px bg-gray-200"}]]
          [:div
           {:class
            "relative flex size-6 flex-none items-center justify-center bg-white"}
           [:svg
            {:viewBox "0 0 24 24",
             :fill "currentColor",
             :data-slot "icon",
             :aria-hidden "true",
             :class "size-6 text-indigo-600"}
            [:path
             {:d
              "M2.25 12c0-5.385 4.365-9.75 9.75-9.75s9.75 4.365 9.75 9.75-4.365 9.75-9.75 9.75S2.25 17.385 2.25 12Zm13.36-1.814a.75.75 0 1 0-1.22-.872l-3.236 4.53L9.53 12.22a.75.75 0 0 0-1.06 1.06l2.25 2.25a.75.75 0 0 0 1.14-.094l3.75-5.25Z",
              :clip-rule "evenodd",
              :fill-rule "evenodd"}]]]
          [:p
           {:class "flex-auto py-0.5 text-xs/5 text-gray-500"}
           [:span {:class "font-medium text-gray-900"} "Alex Curren"]
           "paid the invoice."]
          [:time
           {:datetime "2023-01-24T09:20",
            :class "flex-none py-0.5 text-xs/5 text-gray-500"}
           "1d ago"]]]
        (comment "New comment form")
        [:div
         {:class "mt-6 flex gap-x-3"}
         [:img
          {:src
           "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?ixlib=rb-1.2.1&ixid=eyJhcHBfaWQiOjEyMDd9&auto=format&fit=facearea&facepad=2&w=256&h=256&q=80",
           :alt "",
           :class "size-6 flex-none rounded-full bg-gray-50"}]
         [:form
          {:action "#", :class "relative flex-auto"}
          [:div
           {:class
            "overflow-hidden rounded-lg pb-12 outline-1 -outline-offset-1 outline-gray-300 focus-within:outline-2 focus-within:-outline-offset-2 focus-within:outline-indigo-600"}
           [:label {:for "comment", :class "sr-only"} "Add your comment"]
           [:textarea
            {:id "comment",
             :name "comment",
             :rows "2",
             :placeholder "Add your comment...",
             :class
             "block w-full resize-none bg-transparent px-3 py-1.5 text-base text-gray-900 placeholder:text-gray-400 focus:outline-none sm:text-sm/6"}]]
          [:div
           {:class
            "absolute inset-x-0 bottom-0 flex justify-between py-2 pr-2 pl-3"}
           [:div
            {:class "flex items-center space-x-5"}
            [:div
             {:class "flex items-center"}
             [:button
              {:type "button",
               :class
               "-m-2.5 flex size-10 items-center justify-center rounded-full text-gray-400 hover:text-gray-500"}
              [:svg
               {:viewBox "0 0 20 20",
                :fill "currentColor",
                :data-slot "icon",
                :aria-hidden "true",
                :class "size-5"}
               [:path
                {:d
                 "M15.621 4.379a3 3 0 0 0-4.242 0l-7 7a3 3 0 0 0 4.241 4.243h.001l.497-.5a.75.75 0 0 1 1.064 1.057l-.498.501-.002.002a4.5 4.5 0 0 1-6.364-6.364l7-7a4.5 4.5 0 0 1 6.368 6.36l-3.455 3.553A2.625 2.625 0 1 1 9.52 9.52l3.45-3.451a.75.75 0 1 1 1.061 1.06l-3.45 3.451a1.125 1.125 0 0 0 1.587 1.595l3.454-3.553a3 3 0 0 0 0-4.242Z",
                 :clip-rule "evenodd",
                 :fill-rule "evenodd"}]]
              [:span {:class "sr-only"} "Attach a file"]]]
            [:div
             {:class "flex items-center"}
             [:el-select
              {:name "selected", :value ""}
              [:button
               {:type "button",
                :aria-label "Your mood",
                :class
                "relative -m-2.5 flex size-10 items-center justify-center rounded-full text-gray-400 hover:text-gray-500"}
               [:el-selectedcontent
                {:class "flex items-center justify-center"}
                [:svg
                 {:viewBox "0 0 20 20",
                  :fill "currentColor",
                  :data-slot "icon",
                  :aria-hidden "true",
                  :class "size-5 shrink-0"}
                 [:path
                  {:d
                   "M10 18a8 8 0 1 0 0-16 8 8 0 0 0 0 16Zm3.536-4.464a.75.75 0 1 0-1.061-1.061 3.5 3.5 0 0 1-4.95 0 .75.75 0 0 0-1.06 1.06 5 5 0 0 0 7.07 0ZM9 8.5c0 .828-.448 1.5-1 1.5s-1-.672-1-1.5S7.448 7 8 7s1 .672 1 1.5Zm3 1.5c.552 0 1-.672 1-1.5S12.552 7 12 7s-1 .672-1 1.5.448 1.5 1 1.5Z",
                   :clip-rule "evenodd",
                   :fill-rule "evenodd"}]]
                [:span {:class "sr-only"} "Add your mood"]]]
              [:el-options
               {:anchor "top start",
                :popover "",
                :class
                "-ml-3.5 w-60 rounded-lg bg-white py-3 text-base shadow-sm ring-1 ring-black/5 focus:outline-hidden data-leave:transition data-leave:transition-discrete data-leave:duration-100 data-leave:ease-in data-closed:data-leave:opacity-0 sm:ml-2.5 sm:w-64 sm:text-sm"}
               [:el-option
                {:value "excited",
                 :class
                 "relative block cursor-default bg-white px-3 py-2 select-none focus:bg-gray-100 focus:outline-hidden"}
                [:div
                 {:class "flex items-center"}
                 [:div
                  {:class
                   "flex size-8 items-center justify-center rounded-full bg-red-500"}
                  [:svg
                   {:viewBox "0 0 20 20",
                    :fill "currentColor",
                    :data-slot "icon",
                    :aria-hidden "true",
                    :class "size-5 shrink-0 text-white"}
                   [:path
                    {:d
                     "M13.5 4.938a7 7 0 1 1-9.006 1.737c.202-.257.59-.218.793.039.278.352.594.672.943.954.332.269.786-.049.773-.476a5.977 5.977 0 0 1 .572-2.759 6.026 6.026 0 0 1 2.486-2.665c.247-.14.55-.016.677.238A6.967 6.967 0 0 0 13.5 4.938ZM14 12a4 4 0 0 1-4 4c-1.913 0-3.52-1.398-3.91-3.182-.093-.429.44-.643.814-.413a4.043 4.043 0 0 0 1.601.564c.303.038.531-.24.51-.544a5.975 5.975 0 0 1 1.315-4.192.447.447 0 0 1 .431-.16A4.001 4.001 0 0 1 14 12Z",
                     :clip-rule "evenodd",
                     :fill-rule "evenodd"}]]]
                 [:span
                  {:class
                   "ml-3 block truncate font-medium in-[el-selectedcontent]:hidden"}
                  "Excited"]]]
               [:el-option
                {:value "loved",
                 :class
                 "relative block cursor-default bg-white px-3 py-2 select-none focus:bg-gray-100 focus:outline-hidden"}
                [:div
                 {:class "flex items-center"}
                 [:div
                  {:class
                   "flex size-8 items-center justify-center rounded-full bg-pink-400"}
                  [:svg
                   {:viewBox "0 0 20 20",
                    :fill "currentColor",
                    :data-slot "icon",
                    :aria-hidden "true",
                    :class "size-5 shrink-0 text-white"}
                   [:path
                    {:d
                     "m9.653 16.915-.005-.003-.019-.01a20.759 20.759 0 0 1-1.162-.682 22.045 22.045 0 0 1-2.582-1.9C4.045 12.733 2 10.352 2 7.5a4.5 4.5 0 0 1 8-2.828A4.5 4.5 0 0 1 18 7.5c0 2.852-2.044 5.233-3.885 6.82a22.049 22.049 0 0 1-3.744 2.582l-.019.01-.005.003h-.002a.739.739 0 0 1-.69.001l-.002-.001Z"}]]]
                 [:span
                  {:class
                   "ml-3 block truncate font-medium in-[el-selectedcontent]:hidden"}
                  "Loved"]]]
               [:el-option
                {:value "happy",
                 :class
                 "relative block cursor-default bg-white px-3 py-2 select-none focus:bg-gray-100 focus:outline-hidden"}
                [:div
                 {:class "flex items-center"}
                 [:div
                  {:class
                   "flex size-8 items-center justify-center rounded-full bg-green-400"}
                  [:svg
                   {:viewBox "0 0 20 20",
                    :fill "currentColor",
                    :data-slot "icon",
                    :aria-hidden "true",
                    :class "size-5 shrink-0 text-white"}
                   [:path
                    {:d
                     "M10 18a8 8 0 1 0 0-16 8 8 0 0 0 0 16Zm3.536-4.464a.75.75 0 1 0-1.061-1.061 3.5 3.5 0 0 1-4.95 0 .75.75 0 0 0-1.06 1.06 5 5 0 0 0 7.07 0ZM9 8.5c0 .828-.448 1.5-1 1.5s-1-.672-1-1.5S7.448 7 8 7s1 .672 1 1.5Zm3 1.5c.552 0 1-.672 1-1.5S12.552 7 12 7s-1 .672-1 1.5.448 1.5 1 1.5Z",
                     :clip-rule "evenodd",
                     :fill-rule "evenodd"}]]]
                 [:span
                  {:class
                   "ml-3 block truncate font-medium in-[el-selectedcontent]:hidden"}
                  "Happy"]]]
               [:el-option
                {:value "sad",
                 :class
                 "relative block cursor-default bg-white px-3 py-2 select-none focus:bg-gray-100 focus:outline-hidden"}
                [:div
                 {:class "flex items-center"}
                 [:div
                  {:class
                   "flex size-8 items-center justify-center rounded-full bg-yellow-400"}
                  [:svg
                   {:viewBox "0 0 20 20",
                    :fill "currentColor",
                    :data-slot "icon",
                    :aria-hidden "true",
                    :class "size-5 shrink-0 text-white"}
                   [:path
                    {:d
                     "M10 18a8 8 0 1 0 0-16 8 8 0 0 0 0 16Zm-3.536-3.475a.75.75 0 0 0 1.061 0 3.5 3.5 0 0 1 4.95 0 .75.75 0 1 0 1.06-1.06 5 5 0 0 0-7.07 0 .75.75 0 0 0 0 1.06ZM9 8.5c0 .828-.448 1.5-1 1.5s-1-.672-1-1.5S7.448 7 8 7s1 .672 1 1.5Zm3 1.5c.552 0 1-.672 1-1.5S12.552 7 12 7s-1 .672-1 1.5.448 1.5 1 1.5Z",
                     :clip-rule "evenodd",
                     :fill-rule "evenodd"}]]]
                 [:span
                  {:class
                   "ml-3 block truncate font-medium in-[el-selectedcontent]:hidden"}
                  "Sad"]]]
               [:el-option
                {:value "thumbsy",
                 :class
                 "relative block cursor-default bg-white px-3 py-2 select-none focus:bg-gray-100 focus:outline-hidden"}
                [:div
                 {:class "flex items-center"}
                 [:div
                  {:class
                   "flex size-8 items-center justify-center rounded-full bg-blue-500"}
                  [:svg
                   {:viewBox "0 0 20 20",
                    :fill "currentColor",
                    :data-slot "icon",
                    :aria-hidden "true",
                    :class "size-5 shrink-0 text-white"}
                   [:path
                    {:d
                     "M1 8.25a1.25 1.25 0 1 1 2.5 0v7.5a1.25 1.25 0 1 1-2.5 0v-7.5ZM11 3V1.7c0-.268.14-.526.395-.607A2 2 0 0 1 14 3c0 .995-.182 1.948-.514 2.826-.204.54.166 1.174.744 1.174h2.52c1.243 0 2.261 1.01 2.146 2.247a23.864 23.864 0 0 1-1.341 5.974C17.153 16.323 16.072 17 14.9 17h-3.192a3 3 0 0 1-1.341-.317l-2.734-1.366A3 3 0 0 0 6.292 15H5V8h.963c.685 0 1.258-.483 1.612-1.068a4.011 4.011 0 0 1 2.166-1.73c.432-.143.853-.386 1.011-.814.16-.432.248-.9.248-1.388Z"}]]]
                 [:span
                  {:class
                   "ml-3 block truncate font-medium in-[el-selectedcontent]:hidden"}
                  "Thumbsy"]]]
               [:el-option
                {:value "",
                 :class
                 "relative block cursor-default bg-white px-3 py-2 select-none focus:bg-gray-100 focus:outline-hidden"}
                [:div
                 {:class "flex items-center"}
                 [:div
                  {:class
                   "flex size-8 items-center justify-center rounded-full bg-transparent in-[el-selectedcontent]:hidden"}
                  [:svg
                   {:viewBox "0 0 20 20",
                    :fill "currentColor",
                    :data-slot "icon",
                    :aria-hidden "true",
                    :class "size-5 shrink-0 text-gray-400"}
                   [:path
                    {:d
                     "M6.28 5.22a.75.75 0 0 0-1.06 1.06L8.94 10l-3.72 3.72a.75.75 0 1 0 1.06 1.06L10 11.06l3.72 3.72a.75.75 0 1 0 1.06-1.06L11.06 10l3.72-3.72a.75.75 0 0 0-1.06-1.06L10 8.94 6.28 5.22Z"}]]]
                 [:span
                  {:class "hidden in-[el-selectedcontent]:inline"}
                  [:svg
                   {:viewBox "0 0 20 20",
                    :fill "currentColor",
                    :data-slot "icon",
                    :aria-hidden "true",
                    :class "size-5 shrink-0"}
                   [:path
                    {:d
                     "M10 18a8 8 0 1 0 0-16 8 8 0 0 0 0 16Zm3.536-4.464a.75.75 0 1 0-1.061-1.061 3.5 3.5 0 0 1-4.95 0 .75.75 0 0 0-1.06 1.06 5 5 0 0 0 7.07 0ZM9 8.5c0 .828-.448 1.5-1 1.5s-1-.672-1-1.5S7.448 7 8 7s1 .672 1 1.5Zm3 1.5c.552 0 1-.672 1-1.5S12.552 7 12 7s-1 .672-1 1.5.448 1.5 1 1.5Z",
                     :clip-rule "evenodd",
                     :fill-rule "evenodd"}]]
                  [:span {:class "sr-only"} "Add your mood"]]
                 [:span
                  {:class
                   "ml-3 block truncate font-medium in-[el-selectedcontent]:hidden"}
                  "I feel nothing"]]]]]]]
           [:button
            {:type "submit",
             :class
             "rounded-md bg-white px-2.5 py-1.5 text-sm font-semibold text-gray-900 shadow-xs ring-1 ring-gray-300 ring-inset hover:bg-gray-50"}
            "Comment"]]]]]]]
     [:pre (with-out-str (cljs.pprint/pprint assertion))]]))


(defmethod common/main-view "EvidenceStrengthAssertion" [assertion]
  (case (get-in assertion [:subject :__typename])
    "GeneValidityProposition" (validity-assertion assertion)
    "GeneticConditionMechanismProposition" (mechanism-assertion assertion)))

#_(defmethod common/main-view "EvidenceStrengthAssertion" [assertion]
    [:div "EvidenceStrengthAssertion"
     [:pre (with-out-str (cljs.pprint/pprint assertion))]])
