(ns genegraph.frontend.page.resource
  (:require [re-frame.core :as rf]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.user :as user]
            [genegraph.frontend.icon :as icon]
            [genegraph.frontend.common :as common]
            [genegraph.frontend.queries :as queries]))

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
 ::recieve-resource-query
 (fn [db [_ response]]
   (let [resource (get-in response [:response :data :resource])]
     (assoc db (:curie resource) resource))))

(rf/reg-event-fx
 ::resource-query
 (fn [_ [_ curie]]
   {:fx [[:dispatch
          [::re-graph/query
           {:id ::resource-query
            :query queries/compiled-base-query
            :variables {:iri curie}
            :callback [::recieve-resource-query]}]]]}))


(rf/reg-event-db
 ::set-current-resource
 (fn [db [_ r]]
   (let [result (get-in r [:response :data])]
     (assoc db
            ::current-resource
            (or (:sequenceFeatureQuery result))))))

#_(rf/reg-event-fx
 ::nav-to
 (fn [{:keys [db]} [_ r]]
   (let [fx {:db (assoc db ::current-resource-curie r)}]
     (if (:__typename (get db r))
       fx
       (assoc
        fx
        :fx
        [[:disp]])))

   #_{:fx [[:dispatch
            [::re-graph/query
             :id ::query
             :query (get resource-queries (:__typename r))
             :variables {:iri (:curie r)}
             :callback [::set-current-resource]]]]}))

(rf/reg-event-fx
 ::nav-to
 (fn [{:keys [db]} [_ curie]]
   {:db (assoc db ::current-resource-curie curie)
    :fx [[:dispatch
          [::re-graph/query
           {:id ::resource-query
            :query queries/compiled-base-query
            :variables {:iri curie}
            :callback [::recieve-resource-query]}]]]}))

(rf/reg-sub
 ::current-resource
 (fn [db]
   (get db (::current-resource-curie db))))

;; temp for now, gets us back to the display of genes
(rf/reg-sub
 ::main
 :-> :main)

(defn resource []
  (let [current @(rf/subscribe [::current-resource])]
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
