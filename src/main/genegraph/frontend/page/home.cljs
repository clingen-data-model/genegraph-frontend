(ns genegraph.frontend.page.home
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.icon :as icon]
            [clojure.string :as s]))

(def home-sections
  [{:title "Download data"
    :description "Full historical archive, or just the latest. JSON or RDF. Just a summary or all the facts."
    :icon icon/arrow-down-tray
    :route :routes/downloads
    :link-text "To downloads"}
   {:title "Read documentation"
    :description "Complete documentation of all classes, attributes, properties, and value sets."
    :icon icon/book-open
    :route :routes/documentation
    :link-text "To documentation"}])


(defn home []
  [:div
   {:class "bg-white py-24 sm:py-32"}
   [:div
    {:class "mx-auto max-w-7xl px-16"}
    [:div
     {:class "mx-auto max-w-2xl lg:mx-0"}
     [:h2
      {:class
       "text-4xl font-semibold tracking-tight text-pretty text-gray-900 sm:text-5xl"}
      [:span
       {:class "text-sky-700"}
       "Genegraph:"]
      " computable ClinGen evidence"]
     [:p
      {:class "mt-6 text-lg/8 text-gray-600"}
      "Download the complete ClinGen Gene Validity data set, including the full curation history, as well as rich, detailed evidence. Leverages the SEPIO and GA4GH GKS data models."]]
    [:div
     {:class "mx-auto mt-16 max-w-2xl sm:mt-20 lg:mt-24 lg:max-w-none"}
     [:dl
      {:class
       "grid max-w-xl grid-cols-1 gap-x-8 gap-y-16 lg:max-w-none lg:grid-cols-3"}
      (for [s home-sections]
        ^{:key s}
        [:div
         {:class "flex flex-col"}
         [:dt
          {:class "text-base/7 font-semibold text-gray-900"}
          [:div
           {:class
            "mb-6 flex size-10 items-center justify-center rounded-lg bg-sky-700"}
           [:div {:class "size-6 text-sky-100"}
            (:icon s)]]
          (:title s)]
         [:dd
          {:class "mt-1 flex flex-auto flex-col text-base/7 text-gray-600"}
          [:p
           {:class "flex-auto"}
           (:description s)]
          [:p
           {:class "mt-6"}
           [:a
            {:href (rfe/href (:route s))
             :class "text-sm/6 font-semibold text-sky-500"}
            (:link-text s)
            [:span {:aria-hidden "true"} "→"]]]]])]]]])
