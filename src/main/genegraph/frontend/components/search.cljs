(ns genegraph.frontend.components.search
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.user :as user]
            [genegraph.frontend.queries :as queries]
            [genegraph.frontend.icon :as icon]
            [clojure.string :as str]))

(def text-search-query
"
query ($symbol: String) {
  sequenceFeatureQuery(symbol: $symbol) {
    __typename
    iri
    label
    assertions {
      evidenceStrength {
        curie
      }
      subject {
        __typename
        type {
          curie
          label
        }
      }
      contributions {
        role {
          curie
        }
        date
        agent {
          curie
          label
        }
      }
    }
  }
}
")

(re-frame/reg-event-db
 ::recieve-text-search-result
 (fn [db [_ result]]
   (js/console.log "recieved result")
   (assoc db
          :current-resource
          (get-in result
                  [:response
                   :data
                   :sequenceFeatureQuery]))))

(re-frame/reg-event-fx
 ::text-search
 (fn [{:keys [db]} [_ text]]
   (js/console.log "searching for " text)
   {:fx [[:dispatch
          [::re-graph/query
           {:id ::query
            :query text-search-query
            :variables {:symbol (str/upper-case text)}   ; change 
            :callback [::recieve-text-search-result]}]]]}))

(defn text-search-div []
  [:div
   {:class "mt-2 grid grid-cols-1"}
   [:input
    {:type "text",
     :name "search-text",
     :id "search-text",
     :on-key-down (fn [e]
                    (case (.-key e)
                      "Enter" (re-frame/dispatch
                               [::text-search
                                (-> e .-target .-value)])
                      nil))
     :class
     "col-start-1 row-start-1 block w-full rounded-md bg-white py-1.5 pr-3 pl-10 text-base text-gray-900 outline-1 -outline-offset-1 outline-gray-300 placeholder:text-gray-400 focus:outline-2 focus:-outline-offset-2 focus:outline-indigo-600 sm:pl-9 sm:text-sm/6",
     :placeholder "gene symbol"}]
   [:div
    {:class
     "pointer-events-none col-start-1 row-start-1 ml-3 size-5 self-center text-gray-400 sm:size-4",
     :viewBox "0 0 16 16",
     :fill "currentColor",
     :aria-hidden "true",
     :data-slot "icon"}
    icon/magnifying-glass]])

(defn search-div []
  [:div
   {:class "flex"}
   (text-search-div)])
