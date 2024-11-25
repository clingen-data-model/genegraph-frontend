(ns genegraph.frontend.page.annotations
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]))

(defn submitter [i]
  (->> (:contributions i)
       (map :agent)
       first))

(def annotation-query
  "{
  assertionAnnotation {
    __typename
    date
    curie
    annotations {
      iri
      description
      classification {
        curie
      }
      evidence {
        curie
        subject {
          ...on GeneticConditionMechanismProposition {
            feature {
              label
            }
          }
        }
      }
      contributions {
        agent {
          curie
        }
        role {
          curie
          iri
        }
        date
      }
    }
    contributions {
      agent {
        curie
        label
      }
      role {
        curie
        label
      }
      date
    }
    subject {
      __typename
      curie
      ... on VariantPathogenicityProposition {
        variant {
          __typename
          curie
          label
          ... on CanonicalVariant {
            copyChange {
              label
              curie
            }
            includedVariants {
              ... on CopyNumberChange {
                copyChange {
                  label
                  curie
                }
              }
            }
          }
        }
      }
    }
    classification {
      curie
      label
    }
  }
}
"
)

(re-frame/reg-event-db
 ::recieve-annotation-list
 (fn [db [_ result]]
   (js/console.log "Recieved annotation list")
   (assoc db
          ::annotated-assertions
          (get-in result [:response :data :assertionAnnotation]))))

(re-frame/reg-event-fx
 ::request-annotation-list
 (fn [_ _]
   (js/console.log "Requesting annotation list")
   {:fx [[:dispatch
          [::re-graph/query
           {:id ::annotation-list
            :query annotation-query
            :variables {}
            :callback [::recieve-annotation-list]}]]]}))

(re-frame/reg-sub
 ::annotated-assertions
 :-> ::annotated-assertions)

(defn authorship [a]
  (first
   (filter #(= "CG:Author"
               (get-in % [:role :curie]))
           (:contributions a))))

(defn evidence [e]
  ^{:key (:curie e)}
  [:div (get-in e [:subject :feature :label])])

(defn annotation [a]
  (let [author (authorship a)]
    ^{:key (:iri a)}
    [:div
     [:div {:class "flex gap-8"}
      [:div (get-in a [:classification :curie])]
      [:div (get-in author [:agent :curie])]
      [:div (subs (:date author) 0 10)]]
     [:div {:class "flex gap-4"}
      (for [e (:evidence a)]
        (evidence e))]]))

(defn assertion [i]
  [:div
   {:class "flex gap-x-6"}
   [:div
    {:class "flex w-1/2 gap-x-4"}
    [:div
     {:class "min-w-0 flex-auto"}
     [:p
      {:class "text-sm font-semibold leading-6 text-gray-900"}
      [:a {:href (:iri i) :target "_blank"}
       (get-in i [:subject :variant :label])]]
     [:p
      {:class "text-sm leading-6 text-gray-500"}
      (get-in i [:classification :label])]
     [:div
      {:class "mt-1 truncate text-xs leading-5 text-gray-500"}
      [:span
       {:class "flex gap-8"}
       [:div (:date i)]
       [:div (:label (submitter i))]]]]]
   ;; gene grid
   [:div
    {:class "w-1/4"}
    [:div
     {:class "text-gray-700 text-sm font-light"}
     (for [a (:annotations i)]
       (annotation a))]
    
    [:div
     {:class #_"grid grid-cols-1 gap-2 py-1 sm:grid-cols-4"
      "flex gap-2 py-1 flex-wrap"}]]])

(defn show-annotations []
  (let [assertions @(re-frame/subscribe [::annotated-assertions])]
    [:div
     {:class "py-10"}
     [:header
      [:div
       {:class "mx-auto max-w-7xl px-4 sm:px-6 lg:px-8"}
       [:h1
        {:class
         "text-3xl font-bold leading-tight tracking-tight text-gray-900"}
        "Prior Annotations"]]]
     [:main
      [:div
       {:class "mx-auto max-w-7xl px-4 py-8 sm:px-6 lg:px-8"}
       [:ul
        {:role "list", :divide "class-y divide-gray-100"}
        (doall
         (for [i (->> assertions
                      (filter #(get-in % [:classification :label]))
                      (sort-by #(get-in % [:subject :variant :label]))
                      (take 50))]
           ^{:key i}
           [:li
            {:class "py-8"}
            (assertion i)]))]]
      #_[:div [:pre (with-out-str (cljs.pprint/pprint @(re-frame/subscribe [::annotated-assertions])))]]]]))


(defn annotations []
  (show-annotations))
