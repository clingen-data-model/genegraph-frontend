(ns genegraph.frontend.page.downloads
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.icon :as icon]
            [clojure.string :as s]))

(def download-set
  [{:title "Gene Validity -- Latest Versions"
    :description "Includes only the most recent version of each curation."
    :files
    [{:filename "gene-validity-jsonld-latest.tar.gz"
      :url "https://storage.googleapis.com/genegraph-public/gene-validity-jsonld-latest.tar.gz"
      :format "json-ld"}
     {:filename "gene-validity-nt-latest.tar.gz"
      :url "https://storage.googleapis.com/genegraph-public/gene-validity-nt-latest.tar.gz"
      :format "n-triples"}]}
   {:title "Gene Validity -- All Versions"
    :description "Includes every published version for all curations."
    :files
    [{:filename "gene-validity-jsonld-all.tar.gz"
      :url "https://storage.googleapis.com/genegraph-public/gene-validity-jsonld-all.tar.gz"
      :format "json-ld"}
     {:filename "gene-validity-nt-all.tar.gz"
      :url
      "https://storage.googleapis.com/genegraph-public/gene-validity-nt-all.tar.gz"
      :format "n-triples"}]}])

(defn download-list [files]
  [:ul
   {:role "list",
    :class "divide-y divide-gray-100 rounded-md border border-gray-200 mt-6"}
   (for [f files]
     ^{:key f}
     [:li
      {:class "flex items-center justify-between py-4 pr-5 pl-4 text-sm/6"}
      [:div
       {:class "flex w-0 flex-1 items-center"}
       icon/arrow-down-tray
       [:div
        {:class "ml-4 flex min-w-0 flex-1 gap-2"}
        [:span
         {:class "truncate font-medium"}
         (:filename f)]
        [:span {:class "shrink-0 text-gray-400"} (:format f)]]]
      [:div
       {:class "ml-4 shrink-0"}
       [:a
        {:href (:url f)
         :class "font-medium text-sky-600 hover:text-sky-500 pr-2"}
        "Download"]]])])

(defn downloads []
  [:div
   {:class "px-12 py-12"}
   [:div
    {:class "min-w-0 flex-1"}
    [:h2
     {:class
      "text-2xl/7 font-bold text-gray-900 sm:truncate sm:text-3xl sm:tracking-tight"}
     "Downloads"]
    [:p
     {:class "mt-4 max-w-4xl text-sm text-gray-700"}
     "All downloads are generated every six hours using the most recent published data from ClinGen. They are made available in JSON-LD and RDF n-triples formats, compressed into an archive package."]]
   [:ul
    {:role "list", :class "py-10"}
    (for [s download-set]
      ^{:key s}
      [:li {:class "py-4"}
       [:div
        {:class "pb-5"}
        [:h3 {:class "text-base font-semibold text-gray-900"}
         (:title s)]
        [:p
         {:class "mt-2 max-w-4xl text-sm text-gray-500"}
         (:description s)]
        (download-list (:files s))]])]])

