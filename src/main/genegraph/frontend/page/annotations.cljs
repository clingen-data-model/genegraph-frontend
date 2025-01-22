(ns genegraph.frontend.page.annotations
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]))

(defn submitter [i]
  (->> (:contributions i)
       (map :agent)
       first))

(def document-text
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :fill "none",
    :viewBox "0 0 24 24",
    :stroke-width "1.5",
    :stroke "currentColor",
    :class "size-6"}
   [:path
    {:stroke-linecap "round",
     :stroke-linejoin "round",
     :d
     "M19.5 14.25v-2.625a3.375 3.375 0 0 0-3.375-3.375h-1.5A1.125 1.125 0 0 1 13.5 7.125v-1.5a3.375 3.375 0 0 0-3.375-3.375H8.25m0 12.75h7.5m-7.5 3H12M10.5 2.25H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 0 0-9-9Z"}]])

(def wrench
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :fill "none",
    :viewBox "0 0 24 24",
    :stroke-width "1.5",
    :stroke "currentColor",
    :class "size-6"}
   [:path
    {:stroke-linecap "round",
     :stroke-linejoin "round",
     :d
     "M21.75 6.75a4.5 4.5 0 0 1-4.884 4.484c-1.076-.091-2.264.071-2.95.904l-7.152 8.684a2.548 2.548 0 1 1-3.586-3.586l8.684-7.152c.833-.686.995-1.874.904-2.95a4.5 4.5 0 0 1 6.336-4.486l-3.276 3.276a3.004 3.004 0 0 0 2.25 2.25l3.276-3.276c.256.565.398 1.192.398 1.852Z"}]
   [:path
    {:stroke-linecap "round",
     :stroke-linejoin "round",
     :d "M4.867 19.125h.008v.008h-.008v-.008Z"}]])

(def hand-thumb-down
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :fill "none",
    :viewBox "0 0 24 24",
    :stroke-width "1.5",
    :stroke "currentColor",
    :class "size-6"}
   [:path
    {:stroke-linecap "round",
     :stroke-linejoin "round",
     :d
     "M7.498 15.25H4.372c-1.026 0-1.945-.694-2.054-1.715a12.137 12.137 0 0 1-.068-1.285c0-2.848.992-5.464 2.649-7.521C5.287 4.247 5.886 4 6.504 4h4.016a4.5 4.5 0 0 1 1.423.23l3.114 1.04a4.5 4.5 0 0 0 1.423.23h1.294M7.498 15.25c.618 0 .991.724.725 1.282A7.471 7.471 0 0 0 7.5 19.75 2.25 2.25 0 0 0 9.75 22a.75.75 0 0 0 .75-.75v-.633c0-.573.11-1.14.322-1.672.304-.76.93-1.33 1.653-1.715a9.04 9.04 0 0 0 2.86-2.4c.498-.634 1.226-1.08 2.032-1.08h.384m-10.253 1.5H9.7m8.075-9.75c.01.05.027.1.05.148.593 1.2.925 2.55.925 3.977 0 1.487-.36 2.89-.999 4.125m.023-8.25c-.076-.365.183-.75.575-.75h.908c.889 0 1.713.518 1.972 1.368.339 1.11.521 2.287.521 3.507 0 1.553-.295 3.036-.831 4.398-.306.774-1.086 1.227-1.918 1.227h-1.053c-.472 0-.745-.556-.5-.96a8.95 8.95 0 0 0 .303-.54"}]])

(def hand-thumb-up
  [:svg
 {:xmlns "http://www.w3.org/2000/svg",
  :fill "none",
  :viewBox "0 0 24 24",
  :stroke-width "1.5",
  :stroke "currentColor",
  :class "size-6"}
 [:path
  {:stroke-linecap "round",
   :stroke-linejoin "round",
   :d
   "M6.633 10.25c.806 0 1.533-.446 2.031-1.08a9.041 9.041 0 0 1 2.861-2.4c.723-.384 1.35-.956 1.653-1.715a4.498 4.498 0 0 0 .322-1.672V2.75a.75.75 0 0 1 .75-.75 2.25 2.25 0 0 1 2.25 2.25c0 1.152-.26 2.243-.723 3.218-.266.558.107 1.282.725 1.282m0 0h3.126c1.026 0 1.945.694 2.054 1.715.045.422.068.85.068 1.285a11.95 11.95 0 0 1-2.649 7.521c-.388.482-.987.729-1.605.729H13.48c-.483 0-.964-.078-1.423-.23l-3.114-1.04a4.501 4.501 0 0 0-1.423-.23H5.904m10.598-9.75H14.25M5.904 18.5c.083.205.173.405.27.602.197.4-.078.898-.523.898h-.908c-.889 0-1.713-.518-1.972-1.368a12 12 0 0 1-.521-3.507c0-1.553.295-3.036.831-4.398C3.387 9.953 4.167 9.5 5 9.5h1.053c.472 0 .745.556.5.96a8.958 8.958 0 0 0-1.302 4.665c0 1.194.232 2.333.654 3.375Z"}]])




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
          iri
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

(re-frame/reg-event-db
 ::set-active-annotation
 (fn [db [_ active-annotation]]
   (js/console.log "active-annotation " active-annotation)
   (assoc db ::active-annotation active-annotation)))

(re-frame/reg-sub
 ::active-annotation
 :-> ::active-annotation)

(defn authorship [a]
  (first
   (filter #(= "CG:Author"
               (get-in % [:role :curie]))
           (:contributions a))))

(defn evidence [e]
  ^{:key (:curie e)}
  [:div (get-in e [:subject :feature :label])])

#_(defn annotation [a]
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

(def users
  {"https//clingen.app/users/1" "Tasha"
   "https//clingen.app/users/2" "Tracy"
   "https//clingen.app/users/3" "Tristan"})

(def curation-reason
  {"CG:DosageMapConflict" {:icon hand-thumb-down
                           :text "Dosage Map Conflict"
                           :color "bg-red-100"}
   "CG:NoAssessment" {:icon document-text
                      :text "No Assessment"
                      :color "bg-slate-200"}
   "CG:ErrorInVariantDescription" {:icon wrench
                                   :text "Variant Problem"
                                   :color "bg-blue-100"}
   "CG:ClassificationCorrect" {:icon hand-thumb-up
                               :text "Correct"
                               :color "bg-green-100"}})

(defn user-name [agent]
  (get users (:iri agent)))

(defn annotation [a]
  (let [author (authorship a)
        classification  (get-in a [:classification :curie])
        active-annotation @(re-frame/subscribe [::active-annotation])]
    ^{:key (:iri a)}
    [:div
     [:div {:class (str "flex gap-4 rounded-lg m-1 py-1 px-3 "
                        (get-in curation-reason [classification :color]))
            ;; active annotation not currently used
            :on-click #(re-frame/dispatch [::set-active-annotation (:iri a)])}
      [:div (get-in curation-reason [classification :icon])]
      [:div (get-in curation-reason [classification :text])]
      [:div (user-name (:agent author))]]
      [:div {:class "px-8 py-1"}
       (:description a)]]))

(defn assertion [i]
  [:div
   {:class "flex gap-x-6"}
   [:div
    {:class "flex w-1/2 gap-x-4"}
    [:div
     {:class "min-w-0 flex-auto"}
     [:p
      {:class "text-sm font-semibold leading-6 text-gray-900"}
      [:a :href
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
   [:div
    {:class "w-1/2"}
    [:div
     {:class "text-gray-700 text-sm font-light"}
     (doall
      (for [a (:annotations i)]
        (annotation a)))]
    
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
