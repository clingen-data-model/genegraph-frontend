(ns genegraph.frontend.page.home
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.display :as display]
            [genegraph.frontend.user :as user]
            [genegraph.frontend.icon :as icon]
            [genegraph.frontend.display.annotation :as annotation]
            [genegraph.frontend.display.features :as features]
            [genegraph.frontend.queries.assertion :as assertion]
            [clojure.string :as s]))


(def assertion-query
  "query ($iri: String) {
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

(re-frame/reg-sub
 ::query-data
 :-> :query-data)

(re-frame/reg-event-db
 ::recieve-select-result
 (fn [db [_ result]]
   (js/console.log (str "recieve select result "
                        (get-in result [:response :data :assertion :iri])))
   (assoc db ::assertion (get-in result [:response :data :assertion]))))

(re-frame/reg-sub
 ::assertion
 :-> ::assertion)

(re-frame/reg-event-fx
 ::select
 (fn [{:keys [db]} [_ i]]
   (js/console.log (str "run query " i))
   {:db (assoc db ::selected i)
    :fx [[:dispatch
          [::re-graph/query
           {:id ::select-assertion
            :query assertion-query
            :variables {:iri i}
            :callback [::recieve-select-result]}]]]}))

(re-frame/reg-sub
 ::selected
 :-> ::selected)

(def curie->pill
  {"CG:Haploinsufficiency" {:label "HS"
                            :tooltip "Haploinsufficiency"}
   "CG:Triplosensitivity" {:label "TS"
                           :tooltip "Triplosensitivity"}
   "HP:0000006" {:label "AD"
                 :tooltip "Autosomal Dominant"}
   "HP:0000007" {:label "AR"
                 :tooltip "Autosomal Recessive"}
   "HP:0032113" {:label "SD"
                 :tooltip "Semidominant"}
   "HP:0001417" {:label "XL"
                 :tooltip "X-Linked"}
   "HP:0000005" {:label "MOI"
                 :tooltip "No mode of inheritance determined"}
   "GeneValidityProposition" {:label "GV"
                              :tooltip "Gene Validity"}
   "GeneticConditionMechanismProposition" {:label "Dosage"
                                           :tooltip "Mechanism of Disease"}
   "CG:Definitive" {:label "Definitive"
                    :tooltip "Definitive"}
   "CG:Strong" {:label "Strong"
                :tooltip "Strong"}
   "CG:Moderate" {:label "Moderate"
                  :tooltip "Moderate"}
   "CG:Limited" {:label "Limited"
                 :tooltip "Limited"}
   "CG:NoKnownDiseaseRelationship" {:label "None"
                                    :tooltip "No Known Disease Relationship"}
   "CG:Disputed" {:label "Disputed"
                  :tooltip "Disputed"}
   "CG:Refuted" {:label "Refuted"
                 :tooltip "Refuted"}
   "CG:Supportive" {:label "Supportive"
                    :tooltip "Supportive"}
   "CG:DosageSufficientEvidence" {:label "Sufficient"
                                  :tooltip "Sufficient"}
   "CG:DosageModerateEvidence" {:label "Moderate"
                                :tooltip "Moderate"}
   "CG:DosageMinimalEvidence" {:label "Minimial"
                               :tooltip "Minimal"}
   "CG:DosageNoEvidence" {:label "None"
                          :tooltip "No Evidence"}
   "CG:DosageAutosomalRecessive" {:label "None"
                                  :tooltip "No Evidence"}})

(defn pill [curie]
  (let [{:keys [label]} (curie->pill curie {:label curie :tooltip curie})]
    [:div
     {:class "bg-gray-100 m-1 px-2 rounded-full h-fit"}
     label]))

(defn assertions [assertions]
  [:div
   {:class "flex gap-1 px-1"}
   (for [a assertions]
     ^{:key a}
     (pill (get-in a [:evidenceStrength :curie])))])

(defn gene-validity-proposition [{:keys [modeOfInheritance disease]
                                  :as p}]
  ^{:key p}
  [:div
   #_[:pre (with-out-str (cljs.pprint/pprint p))]
   {:class "flex gap-1 px-2"}
   (pill "GeneValidityProposition")
   (pill (:curie modeOfInheritance))
   (assertions (:assertions p))
   [:div
    {:class "p-1"}
    (or (get-in p [:disease :label])
        (get-in p [:disease :curie]))]])

(defn genetic-condition-mechanism-proposition [{:keys [mechanism] :as p}]
  ^{:key p}
  [:div
   {:class "flex gap-1 px-2"}
   (pill "GeneticConditionMechanismProposition")
   (pill (:curie mechanism))
   (assertions (:assertions p))
   [:div
    {:class "p-1"}
    (get-in p [:condition :label])]])

(defn proposition [p]
  (case (:__typename p)
    "GeneValidityProposition"
    (gene-validity-proposition p)
    "GeneticConditionMechanismProposition"
    (genetic-condition-mechanism-proposition p)
    {:key p}
    [:div (:__typename p)]))

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
    (for [p (:subjectOf feature)]
      (proposition p))]])


(defn evaluation [assertion]
  (->> (:contributions assertion)
       #_(filter #(= "CG:Evaluator" (get-in % [:role :curie])))
       first))

(defn annotation-div [assertion]
  [:div
   {:class "flex p-1"
    :on-click #(re-frame/dispatch [::annotation/set-currently-annotating (:iri assertion)])}
   icon/document-text-outline])

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

(defn assertion-list-item [assertion current-user selected]
  [assertion]
  (if-let [e (evaluation assertion)]
    ^{:key assertion}
    [:li
     {:class (if selected
               "p-2 bg-gray-200 rounded-xl"
               "p-2")
      :on-click #(re-frame/dispatch [::select (:iri assertion)])}
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

(defn overlapping-features [assertion]
  (let [features (get-in assertion [:subject :variant :overlappingFeatures])
        grouped-features (features/group-features-for-cnv features)]
    [:div
     {:class "flex flex-col"}
     (for [f (:dosage-sufficient grouped-features)]
       (feature-display f))
     (for [f (:gene-validity grouped-features)]
       (feature-display f))
     [:div (count (:others grouped-features))]]))

(defn assertion-detail []
  (if-let [assertion @(re-frame/subscribe [::assertion])]
    [:div
     {:class "flex flex-1 p-10 flex-col"}
     [:div
      {:class "flex"}
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
         icon/link-micro]]]]
     (overlapping-features assertion)
     #_[:div
      [:pre (with-out-str (cljs.pprint/pprint assertion))]]]
    [:div
     {:class "flex flex-1 p-10"}
     "nothing yet"]))

(defn home []
  (let [current-user @(re-frame/subscribe [::user/current-user])
        selected @(re-frame/subscribe [::selected])
        currently-annotating @(re-frame/subscribe [::annotation/currently-annotating])]
    [:div
     {:class "flex"}
     [:div
      {:class "flex flex-1 p-10"}
      [:ul
       (doall
        (for [i @(re-frame/subscribe [::query-data])]
          (if (= selected (:iri i))
            (assertion-list-item i current-user selected)
            (assertion-list-item i current-user false))))]]
     (assertion-detail)
     #_[:div {:class "flex flex-1 p-10"} "hi"]]))

;; TODO reimplement with hybrid-resource methods
;; Also only works for ClinVar submisisons
;; Consdier other use cases

(defmethod display/list-title  "VariantPathogenicityProposition"
  [prop]
  [:p
   {:class "text-sm/6 font-semibold text-gray-900"}
   [:a
    {:href (get-in prop [:variant :iri])
     :target "_blank"}
    (get-in prop [:variant :label])]])
 
