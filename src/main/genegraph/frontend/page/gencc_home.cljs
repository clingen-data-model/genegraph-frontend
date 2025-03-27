(ns genegraph.frontend.page.gencc-home
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.display :as display]
            [genegraph.frontend.user :as user]
            [genegraph.frontend.icon :as icon]
            [genegraph.frontend.display.annotation :as annotation]
            [genegraph.frontend.queries.assertion :as assertion]
            [clojure.string :as s]))


#_(defn home []
  [:div "gencc home"])

[:span
 {:class "isolate inline-flex rounded-md shadow-xs"}
 [:button
  {:type "button",
   :class
   "relative inline-flex items-center rounded-l-md bg-white px-3 py-2 text-sm font-semibold text-gray-900 ring-1 ring-gray-300 ring-inset hover:bg-gray-50 focus:z-10"}
  "Years"]
 [:button
  {:type "button",
   :class
   "relative -ml-px inline-flex items-center bg-white px-3 py-2 text-sm font-semibold text-gray-900 ring-1 ring-gray-300 ring-inset hover:bg-gray-50 focus:z-10"}
  "Months"]
 [:button
  {:type "button",
   :class
   "relative -ml-px inline-flex items-center rounded-r-md bg-white px-3 py-2 text-sm font-semibold text-gray-900 ring-1 ring-gray-300 ring-inset hover:bg-gray-50 focus:z-10"}
  "Days"]]

(def main-links
  [{:href "https://thegencc.org/",
    :label "Home"}
   {:href "https://search.thegencc.org",
    :label "GenCC Database"}
   {:href "https://thegencc.org/about.html",
    :label "About"}
   {:href "https://thegencc.org/members.html",
    :label "Members"}
   {:href "https://thegencc.org/articles.html",
    :label "News & Articles"}])

(defn home []
  [:header
   [:div
    {:class "absolute text-shadow bg-black flex flex-col items-center"}
    [:div
     {:class "pt-14"}
     [:a
      {:href "https://thegencc.org/"}
      [:img
       {:alt "The GenCC",
        :src "https://thegencc.org/assets/gencc-logo-white.png",
        :class "img-fluid"}]]]
    [:div
     [:div
      {:class "pt-10 font-light text-xl text-white"}
      [:h3 "A global effort to harmonize gene-level resources."]]]
    [:div
     {:class "text-white pt-4"}
     [:span
      {:class "isolate inline-flex rounded-md"}
      (for [l main-links]
        ^{:key l}
        [:button
         {:href (:href l)
          :type "button"
          :class "relative -ml-px inline-flex items-center bg-black px-3 py-2 text-sm font-semibold ring-1 ring-gray-300 ring-inset hover:bg-white hover:text-gray-900 focus:z-10"}
         (:label l)])]]
    [:div
     {:class ""}
     [:img
      {:src "https://thegencc.org/assets/header-banner.jpg",
       :class "img-fluid"}]]]
   [:div
    {:class "section-ico section-icon-bottom"}
    [:span {:class "h2"} [:i {:class "far fa-arrow-alt-circle-down"}]]]])
