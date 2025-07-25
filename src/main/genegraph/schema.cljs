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
    {:dc/description "A class of resources. Ontological terms are typically represented as classes."
     :properties
     [:rdfs/subClassOf]}
    :cg/Agent
    {:dc/description "An agent is something that bears some form of responsibility for an activity taking place, for the existence of an entity, or for another agent's activity."}
    :cg/ValueSet
    {:dc/description "Terms drawn from one or more terminologies and defined as a separate set, generally for specific coding purposes and given a formal designation."
     :properties
     [:skos/member]}
    :cg/GeneticConditionMechanismProposition
    {:rdfs/label "Genetic Condition Mechanism Proposition"
     :dc/description "The proposition that a change of function of the given feature with the associated mechanism causes the given condition."
     :cg/review true
     :properties
     [:cg/feature :cg/mechanism :cg/condition :cg/assertions]}
    :cg/EvidenceStrengthAssertion
    {:rdfs/label "Evidence Strength Assertion"
     :dc/description "An assertion of the strength of evidence supporting a given proposition."
     :properties
     [:cg/subject :cg/annotations :cg/evidenceStrength :cg/contributions :dc/date :cg/evidence :cg/strengthScore]}
    :cg/Contribution
    {:dc/description "An action or set of actions performed by an agent toward the creation, modification, evaluation, or deprecation of an artifact."
     :properties
     [:cg/agent :dc/date :cg/role]}
    :cg/EvidenceLine ;; add to base
    {:dc/description "An evidence line represents an independent and meaningful argument for or against a particular proposition, that is based on the interpretation of one or more pieces of information as evidence."
     :skos/exactMatch "http://purl.obolibrary.org/obo/SEPIO_0000002"
     :properties
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
     :dc/description "The proposition that variants affecting a gene are causative of a disease, given a mode of inheritance. Such variants are therefore valid to report to patients as pathogenic, given appropriate variant evaluation criteria."
     :properties
     [:cg/gene :cg/modeOfInheritance :cg/disease]}
    :cg/VariantObservation ;; Maybe want to use just Observation, FHIR style? Let's do that
    {:dc/description
     "A recorded instance of observing a specific genetic variant in a sample or individual."
     :properties
     [:cg/proband :cg/variant]}
    :cg/Proband
    {:dc/description "The affected family member who seeks medical attention for a genetic disorder thereby bringing the family under study."
     :cg/note "There are other properties here that should be refactored to other parts of the model. Specifically, zygosity, genotyping_method, etc are part of variant detection."
     :properties
     [:cg/observations :cg/family :cg/phenotype]}
    :cg/FunctionalAlteration
    {:dc/description "A change in protein or gene function caused by a genetic variant, affecting normal biological processes."
     :properties
     []
     :cg/note "Currently does not support properties beyond standard Resource properties"}
    :cg/Affiliation ;; Can keep, but need to sort out the relationships with Agents, etc
    {:dc/description "A ClinGen affiliation (such as a Gene Curation Expert Panel) is one of the groups responsible for producing ClinGen expert knowledge."
     :properties
     []}
    :cg/Finding
    {:dc/description "A specific result, observation, or conclusion that emerges from systematic research or experimentation. It represents new knowledge or evidence discovered through the scientific process."
     :properties
     [:cg/demonstrates :cg/method :cg/statisticalSignificanceType :cg/statisticalSignificanceValue :cg/caseCohort :cg/controlCohort :cg/statisticalSignificanceValueType :cg/lowerConfidenceLimit :cg/upperConfidenceLimit :cg/pValue]}
    :cg/FamilyCosegregation
    {:dc/description
     "Analysis of how genetic variants are inherited together within families to assess pathogenicity."
     :properties
     [:cg/phenotype :cg/phenotypePositiveAllelePositive :cg/family :cg/proband :cg/meetsInclusionCriteria :cg/estimatedLodScore :cg/phenotypeFreeText :cg/publishedLodScore :cg/SequencingMethod :dc/description :cg/phenotypeNegativeAlleleNegative :cg/sequencingMethod]}
    :cg/Family
    {:dc/description "A group of biologically related individuals who share genetic material and are studied together to understand inheritance patterns, genetic disease transmission, or trait heritability."
     :properties
     [:cg/member :cg/ethnicity :cg/modeOfInheritance]}
    :cg/Cohort
    {:dc/description "A defined group of individuals who are followed over time to investigate the relationship between genetic factors and health outcomes or traits."
     :properties
     [:cg/evidence :cg/detectionMethod :cg/allGenotypedSequenced :cg/numWithVariant :cg/alleleFrequency :cg/relatedCondition]}
    :cg/VariantFunctionalImpactEvidence
    {:dc/description "Data demonstrating how a genetic variant affects biological function."
     :properties
     [:cg/functionalDataSupport]
     :cg/note "Find out what is going on with functionalDataSupport"}
    :so/SequenceFeature
    {:dc/description "Any extent of continuous biological sequence."
     :properties
     [:ga4gh/definingLocation :ga4gh/location :owl/sameAs :skos/prefLabel :skos/altLabel :so/ChromosomeBand :skos/hiddenLabel]}
    :dc/BibliographicResource
    {:dc/description "A book, article, or other documentary resource."
     :properties
     [:dc/creator :dc/title :dc/date :dc/abstract :cg/about]}
    ;; [:ga4gh/CanonicalLocation nil] ; need, but need to think about
    ;; [:ga4gh/SequenceLocation nil] ; need -- incorporate from ga4gh schema
    ;; [:ga4gh/VariationDescriptor nil] ; obsolete, but need for now
    }
   
   :properties
   {:rdfs/label {:type 'String}
    :rdf/type {:type (list :owl/Class)}
    :dc/description {:dc/description "An account of the resource."
                     :type 'String}
    :dc/source {:dc/description "A related resource from which the described resource is derived."
                :type :rdfs/Resource}
    :skos/inScheme {:type (list :cg/ValueSet)}
    :skos/member {:type (list :rdfs/Resource)}
    :rdfs/subClassOf {:type (list :owl/Class)}
    :cg/assertions {:type (list :cg/EvidenceStrengthAssertion)}
    :cg/subject {:type :rdfs/Resource}
    :cg/annotations {:type (list :cg/AssertionAnnotation)}
    :cg/evidenceStrength {:type :owl/Class} ;; TODO or skos/concept?
    :cg/contributions {:type (list :cg/Contribution)}
    :dc/date {:dc/description
   "A point or period of time associated with an event in the lifecycle of the resource."
              :type 'String}
    :cg/agent {:type :cg/Agent}
    :cg/role {:type :owl/Class}
    :cg/feature {:type :so/SequenceFeature}
    :cg/observations {:type (list :cg/VariantObservation)}
    :skos/prefLabel {:type 'String}
    :cg/variant {:type :cg/CanonicalVariant}
    :cg/pValue {:type 'Float}
    :cg/classification {:type :owl/Class}
    :dc/creator {:dc/description "An entity responsible for making the resource."
                 :type :cg/Agent}
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
    :cg/upperConfidenceLimit {}           ; investigate
    :cg/member {:type (list :cg/Proband)} ; update to family member?
    :cg/disease {:type :owl/Class}
    :dc/abstract {:dc/description "A summary of the resource."
                  :type 'String}
    :cg/statisticalSignificanceValueType {} ;;investigate
    :cg/evidence {:type (list :rdfs/Resource)}
    :dc/title {:dc/description "A name given to the resource."
               :type 'String}
    :cg/ethnicity {:type :owl/Class}
    :cg/sequencingMethod {:type :owl/Class}
    :cg/meetsInclusionCriteria {}           ; boolean?
    :cg/relatedCondition {:type :owl/Class} ; also investigate
    :cg/phenotypeNegativeAlleleNegative {}  ; boolean?
    :cg/statisticalSignificanceType {:type :owl/Class} ; also investigate
    :cg/phenotypePositiveAllelePositive {}             ; boolean?
    :cg/modeOfInheritance {:type :owl/Class :cg/note "domain is HPO terms for MOI"} 
    :cg/demonstrates {}             ; investigate, think is :owl/Class
    :ga4gh/location {}              ; bring in from GA4GH
    :cg/proband {:type :cg/Proband}
    :cg/condition {:type :owl/Class}
    :owl/sameAs {:type (list :owl/Class)}
    :cg/strengthScore {:type 'Float}
    :cg/phenotypeFreeText {:type 'String}
    :cg/controlCohort {}                ; investigate
    :cg/alleleMappings {}               ; ga4gh allele list
    :cg/gene {:type :so/SequenceFeature}
    :so/ChromosomeBand {:type 'String} ; should maybe be sequence feature
    :cg/detectionMethod {:type :owl/Class} ;; investigate may actually be 'String
    :cg/estimatedLodScore {:type 'Float}
    :cg/statisticalSignificanceValue {} ;; investigate
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

(def type-mapping
  {:interfaces :cg/Interface
   :properties :rdf/Property
   :classes :owl/Class
   :value-sets :cg/ValueSet
   :concepts :skos/Concept})

(defn add-types [schema]
  (apply
   merge 
   (map
    (fn [[k v]]
      (update-vals v (fn [v1] (assoc v1 :rdf/type (get type-mapping k)))))
    schema)))

#_(defn add-member-refs [schema]
  (reduce
   (fn [s1 [vs-kw vs]]
     (reduce
      (fn [s2 concept] (update s2 concept assoc :skos/inScheme vs-kw))
      s1
      (:skos/member vs)))
   schema
   (filter (fn [[k v]] (= :cg/ValueSet (:rdf/type v))) schema)))

(defn append-item [m k i]
  (if-let [v (get m k)]
    (assoc m k (conj v i))
    (assoc m k [i])))

(defn add-member-refs [schema]
  (reduce
   (fn [s1 [vs-kw vs]]
     (reduce
      (fn [s2 concept] (update s2 concept append-item :skos/inScheme vs-kw))
      s1
      (:skos/member vs)))
   schema
   (filter (fn [[k v]] (= :cg/ValueSet (:rdf/type v))) schema)))

(defn add-property-class-refs [schema]
  (reduce
   (fn [s1 [vs-kw vs]]
     (reduce
      (fn [s2 concept] (update s2 concept append-item :used-in vs-kw))
      s1
      (:properties vs)))
   schema
   (filter (fn [[k v]] (= :owl/Class (:rdf/type v))) schema)))

(def schema-by-id
  (-> (add-types schema)
      add-member-refs
      add-property-class-refs))
