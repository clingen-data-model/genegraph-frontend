(ns genegraph.frontend.page.resource
  (:require [re-frame.core :as rf]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.user :as user]
            [genegraph.frontend.queries :as queries]
            [genegraph.frontend.icon :as icon]
            [genegraph.frontend.common :as common]))

(def resource-queries
  {"SequenceFeature"
   "query ($iri: String) {
  sequenceFeatureQuery(symbol: $symbol) {
    __typename
    iri
    curie
    label
    type {
      curie
      label
    }
    assertions {
      curie
      iri
      evidenceStrength {
        curie
      }
      subject {
        __typename
        type {
          curie
          label
        }
        ...geneValidityProp
        ...mechanismProp
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

fragment geneValidityProp on GeneValidityProposition {
  modeOfInheritance {
    curie
    label
  }
  disease {
    curie
    label
  }
}

fragment mechanismProp on GeneticConditionMechanismProposition {
  mechanism {
    curie
  }
  condition {
    label
    curie
  }
}"})



(rf/reg-event-db
 ::set-current-resource
 (fn [db [_ r]]
   (let [result (get-in r [:response :data])]
     (assoc db
            ::current-resource
            (or (:sequenceFeatureQuery result))))))

(rf/reg-event-fx
 ::nav-to
 (fn [{:keys [db]} [_ r]]
   {:fx [[:dispatch
          [::re-graph/query
           :id ::query
           :query (get resource-queries (:__typename r))
           :variables {:iri (:curie r)}
           :callback [::set-current-resource]]]]}))

(rf/reg-sub
 ::current-resource
 :-> ::current-resource)

;; temp for now, gets us back to the display of genes
(rf/reg-sub
 ::main
 :-> :main)

(defn resource []
  (let [current @(rf/subscribe [::main])]
    [:div
     {:class "px-4 py-10 sm:px-6 lg:px-8 lg:py-6"}
     (if current
       (common/main-view current)
       [:div])
     #_[:pre (with-out-str (cljs.pprint/pprint current))]]))

#_(defn resource []
  (let [current-route @(rf/subscribe [::current-resource])
        current @(rf/subscribe [::main])]
    [:div
     [:pre (with-out-str (cljs.pprint/pprint current))]]))
