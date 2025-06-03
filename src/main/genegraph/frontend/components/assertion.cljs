(ns genegraph.frontend.components.assertion
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.icon :as icon]
            [genegraph.frontend.common :as common]
            [genegraph.frontend.display.features :as features]
            [clojure.string :as s]))

(def gv-classification-ordinals
  {"CG:Disputed" -1
   "CG:Refuted" -1
   "CG:NoKnownDiseaseRelationship" 0
   "CG:Supportive" 1
   "CG:Limited" 1
   "CG:Moderate" 2
   "CG:Strong" 3
   "CG:Definitive" 4})

(def dosage-classification-ordinals
  {"CG:DosageSufficientEvidence" 3
   "CG:DosageModerateEvidence" 2
   "CG:DosageMinimalEvidence" 1
   "CG:DosageNoEvidence" 0
   "CG:DosageAutosomalRecessive" -1
   "CG:DosageSensitivityUnlikely" -2})

(def assertion-query
  "  query ($iri: String) {
  assertion(iri: $iri) {
    __typename
    iri
    label
    annotations {
      classification {
        iri
        curie
        label
      }
    }
    classification {
      curie
      label
    }
    contributions {
      agent {
        iri
        label
      }
      role {
        iri
        label
      }
      date
    }
    subject {
      __typename
      iri
      ...variathPathFields
      ...mechanismProp
    }
    relatedAssertions {
      __typename
      iri
      classification {
        curie
        label
      }
      subject {
        __typename
        iri
        ...variathPathFields
      }
    }
  }
}

