(ns genegraph.schema)

(def schema
  {:interfaces
   {:rdfs/Resource
    {:description "An RDF Resource in Genegraph. Common interface to all entities in Genegraph; all entities will inherit from this at some level."

     ;; we'll see how things work,
     ;; but IRI and CURIE might be special--will have to deal later
     :properties
     [:rdfs/label :rdf/type :dc/description :dc/source :dc/description :skos/inScheme]}}
   :classes
   {:owl/Class
    {:properties
     [:rdfs/subClassOf]}
    :cg/Agent
    {}
    :cg/ValueSet
    {:properties
     [:skos/member]}
    :cg/GeneticConditionMechanismProposition
    {:rdfs/label "Genetic Condition Mechanism Proposition"
     :dc/description "The proposition that a change of function of the given feature with the associated mechanism causes the given condition."
     :properties
     [:cg/feature :cg/mechanism :cg/condition :cg/assertions]}
    :cg/EvidenceStrengthAssertion
    {:rdfs/label "Evidence Strength Assertion"
     :dc/description "An assertion of the strength of evidence supporting a given proposition."
     :properties
     [:cg/subject :cg/annotations :cg/evidenceStrength :cg/contributions :dc/date :cg/evidence :cg/strengthScore]}
    :cg/Contribution
    {:properties
     [:cg/agent :dc/date :cg/role]}
    :cg/EvidenceLine     ;; add to base
    {:properties
     [:cg/evidence :cg/strengthScore :cg/evidenceStrength]}
    :cg/CanonicalVariant ;; Refactor around CatVRS? not right now
    {:rdfs/label "Cannonical Allele"
     :dc/description "A concept encompassing all alleles that can be mapped to the given allele. May optionally include a set of alleleMappings of equivalent alleles. May also include a source for the set of mappings and associated identifier."
     :properties
     [:cg/allele :cg/alleleMappings]}
    :cg/VariantPathogenicityProposition
    {:rdfs/label "Variant Pathogenicity Proposition"
     :dc/description "The proposition that a given variant is causative of a given condition."
     :properties
     [:cg/variant :cg/condition]}
    :cg/AssertionAnnotation
    {:rdfs/label "Assertion Annotation"
     :dc/description "Annotation on an assertion. May be used to annotate whether the person performing the annotation agrees with the assertion or not."
     :properties
     [:cg/subject :cg/evidence :cg/classification :cg/contributions]}
    :cg/GeneValidityProposition
    {:rdfs/label "Gene Validity Proposition"
     :dc/description "The proposition that variants affecting a gene are causative of a disease, given a mode of inheritance."
     :properties
     [:cg/gene :cg/modeOfInheritance :cg/disease]}
    :cg/VariantObservation ;; Maybe want to use just Observation, FHIR style? Let's do that
    {:properties
     [:cg/proband :cg/variant]}
    :cg/Proband
    {:cg/note "There are other properties here that should be refactored to other parts of the model. Specifically, zygosity, genotyping_method, etc are part of variant detection."
     :properties
     [:cg/observations :cg/family :cg/phenotype]}
    :cg/FunctionalAlteration
    {:properties
     []
     :cg/note "Currently does not support properties beyond standard Resource properties"}
    :cg/Affiliation ;; Can keep, but need to sort out the relationships with Agents, etc
    {:properties
     []}
    :cg/Finding
    {:properties
     [:cg/demonstrates :cg/method :cg/statisticalSignificanceType :cg/statisticalSignificanceValue :cg/caseCohort :cg/controlCohort :cg/statisticalSignificanceValueType :cg/lowerConfidenceLimit :cg/upperConfidenceLimit :cg/pValue]}
    :cg/FamilyCosegregation
    {:properties
     [:cg/phenotype :cg/phenotypePositiveAllelePositive :cg/family :cg/proband :cg/meetsInclusionCriteria :cg/estimatedLodScore :cg/phenotypeFreeText :cg/publishedLodScore :cg/SequencingMethod :dc/description :cg/phenotypeNegativeAlleleNegative :cg/sequencingMethod]}
    :cg/Family
    {:properties
     [:cg/member :cg/ethnicity :cg/modeOfInheritance]}
    :cg/Cohort
    {:properties
     [:cg/evidence :cg/detectionMethod :cg/allGenotypedSequenced :cg/numWithVariant :cg/alleleFrequency :cg/relatedCondition]}
    :cg/VariantFunctionalImpactEvidence
    {:properties
     [:cg/functionalDataSupport]
     :cg/note "Find out what is going on with functionalDataSupport"}
    :so/SequenceFeature
    {:properties
     [:ga4gh/definingLocation :ga4gh/location :owl/sameAs :skos/prefLabel :skos/altLabel :so/ChromosomeBand :skos/hiddenLabel]}
    :dc/BibliographicResource
    {:properties
     [:dc/creator :dc/title :dc/date :dc/abstract :cg/about]}
    ;; [:ga4gh/CanonicalLocation nil] ; need, but need to think about
    ;; [:ga4gh/SequenceLocation nil] ; need -- incorporate from ga4gh schema
    ;; [:ga4gh/VariationDescriptor nil] ; obsolete, but need for now
    }
   
   :properties
   {:rdfs/label {:type 'String}
    :rdf/type {:type (list :owl/Class)}
    :dc/description {:type 'String}
    :dc/source {:type :rdfs/Resource}
    :skos/inScheme {:type (list :cg/ValueSet)}
    :skos/member {:type (list :rdfs/Resource)}
    :rdfs/subClassOf {:type (list :owl/Class)}
    :cg/assertions {:type (list :cg/EvidenceStrengthAssertion)}
    :cg/subject {:type :rdfs/Resource}
    :cg/annotations {:type (list :cg/AssertionAnnotation)}
    :cg/evidenceStrength {:type :owl/Class} ;; TODO or skos/concept?
    :cg/contributions {:type (list :cg/Contribution)}
    :dc/date {:type 'String}
    :cg/agent {:type :cg/Agent}
    :cg/role {:type :owl/Class}
    :cg/feature {:type :so/SequenceFeature}
    :cg/observations {:type (list :cg/VariantObservation)}
    :skos/prefLabel {:type 'String}
    :cg/variant {:type :cg/CanonicalVariant}
    :cg/pValue {:type 'Float}
    :cg/classification {:type :owl/Class}
    :dc/creator {:type :cg/Agent}
    :cg/functionalDataSupport {} ;; investigate
    :cg/about {:type :rdfs/Resource}
    :cg/method {:type :owl/Class}
    :skos/altLabel {:type 'String}
    :cg/caseCohort {} ;; investigate
    :cg/publishedLodScore {:type 'Float}
    :cg/allGenotypedSequenced {} ;; investigate, boolean?
    :cg/allele {} ;; source CannonicalAllele, refers to ga4gh allele
    :cg/numWithVariant {:type 'Int}
    :ga4gh/definingLocation {}  ;; ga4gh location
    :cg/lowerConfidenceLimit {} ;; investigate
    :cg/family {:type :cg/Family}
    :cg/alleleFrequency {} ;; investigate
    :cg/phenotype {:type :owl/Class :cg/note "recommended to use HPO terms"}
    :cg/upperConfidenceLimit {} ; investigate
    :cg/member {:type (list :cg/Proband)}            ; update to family member?
    :cg/disease {:type :owl/Class}
    :dc/abstract {:type 'String}
    :cg/statisticalSignificanceValueType {} ;;investigate
    :cg/evidence {:type (list :rdfs/Resource)}
    :dc/title {:type 'String}
    :cg/ethnicity {:type :owl/Class}
    :cg/sequencingMethod {:type :owl/Class}
    :cg/meetsInclusionCriteria {}           ; boolean?
    :cg/relatedCondition {:type :owl/Class} ; also investigate
    :cg/phenotypeNegativeAlleleNegative {}  ; boolean?
    :cg/statisticalSignificanceType {:type :owl/Class} ; also investigate
    :cg/phenotypePositiveAllelePositive {}             ; boolean?
    :cg/modeOfInheritance {:type :owl/Class :cg/note "domain is HPO terms for MOI"} 
    :cg/demonstrates {}               ; investigate, think is :owl/Class
    :ga4gh/location {}                    ; bring in from GA4GH
    :cg/proband {:type :cg/Proband}
    :cg/condition {:type :owl/Class}
    :owl/sameAs {:type (list :owl/Class)}
    :cg/strengthScore {:type 'Float}
    :cg/phenotypeFreeText {:type 'String}
    :cg/controlCohort {}                  ; investigate
    :cg/alleleMappings {} ; ga4gh allele list
    :cg/gene {:type :so/SequenceFeature}
    :so/ChromosomeBand {:type 'String} ; should maybe be sequence feature
    :cg/detectionMethod {:type :owl/Class} ;; investigate may actually be 'String
    :cg/estimatedLodScore {:type 'Float}
    :cg/statisticalSignificanceValue {}  ;; investigate
    :skos/hiddenLabel {:type (list 'String)}
    :cg/mechanism {:type :owl/Class}}

   :value-sets
   {:cg/StudyGeneticAnalysisSet
    {:skos/member [:cg/AggregateVariantAnalysis
                   :cg/SingleVariantAnalysis
                   :cg/CandidateGeneSequencing
                   :cg/AllGenesSequencing]}
    
    :cg/EvidenceStrengthSet
    {:skos/member [:cg/Definitive
                   :cg/Strong
                   :cg/Moderate
                   :cg/Limited
                   :cg/NoKnownDiseaseRelationship
                   :cg/Refuted
                   :cg/Disputed]}

    :cg/VariantZygositySet
    {:skos/member [:cg/Homozygous 
                   :cg/TwoVariantsInTrans
                   :cg/Hemizygous]}
    
    :cg/ClinGenCriteriaSet
    {:skos/member [:cg/GeneValidityCriteria4
                   :cg/GeneValidityCriteria5
                   :cg/GeneValidityCriteria6
                   :cg/GeneValidityCriteria7
                   :cg/GeneValidityCriteria8
                   :cg/GeneValidityCriteria9
                   :cg/GeneValidityCriteria10
                   :cg/GeneValidityCriteria11]}

    :cg/SexSet
    {:skos/member [:cg/AmbiguousSex
                   :cg/Female
                   :cg/Intersex
                   :cg/Male]}

    :cg/EthnicitySet
    {:skos/member [:cg/HispanicOrLatino
                   :cg/NotHispanicOrLatino
                   :cg/UnknownEthnicity]}

    :cg/AgeQualifierSet
    {:skos/member [:cg/AgeAtDeath ,
                   :cg/AgeAtDiagnosis
                   :cg/AgeAtOnset
                   :cg/AgeAtReport]}

    :cg/TimeIntervalSet
    {:skos/member [:cg/Days
                   :cg/Hours
                   :cg/Months
                   :cg/Weeks
                   :cg/WeeksGestation
                   :cg/Years]}

    :cg/EvidenceDirectionSet
    {:skos/member [:cg/Contradicts
                   :cg/Inconclusive
                   :cg/Supports]}

    :cg/VariantDetectionMethodSet
    {:skos/member [:cg/ChromosomalMicroarray
                   :cg/DenaturingGradientGel
                   :cg/ExomeSequencing
                   :cg/Genotyping
                   :cg/HighResolutionMelting
                   :cg/HomozygosityMapping
                   :cg/LinkageAnalysis
                   :cg/GenePanels
                   :cg/OtherVariantDetectionMethod
                   :cg/PCR
                   :cg/RestrictionDigest
                   :cg/SSCP
                   :cg/SangerSequencing
                   :cg/WholeGenomeSequencing]}

    :cg/VariantScoringCategorySet
    {:skos/member [:cg/OtherVariant
                   :cg/NullVariant
                   :cg/BiallelicCompoundHeterozygous
                   :cg/BiallelicHomozygous
                   :cg/MonoallelicHeterozygous]}

    :cg/ChangeReasonSet
    {:skos/member [:cg/NewCuration
                   :cg/DiseaseNameUpdate
                   :cg/ErrorClarification
                   :cg/RecurationCommunityRequest
                   :cg/RecurationTiming
                   :cg/RecurationNewEvidence
                   :cg/RecurationFrameworkChange
                   :cg/RecurationErrorAffectingScoreorClassification
                   :cg/RecurationDiscrepancyResolution]}
    
    }
   
   :concepts
   {:cg/AggregateVariantAnalysis {:rdfs/label "Aggregate variant analysis"},
    :cg/SingleVariantAnalysis {:rdfs/label "Single variant analysis"},
    :cg/CandidateGeneSequencing {:rdfs/label "Candidate gene sequencing"},
    :cg/AllGenesSequencing {:rdfs/label "Exome genome or all genes sequenced in linkage region"},
    
    :cg/Definitive {:rdfs/label "Definitive"},
    :cg/Strong {:rdfs/label "Strong"},
    :cg/Moderate {:rdfs/label "Moderate"},
    :cg/Limited {:rdfs/label "Limited"},
    :cg/NoKnownDiseaseRelationship {:rdfs/label "No Classification"},
    :cg/Refuted {:rdfs/label "Refuted"},
    :cg/Disputed {:rdfs/label "Disputed"},
    
    :cg/Homozygous {:rdfs/label "Homozygous"},
    :cg/TwoVariantsInTrans {:rdfs/label "TwoTrans"},
    :cg/Hemizygous {:rdfs/label "Hemizygous"},
    
    :cg/GeneValidityCriteria4 {:rdfs/label "4"},
    :cg/GeneValidityCriteria5 {:rdfs/label "5"},
    :cg/GeneValidityCriteria6 {:rdfs/label "6"},
    :cg/GeneValidityCriteria7 {:rdfs/label "7"},
    :cg/GeneValidityCriteria8 {:rdfs/label "8"},
    :cg/GeneValidityCriteria9 {:rdfs/label "9"},
    :cg/GeneValidityCriteria10 {:rdfs/label "10"},
    :cg/GeneValidityCriteria11 {:rdfs/label "11"},
    
    :cg/AmbiguousSex {:rdfs/label "Ambiguous"},
    :cg/Female {:rdfs/label "Female"},
    :cg/Intersex {:rdfs/label "Intersex"},
    :cg/Male {:rdfs/label "Male"},
    
    :cg/HispanicOrLatino {:rdfs/label "Hispanic or Latino"},
    :cg/NotHispanicOrLatino {:rdfs/label "Not Hispanic or Latino"},
    :cg/UnknownEthnicity {:rdfs/label "Unknown"},
    
    :cg/AgeAtDeath {:rdfs/label "Death"},
    :cg/AgeAtDiagnosis {:rdfs/label "Diagnosis"},
    :cg/AgeAtOnset {:rdfs/label "Onset"},
    :cg/AgeAtReport {:rdfs/label "Report"},
    
    :cg/Days {:rdfs/label "Days"},
    :cg/Hours {:rdfs/label "Hours"},
    :cg/Months {:rdfs/label "Months"},
    :cg/Weeks {:rdfs/label "Weeks"},
    :cg/WeeksGestation {:rdfs/label "Weeks gestation"},
    :cg/Years {:rdfs/label "Years"},
    
    :cg/Contradicts {:rdfs/label "Contradicts"},
    :cg/Inconclusive {:rdfs/label "none"},
    :cg/Supports {:rdfs/label "Supports"},
    
    :cg/ChromosomalMicroarray {:rdfs/label "Chromosomal microarray"},
    :cg/DenaturingGradientGel {:rdfs/label "Denaturing gradient gel"},
    :cg/ExomeSequencing {:rdfs/label "Exome sequencing"},
    :cg/Genotyping {:rdfs/label "Genotyping"},
    :cg/HighResolutionMelting {:rdfs/label "High resolution melting"},
    :cg/HomozygosityMapping {:rdfs/label "Homozygosity mapping"},
    :cg/LinkageAnalysis {:rdfs/label "Linkage analysis"},
    :cg/GenePanels {:rdfs/label "Next generation sequencing panels"},
    :cg/OtherVariantDetectionMethod {:rdfs/label "Other"},
    :cg/PCR {:rdfs/label "PCR"},
    :cg/RestrictionDigest {:rdfs/label "Restriction digest"},
    :cg/SSCP {:rdfs/label "SSCP"},
    :cg/SangerSequencing {:rdfs/label "Sanger sequencing"},
    :cg/WholeGenomeSequencing {:rdfs/label "Whole genome shotgun sequencing"},
    
    :cg/OtherVariant {:rdfs/label "OTHER_VARIANT_TYPE"},
    :cg/NullVariant {:rdfs/label "PREDICTED_OR_PROVEN_NULL"},
    :cg/BiallelicCompoundHeterozygous {:rdfs/label "Biallelic compound heterozygous"},
    :cg/BiallelicHomozygous {:rdfs/label "Biallelic homozygous"},
    :cg/MonoallelicHeterozygous {:rdfs/label "Monoallelic heterozygous"},

    :cg/NewCuration {:rdfs/label "New Curation"},
    :cg/DiseaseNameUpdate {:rdfs/label "Adminstrative Update Disease Name Update"},
    :cg/ErrorClarification {:rdfs/label "Administrative Update Error Clarification"},
    :cg/RecurationCommunityRequest {:rdfs/label "Recuration Community Request"},
    :cg/RecurationTiming {:rdfs/label "Recuration Due to Timing"},
    :cg/RecurationNewEvidence {:rdfs/label "Recuration New Evidence"},
    :cg/RecurationFrameworkChange {:rdfs/label "Recuration Framework Change"},
    :cg/RecurationErrorAffectingScoreorClassification
    {:rdfs/label "Recuration Error affecting score and or classification"},
    :cg/RecurationDiscrepancyResolution
    {:rdfs/label "Recuration Discrepancy Resolution"}}})

(def schema-by-id
  (apply merge (vals schema)))
