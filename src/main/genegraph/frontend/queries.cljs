(ns genegraph.frontend.queries)

(def graphql-query
"
query ($filters: [Filter]) {
  assertions(filters: $filters) {
    __typename
    iri
    label
    annotations {
      classification {
        iri
       }
    }
    classification {
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
      ... on VariantPathogenicityProposition {
        curie
        variant {
          iri
          label
        }
      }
    }
  }
}
"
  )

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
    :description "Copy Number Loss variants in ClinVar that have a partial overlap with a gene classified as Haploinsufficenty genes in the ClinGen Dosage map. Excluding variants that could be classified as pathogenic for another reason."
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
   {:label "Other annotated assertions"
    :description "Assertions that have been annotated by curators, without making an assessment about the quality of the submission or the "
    :filters [{:filter :proposition_type
               :argument "CG:VariantPathogenicityProposition"}
              {:filter :has_annotation
               :argument "CG:NoAssessment"}]}])