fragment variathPathFields on VariantPathogenicityProposition {
  curie
  variant {
    iri
    curie
    label
    overlappingFeatures {
      label
      curie
      subjectOf {
        __typename
        curie
        type {
          curie
        }
        ...geneValidityProp
        ...mechanismProp
      }
    }

    copyChange {
      curie
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
  assertions {
    curie
    evidenceStrength {
      curie
    }
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
  assertions {
    curie
    evidenceStrength {
      curie
    }
  }
}
")

(re-frame/reg-event-fx
 ::select
 (fn [{:keys [db]} [_ i source-component]]
   (js/console.log (str "run query " i))
   {#_#_:db (assoc db ::selected i)
    :fx [[:dispatch
          [::re-graph/query
           {:id ::select-assertion
            :query assertion-query
            :variables {:iri i}
            :callback [::recieve-select-result]}]]
         [:dispatch
          [:genegraph.frontend.view/push
           {:component :assertion
            :iri i}
           source-component]]]}))

(re-frame/reg-event-db
 ::recieve-select-result
 (fn [db [_ result]]
   (let [assertion (get-in result [:response :data :assertion])
         iri (:iri assertion)]
     (js/console.log (str "recieve select result " iri))
     #_(assoc db ::assertion (get-in result [:response :data :assertion]))
     (assoc-in db [:resources iri] assertion))))

(re-frame/reg-sub
 :resource
 (fn [db [_ iri]]
   (get-in db [:resources iri])))

(defn evaluation [assertion]
  (->> (:contributions assertion)
       #_(filter #(= "CG:Evaluator" (get-in % [:role :curie])))
       first))

(defn classification-pill [classification]
  (let [base-classes "p-px px-1 rounded-md "
        [label color-classes]
        (case (:curie classification)
          "CG:Pathogenic" ["P" "bg-red-700 text-red-100"]
          "CG:LikelyPathogenic" ["LP" "bg-red-700 text-red-100"]
          "CG:UncertainSignificance" ["VUS" "bg-blue-900 text-blue-50"]
          "CG:LikelyBenign" ["LB" "bg-green-900 text-green-100"]
          "CG:Benign" ["B" "bg-green-900 text-green-100"]
          ["N" "bg-gray-500 text-gray-50"])]
    [:div
     {:class (str base-classes color-classes)}
     label]))

(defn variant-small-label [variant]
  (-> (:label variant)
      (s/replace #"GRCh\d\d/hg\d\d" "")
      (s/replace #"\(.*\)" "")))

(defn evidenceStrength->ordinal [assertion]
  (gv-classification-ordinals
   (get-in assertion [:evidenceStrength :curie])))

(defn gv-assertion-with-strongest-classification [assertions]
  (->> assertions (sort-by evidenceStrength->ordinal) reverse first))

(defn gv-proposition-with-strongest-classification [propositions]
  (->> propositions
       (sort-by #(-> %
                     :assertions
                     gv-assertion-with-strongest-classification
                     evidenceStrength->ordinal))
       reverse
       first))

(defn assertions [assertions]
  [:div
   {:class "flex gap-1 px-1"}
   (for [a assertions]
     ^{:key a}
     (common/pill (get-in a [:evidenceStrength :curie])))])

(defn assertion-list-item [assertion source-component]
  [assertion]
  (if-let [e (evaluation assertion)]
    ^{:key assertion}
    [:li
     {:class "p-2"
      :on-click #(re-frame/dispatch [::select (:iri assertion) source-component])}
     [:div
      {:class "flex items-center"}
      [:div
       {:class "pr-3 text-xs font-semibold"}
       (classification-pill (:classification assertion))]
      [:div
       {:class "flex-1 text-sm/6 font-semibold text-gray-900"}
       (variant-small-label (get-in assertion [:subject :variant]))]]
     [:p
      {:class "text-xs text-gray-500"}
      (get-in e [:agent :label])]
     [:p
      {:class "text-xs text-gray-500"}
      (:date e)]]
    ^{:key assertion}
    [:li "error"]))

(defn gene-validity-proposition-div [{:keys [modeOfInheritance disease]
                                  :as p}]
  ^{:key p}
  [:div
   #_[:pre (with-out-str (cljs.pprint/pprint p))]
   {:class "flex gap-1 px-2"}
   (common/pill "GeneValidityProposition")
   (common/pill (:curie modeOfInheritance))
   [:div
    (-> p
        :assertions
        gv-assertion-with-strongest-classification
        :evidenceStrength
        :curie
        common/pill)]
   #_(assertions (:assertions p))
   [:div
    {:class "p-1"}
    (or (get-in p [:disease :label])
        (get-in p [:disease :curie]))]])

(defn genetic-condition-mechanism-proposition [{:keys [mechanism] :as p}]
  ^{:key p}
  [:div
   {:class "flex gap-1 px-2"}
   (common/pill "GeneticConditionMechanismProposition")
   (common/pill (:curie mechanism))
   (assertions (:assertions p))
   [:div
    {:class "p-1"}
    (get-in p [:condition :label])]])

(defn proposition-div [p]
  (case (:__typename p)
    "GeneValidityProposition"
    (gene-validity-proposition-div p)
    "GeneticConditionMechanismProposition"
    (genetic-condition-mechanism-proposition p)
    ^{:key p}
    [:div (:__typename p)]))

(defn strongest-gv-assertions-div [propositions]
  (for [p (->> propositions
               (filter #(= "GeneValidityProposition" (:__typename %)))
               (group-by :modeOfInheritance)
               vals
               (map gv-proposition-with-strongest-classification))]
    ^{:key p}
    (gene-validity-proposition-div p)))

(defn feature-display [feature]
  ^{:key feature}
  [:div
   #_{:class "flex"}
   {:class "flex gap-2 py-4"}
   [:div
    {:class "p-1 font-semibold"}
    (:label feature)]
   [:div
    {:class "flex flex-col"}
    (strongest-gv-assertions-div (:subjectOf feature))
    #_(gv-proposition-with-strongest-classification )
    #_(for [p (:subjectOf feature)]
      (proposition-div p))]])

(defn overlapping-features [assertion]
  (let [features (get-in assertion [:subject :variant :overlappingFeatures])
        grouped-features (features/group-features-for-cnv features)]
    [:div
     {:class "flex flex-col"}
     (for [f (:dosage-sufficient grouped-features)]
       (feature-display f))
     (for [f (:gene-validity grouped-features)]
       (feature-display f))]))

(defn variant-assertion-label-div
  ([assertion] (variant-assertion-label-div assertion nil))
  ([assertion source-component]
   [:div
    {:class "flex"
     :on-click #(if source-component
                  (re-frame/dispatch [::select (:iri assertion) source-component]))}
    [:div
     {:class "flex"}
     [:div
      {:class "pr-3 text-xs font-semibold"}
      (classification-pill (:classification assertion))]
     [:div
      {:class "flex"}
      [:div
       {:class "flex text-sm/6 font-semibold text-gray-900"}
       (variant-small-label (get-in assertion [:subject :variant]))]
      [:a
       {:href (:iri assertion)
        :target "_blank"}
       icon/link-micro]]]]))

(defn related-assertions [assertion assertion-def]
  [:div
   {:class "flex flex-col"}
   (for [a (:relatedAssertions assertion)]
     ^{:key a}
     (variant-assertion-label-div a assertion-def))])

(defn assertion-detail-div [assertion-def]
  (if-let [assertion @(re-frame/subscribe [:resource (:iri assertion-def)])]
    [:div
     {:class "flex flex-1 p-10 flex-col"}
     (variant-assertion-label-div assertion)
     (overlapping-features assertion)
     (related-assertions assertion assertion-def)]
    [:div
     {:class "flex flex-1 p-10"}
     "nothing yet"]))


#_[:div
   [:pre (with-out-str (cljs.pprint/pprint assertion))]]
