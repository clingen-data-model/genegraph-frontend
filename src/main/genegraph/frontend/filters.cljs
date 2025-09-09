(ns genegraph.frontend.filters
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [genegraph.frontend.queries :as queries]))

;; These are the filters used to narrow down the set of 

(def graphql-query
  (queries/compile-query  "
query ($filters: [Filter]) {
  assertions(filters: $filters) {
    __typename
    iri
    label
    annotations {
      classification {
        curie
       }
    }
    classification {
      curie
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
      ...Resource1
      ...VariantPathogenicityProposition1
    }
  }
}
"))
(println graphql-query) 
 
(re-frame/reg-event-fx
 ::send-query
 (fn [{:keys [db]} [_ filters]]
   (js/console.log "sending query")
   {:fx [[:dispatch
          [::re-graph/query
           {:id ::query
            :query graphql-query
            :variables {:filters filters}
            :callback [::recieve-query-result]}]]]}))

(re-frame/reg-event-db
 ::recieve-query-result
 (fn [db [_ result]]
   (js/console.log "recieved result")
   (assoc db
          ::query-result
          (get-in result
                  [:response
                   :data
                   :assertions]))))


(re-frame/reg-event-db
 ::clear-query-result
 (fn [db _]
   (js/console.log "cleared result")
   (dissoc db ::query-result)))

(re-frame/reg-sub
 ::query-result
 :-> ::query-result)

(def queries
  [{:label "Deletions with >= 35 Genes"
    :description "Copy Number Loss variants in ClinVar that meet the criteria for Likely Pathogenic according to the ACMG guidelines based on gene count alone."
    :filters [{:filter :proposition_type
               :argument "CG:VariantPathogenicityProposition"}
              {:filter :copy_change
               :argument "EFO:0030067"}
              {:filter :gene_count_min
               :argument "CG:Genes35"}]}
   {:label "Duplications with >= 50 Genes"
    :description "Copy Number Gain variants in ClinVar that meet the criteria for Likely Pathogenic according to the ACMG guidelines based on gene count alone."
    :filters [{:filter :proposition_type
               :argument "CG:VariantPathogenicityProposition"}
              {:filter :copy_change
               :argument "EFO:0030070"}
              {:filter :gene_count_min
               :argument "CG:Genes50"}]}
   {:label "Deletions with complete overlap of HI 3 features"
    :description "Copy Number Loss variants in ClinVar that have a complete overlap with a gene or region classified as Haploinsufficient with sufficient evidence in the ClinGen Dosage Map."
    :filters [{:filter :proposition_type
               :argument "CG:VariantPathogenicityProposition"}
              {:filter :copy_change
               :argument "EFO:0030067"}
              {:filter :complete_overlap_with_feature_set
               :argument "CG:HaploinsufficiencyFeatures"}]}
   {:label "Duplications with complete overlap of TS 3 features"
    :description "Copy Number Gain variants in ClinVar that have a complete overlap with a gene or region classified as Triplosensitive with sufficient evidence by the ClinGen Dosage Map."
    :filters [{:filter :proposition_type
               :argument "CG:VariantPathogenicityProposition"}
              {:filter :copy_change
               :argument "EFO:0030070"}
              {:filter :complete_overlap_with_feature_set
               :argument "CG:TriplosensitivityFeatures"}]}
   {:label "Deletions with complete overlap of AD/XL gene-disease-validity"
    :description "Copy Number Loss variants in ClinVar that have a complete overlap with a gene classified as Moderate or greater in the ClinGen Gene-Disease Validity curation framework with an Autosomal Dominant or X-Linked inheritance pattern."
    :filters [{:filter :proposition_type
               :argument "CG:VariantPathogenicityProposition"}
              {:filter :copy_change
               :argument "EFO:0030067"}
              {:filter :complete_overlap_with_feature_set
               :argument "CG:GeneValidityModerateAndGreaterADXL"}]}
   {:label "Deletions with complete overlap with AR genes not AD genes"
    :description "Copy Number Loss variants in ClinVar that have a complete overlap with a gene classified as Moderate or greater in the ClinGen Gene-Disease Validity curation framework with an Autosomal Recessive inheritance pattern or a gene classified as having an Autosomal Recessive pattern in Gene Dosage (Score 30), excluding variants that overlap a gene associated with an Autosomal Dominant condition."
    :filters [{:filter :proposition_type
               :argument "CG:VariantPathogenicityProposition"}
              {:filter :copy_change
               :argument "EFO:0030067"}
              {:filter :complete_overlap_with_feature_set
               :argument "CG:ARGene"}
              {:filter :complete_overlap_with_feature_set
               :argument "CG:GeneValidityModerateAndGreaterADXL"
               :operation "not_exists"}
              {:filter :complete_overlap_with_feature_set
               :argument "CG:HaploinsufficiencyFeatures"
               :operation "not_exists"}
              {:filter :gene_count_min
               :argument "CG:Genes35"
               :operation "not_exists"}]}
   {:label "Deletions with partial overlap of HI genes"
    :description "Copy Number Loss variants in ClinVar that have a partial overlap with a gene classified as Haploinsufficency genes in the ClinGen Dosage map. Excluding variants that could be classified as pathogenic for another reason."
    :filters [{:filter :proposition_type
               :argument "CG:VariantPathogenicityProposition"}
              {:filter :copy_change
               :argument "EFO:0030067"}
              {:filter :partial_overlap_with_feature_set
               :argument "CG:HaploinsufficiencyFeatures"}
              {:filter :complete_overlap_with_feature_set
               :argument "CG:HaploinsufficiencyFeatures"
               :operation "not_exists"}
              {:filter :gene_count_min
               :argument "CG:Genes35"
               :operation "not_exists"}]}
   {:label "Deletions with partial overlap of AD/XL Gene Validity features"
    :description "Copy Number Loss variants in ClinVar that have a partial overlap with a gene classified as having Moderate or greater evidence and AD/XL inheritance pattern in the ClinGen Gene Validity framework; Excluding variants that could be classified as pathogenic for another reason."
    :filters [{:filter :proposition_type
               :argument "CG:VariantPathogenicityProposition"}
              {:filter :copy_change
               :argument "EFO:0030067"}
              {:filter :partial_overlap_with_feature_set
               :argument "CG:GeneValidityModerateAndGreaterADXL"}
              {:filter :partial_overlap_with_feature_set
               :argument "CG:HaploinsufficiencyFeatures"
               :operation "not_exists"}
              {:filter :complete_overlap_with_feature_set
               :argument "CG:HaploinsufficiencyFeatures"
               :operation "not_exists"}
              {:filter :gene_count_min
               :argument "CG:Genes35"
               :operation "not_exists"}]}
   {:label "Deletions with partial overlap of autosomal recessive genes"
    :description "Copy Number Loss variants in ClinVar that have a partial overlap with a gene associated with an autosomal recessive condition. Excluding variants that could be classified as pathogenic for another reason."
    :filters [{:filter :proposition_type
               :argument "CG:VariantPathogenicityProposition"}
              {:filter :copy_change
               :argument "EFO:0030067"}
              {:filter :partial_overlap_with_feature_set
               :argument "CG:ARGene"}
              {:filter :partial_overlap_with_feature_set
               :argument "CG:GeneValidityModerateAndGreaterADXL"
               :operation "not_exists"}
              {:filter :complete_overlap_with_feature_set
               :argument "CG:GeneValidityModerateAndGreaterADXL"
               :operation "not_exists"}
              {:filter :partial_overlap_with_feature_set
               :argument "CG:HaploinsufficiencyFeatures"
               :operation "not_exists"}
              {:filter :complete_overlap_with_feature_set
               :argument "CG:HaploinsufficiencyFeatures"
               :operation "not_exists"}
              {:filter :gene_count_min
               :argument "CG:Genes35"
               :operation "not_exists"}]}
   {:label "Variants with no applicable ClinGen data"
    :description "Copy Number Loss variants in ClinVar that have no overlap with features annotated in ClinGen knowledgebases."
    :filters [{:filter :proposition_type
               :argument "CG:VariantPathogenicityProposition"}
              {:filter :copy_change
               :argument "EFO:0030067"}
              {:filter :partial_overlap_with_feature_set
               :argument "CG:ARGene"
               :operation "not_exists"}
              {:filter :complete_overlap_with_feature_set
               :argument "CG:ARGene"
               :operation "not_exists"}
              {:filter :partial_overlap_with_feature_set
               :argument "CG:GeneValidityModerateAndGreaterADXL"
               :operation "not_exists"}
              {:filter :complete_overlap_with_feature_set
               :argument "CG:GeneValidityModerateAndGreaterADXL"
               :operation "not_exists"}
              {:filter :partial_overlap_with_feature_set
               :argument "CG:HaploinsufficiencyFeatures"
               :operation "not_exists"}
              {:filter :complete_overlap_with_feature_set
               :argument "CG:HaploinsufficiencyFeatures"
               :operation "not_exists"}
              {:filter :gene_count_min
               :argument "CG:Genes35"
               :operation "not_exists"}]}
   {:label "Annotated assertions"
    :description "Assertions that have been annotated by curators."
    :filters [{:filter :proposition_type
               :argument "CG:VariantPathogenicityProposition"}
              {:filter :has_annotation}]}
   {:label "Candidate Gold Standard"
    :description "Variants that are not pathogenic due to the gene dosage map but are called as pathogenic in ClinVar. Must be newer than 2020"
    :filters [{:filter :proposition_type
               :argument "CG:VariantPathogenicityProposition"}
              {:filter :copy_change
               :argument "EFO:0030067"}
              {:filter :partial_overlap_with_feature_set
               :argument "CG:HaploinsufficiencyFeatures"
               :operation "not_exists"}
              {:filter :complete_overlap_with_feature_set
               :argument "CG:HaploinsufficiencyFeatures"
               :operation "not_exists"}
              {:filter :gene_count_min
               :argument "CG:Genes35"
               :operation "not_exists"}
              {:filter :assertion_direction
               :argument "CG:Supports"}
              {:filter :submitter
               :argument "CVAGENT:500031"
               :operation "not_exists"}
              {:filter :date_evaluated_min
               :argument "2020"}
              {:filter :complete_overlap_with_feature_set
               :argument "CG:ProteinCodingGenes"}]}
   {:label "From Invitae"
    :description "Assertions that have been annotated by curators."
    :filters [{:filter :proposition_type
               :argument "CG:VariantPathogenicityProposition"}
              {:filter :from_agent
               :argument "CVAGENT:500031"}]}
   #_{:label "Other annotated assertions"
      :description "Assertions that have been annotated by curators, without making an assessment about the quality of the submission or the "
      :filters [{:filter :proposition_type
                 :argument "CG:VariantPathogenicityProposition"}
                {:filter :has_annotation
                 :argument "CG:NoAssessment"}]}])
