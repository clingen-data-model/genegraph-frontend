(ns genegraph.schema
  (:require [reitit.frontend.easy :as rfe]
            [genegraph.frontend.common :as common]))

;; copied from genegraph.frontend.page.documentation
;; need to decide on appropriate place for this to live
(defn term-href [term]
  (rfe/href :routes/documentation-term {:id (common/kw->iri-id term)}))

(defn entity [e]
  [:a
   {:href (term-href e)
    :class "font-medium text-sky-700"}
   e])

#_(defn schema []
  {})

(defn schema []
    {:interfaces
     {:rdfs/Resource
      {:description "An RDF Resource in Genegraph. Common interface to all entities in Genegraph; all entities will inherit from this at some level."

       ;; we'll see how things work,
       ;; but IRI and CURIE might be special--will have to deal later
       :properties
       [:rdfs/label :rdf/type :dc/description :dc/source :skos/inScheme]}}
     :classes
     {:owl/Class
      {:dc/description "A class of resources. Ontological terms are typically represented as classes."
       :properties
       [:rdfs/subClassOf]}
      :cg/Agent
      {:dc/description "An agent is something that bears some form of responsibility for an activity taking place, for the existence of an entity, or for another agent's activity."
       :rdfs/subClassOf :rdfs/Resource}
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
       :markup
       [:div
        {:class "flex flex-col gap-4"}
        [:p
         "An "
         (entity :cg/EvidenceStrengthAssertion)
         " sits at the core of every ClinGen curation. It represents the assertion that a given Clinical Domain Working Group (CDWG) has found the evidence in support of the "
         (entity :cg/subject)
         " "
         (entity :cg/Proposition)
         " reaches a certain "
         (entity :cg/evidenceStrength)
         " as "
         (entity :cg/specifiedBy)
         " the SOP used by the group. The "
         (entity :rdf/type)
         " of the "
         (entity :cg/Proposition)
         " defines the type of curation performed; a Gene Validity curation will have a "
         (entity :cg/Proposition)
         " of type "
         (entity :cg/GeneValidityProposition)
         " that contains the "
         (entity :cg/gene)
         ", "
         (entity :cg/disease)
         ", and "
         (entity :cg/modeOfInheritance)
         " for the curation."]
        [:p
         "It will typically have one or more "
         (entity :cg/contributions)
         ". A given "
         (entity :cg/Contribution)
         " may include a "
         (entity :cg/date)
         " on which the activity is considered to have been performed. For example, the date given for the "
         (entity :cg/Approver)
         " represents the date on which the curation was approved by the CDWG. Evidence after this date would not have been considered for the overall "
         (entity :cg/evidenceStrength)
         ". The date given for the "
         (entity :cg/Publisher)
         " represents the date the curation was published to the ClinGen Data Exchange, and therefore Genegraph and the ClinGen Website."]]
       :properties
       [:cg/GCISnapshot
        :cg/annotations
        :cg/calculatedEvidenceStrength
        :cg/changes
        :cg/contributions
        :cg/curationReasons
        :dc/date
        :dc/description
        :cg/evidence
        :cg/evidenceStrength
        :dc/isVersionOf
        :cg/sequence
        :cg/specifiedBy
        :cg/strengthScore
        :cg/subject
        :cg/version]}
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
       :rdfs/subClassOf :cg/Resource
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
       :rdfs/subClassOf :rdfs/Resource
       :dc/description "Annotation on an assertion. May be used to annotate whether the person performing the annotation agrees with the assertion or not."
       :properties
       [:cg/subject :cg/evidence :cg/classification :cg/contributions]}
      :cg/GeneValidityProposition
      {:rdfs/label "Gene Validity Proposition"
       :dc/description "The proposition that variants affecting a gene are causative of a disease, given a mode of inheritance. Such variants are therefore valid to report to patients as pathogenic, given appropriate variant evaluation criteria."
       :rdfs/subClassOf :cg/Proposition
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
       :rdfs/subClassOf :cg/Agent
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
       [:cg/member :cg/ethnicity :cg/modeOfInheritance]} ;; TODO mode of inheritance is weird here investigate
      :cg/Cohort ;; TODO validate use of fields and significance
      {:dc/description "A defined group of individuals who are followed over time to investigate the relationship between genetic factors and health outcomes or traits."
       :rdfs/subClassOf :cg/Resource
       :properties
       [:cg/evidence :cg/detectionMethod :cg/allGenotypedSequenced :cg/numWithVariant :cg/alleleFrequency :cg/relatedCondition]
       :cg/note "Investigate why the evidence property is used here."}
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
      :cg/Proposition
      {:dc/Description "An abstract entity representing a possible fact that may be true or false. As abstract entities, Propositions capture a ‘sharable’ piece of meaning whose identify and existence is independent of space and time, or whether it is ever asserted to be true by some agent."
       :xref "https://va-spec.ga4gh.org/en/latest/core-information-model/entities/proposition.html#proposition"}
      ;; [:ga4gh/CanonicalLocation nil] ; need, but need to think about
      ;; [:ga4gh/SequenceLocation nil] ; need -- incorporate from ga4gh schema
      ;; [:ga4gh/VariationDescriptor nil] ; obsolete, but need for now
      }
   
     :properties
     {:rdfs/label {:type 'String
                   :dc/description "A human-readable name for the subject."}
      :rdf/type {:type '(list :owl/Class)
                 :dc/description "The subject is an instance of a class."}
      :dc/description {:dc/description "An account of the resource."
                       :type 'String}
      :dc/source {:dc/description "A related resource from which the described resource is derived."
                  :type :rdfs/Resource}
      :skos/inScheme {:type '(list :cg/ValueSet)
                      :dc/description "Relates a resource (for example a concept) to a concept scheme in which it is included."}
      :skos/member {:type '(list :rdfs/Resource)}
      :rdfs/subClassOf {:type '(list :owl/Class)}
      :cg/assertions {:type '(list :cg/EvidenceStrengthAssertion)}
      :cg/subject {:type :cg/Proposition
                   :dc/description "The subject of this entity, typically a Proposition or an Assertion."}
      :cg/annotations {:type '(list :cg/AssertionAnnotation)
                       :dc/description "Annotations made on the assertion by ClinGen curators; relevant for the ClinGen Curation of ClinVar project."}
      :cg/evidenceStrength {:type :owl/Class
                            :domain :cg/EvidenceStrengthSet
                            :dc/description "Classification of the evidence according to the criteria used for the assertion."}
      :cg/contributions {:type '(list :cg/Contribution)
                         :dc/description "Actions taken by an agent contributing to the creation, modification, assessment, or deprecation of a particular entity (e.g. a Statement, EvidenceLine, DataSet, Publication, etc.)"}
      :dc/date {:dc/description
                "A point or period of time associated with an event in the lifecycle of the resource."
                :type 'String}
      :cg/agent {:type :cg/Agent
                 :dc/description "The agent associated with this contribution."}
      :cg/role {:type :owl/Class
                :dc/description "The role performed by the agent making this contribution."
                :domain :cg/ContributionSet}
      :cg/feature {:type :so/SequenceFeature}
      :cg/observations {:type '(list :cg/VariantObservation)}
      :skos/prefLabel {:type 'String}
      :cg/variant {:type :cg/CanonicalVariant}
      :cg/pValue {:type 'Float}
      :cg/classification {:type :owl/Class
                          :dc/description "The classification of the subject entity"}
      :dc/creator {:dc/description "An entity responsible for making the resource."
                   :type :cg/Agent}
      :cg/functionalDataSupport {} ;; investigate
      :cg/about {:type :rdfs/Resource}
      :cg/method {:type :owl/Class}
      :skos/altLabel {:type 'String}
      :cg/caseCohort {} ;; investigate
      :cg/publishedLodScore {:type 'Float}
      :cg/allGenotypedSequenced {} ;; investigate, boolean?
      :cg/allele {:type :ga4gh/Allele
                  :dc/description "The allele defining a CanonicalVariant. Any allele that can map to this allele. is condidered to be included in the set of variants defined by this CanonicalVariant"} ;; source CannonicalAllele, refers to ga4gh allele
      :cg/numWithVariant {:type 'Int}
      :ga4gh/definingLocation {}  ;; ga4gh location
      :cg/lowerConfidenceLimit {} ;; investigate
      :cg/family {:type :cg/Family
                  :dc/description "The family demonstrating the segregation."}
      :cg/alleleFrequency {} ;; investigate
      :cg/phenotype {:type :owl/Class
                     :dc/description "The phenotype used for the segregation study. Recommended to use terms from Human Phenotype Ontology"
                     :cg/note "recommended to use HPO terms"}
      :cg/upperConfidenceLimit {}           ; investigate
      :cg/member {:type '(list :cg/Proband)
                  :dc/description "Members of the family."} ; update to family member?
      :cg/disease {:type :owl/Class}
      :dc/abstract {:dc/description "A summary of the resource."
                    :type 'String}
      :cg/statisticalSignificanceValueType {} ;;investigate
      :cg/evidence {:type '(list :rdfs/Resource)
                    :dc/description "Evidence relating to the assertion or evidence line."}
      :dc/title {:dc/description "A name given to the resource."
                 :type 'String}
      :cg/ethnicity {:type :owl/Class
                     :dc/description "Ethnicity of the family"
                     :domain :cg/EthnicitySet}
      :cg/sequencingMethod {:type :owl/Class}
      :cg/meetsInclusionCriteria {:type 'Boolean
                                  :dc/description "Whether the study meets inclusion criteria for consideration in the Gene Validity Evaluation framework."}           ; boolean? TODO consider rename
      :cg/relatedCondition {:type :owl/Class} ; also investigate
      :cg/phenotypeNegativeAlleleNegative {}  ; boolean?
      :cg/statisticalSignificanceType {:type :owl/Class} ; also investigate
      :cg/phenotypePositiveAllelePositive {:type 'Boolean
                                           :dc/description "Whether family members positive for the inherited allele demonstrate the given phenotype."}             ; boolean?
      :cg/modeOfInheritance {:type :owl/Class
                             :cg/note "domain is HPO terms for MOI"
                             :dc/description "The clinical validity of variants is contrained by this mode of inheritance. Should be a subclass of HPO:0000005 (mode of inheritance)"} 
      :cg/demonstrates {}           ; investigate, think is :owl/Class
      :ga4gh/location {}            ; bring in from GA4GH
      :cg/proband {:type :cg/Proband
                   :dc/description "The first person in the family presenting with the condition to clinicians or researchers."}
      :cg/condition {:type :owl/Class}
      :owl/sameAs {:type '(list :owl/Class)}
      :cg/strengthScore {:type 'Float
                         :dc/description "A quantitative score indicating the strength of support that an Evidence Line is determined to provide for or against its target Proposition, evaluated relative to the direction indicated by the directionOfEvidenceProvided value."}
      :cg/phenotypeFreeText {:type 'String}
      :cg/controlCohort {}              ; investigate
      :cg/alleleMappings {:type '(list :ga4gh/Allele)
                          :dc/description "A set of alleles known to map to this CanonicalVariant."}             ; ga4gh allele list
      :cg/gene {:type :so/SequenceFeature
                :dc/description "Variants affecting this gene are valid for clinical classification."}
      :so/ChromosomeBand {:type 'String} ; should maybe be sequence feature
      :cg/detectionMethod {:type :owl/Class
                           :dc/description "Variant detection method used for the relevant study."
                           :domain :cg/VariantDetectionMethodSet
                           } ;; investigate may actually be 'String
      :cg/estimatedLodScore {:type 'Float
                             :dc/description "LOD score"}
      :cg/statisticalSignificanceValue {} ;; investigate
      :skos/hiddenLabel {:type '(list 'String)}
      :cg/mechanism {:type :owl/Class}
      :cg/GCISnapshot {:type 'String :dc/description "Internal identifier from the Gene Curation Interface."}
      :cg/calculatedEvidenceStrength {:type :owl/Class
                                      :domain :cg/EvidenceStrengthSet
                                      :dc/description "Evidence strength calculated by the algorithm for the curation criteria (SOP), prior to any adjustment by the Expert Panel."}
      :cg/changes {:type '(list :owl/Class)
                   :domain :cg/ChangeReasonSet
                   :dc/description "List of elements changed in this version relative to the previous version"}
      :cg/curationReasons {:type '(list :owl/Class)
                           :domain :cg/CurationReasonSet
                           :dc/description "Reasons listed for performing the curation."}
      :dc/isVersionOf {:type :rdfs/Resource
                       :dc/description "A related resource of which the described resource is a version, edition, or adaptation. Used in ClinGen resources to link version of a curation together using a consistent identifier."}
      :cg/sequence {:type 'Int
                    :dc/description "Sequence in which this curation was published to the ClinGen data exchange."}
      :cg/specifiedBy {:type :owl/Class
                       :domain :cg/ClinGenCriteriaSet
                       :dc/description "Criteria used to support the assessment made. Provides a reference for the significance of the score, and the methodology used to achieve said score."}
      :cg/version {:type 'String
                   :dc/description "Version of the given resource, using ClinGen's Semantic Versioning Scheme."}
      }

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

      :cg/CurationReasonSet
      {:skos/member [:cg/NewCuration
                     :cg/DiseaseNameUpdate
                     :cg/ErrorClarification
                     :cg/RecurationCommunityRequest
                     :cg/RecurationTiming
                     :cg/RecurationNewEvidence
                     :cg/RecurationFrameworkChange
                     :cg/RecurationErrorAffectingScoreorClassification
                     :cg/RecurationDiscrepancyResolution]}

      :cg/ContributionSet
      {:skos/member [:cg/Approver
                     :cg/Publisher
                     :cg/SecondaryContributor]}
    
      }
   
     :concepts
     {:cg/AggregateVariantAnalysis {:rdfs/label "Aggregate variant analysis"},
      :cg/SingleVariantAnalysis {:rdfs/label "Single variant analysis"},
      :cg/CandidateGeneSequencing {:rdfs/label "Candidate gene sequencing"},
      :cg/AllGenesSequencing {:rdfs/label "Exome genome or all genes sequenced in linkage region"},
    
      :cg/Definitive {:rdfs/label "Definitive"
                      :dc/description "The role of this gene in this particular disease has been repeatedly demonstrated in both the research and clinical diagnostic settings, and has been upheld over time."},
      :cg/Strong {:rdfs/label "Strong"
                  :dc/description "Gene-disease
pairs with strong evidence demonstrate considerable genetic evidence (numerous unrelated
probands harboring variants with sufficient supporting evidence for disease causality)."},
      :cg/Moderate {:rdfs/label "Moderate"
                    :dc/description "There is moderate evidence to support a causal role for this gene in this disease. Gene-disease pairs with moderate evidence typically demonstrate some convincing genetic evidence (probands harboring variants with sufficient supporting evidence for disease
causality with or without moderate experimental data supporting the gene-disease relationship)."},
      :cg/Limited {:rdfs/label "Limited"
                   :dc/description "In general, the category of limited should be applied when experts consider the gene-disease relationship to be plausible, but the evidence is not sufficient to score as Moderate."},
      :cg/NoKnownDiseaseRelationship {:rdfs/label "No Classification"
                                      :dc/description "Evidence for a causal role in the monogenic disease of interest (determined using ClinGen lumping and splitting guidance) has not been reported within the literature (published, prepublished and/or present in public databases [e.g. ClinVar , etc.]). These genes might be “candidate” genes based on linkage intervals, animal models, implication in pathways known to be involved in human disease, etc., but no reports have directly implicated the gene in the specified disease."},
      :cg/Refuted {:rdfs/label "Refuted"
                   :dc/description "Evidence refuting the initial reported evidence for the role of the gene in the specified disease has been reported and significantly outweighs any evidence supporting the role. This designation is to be applied at the discretion of clinical domain experts after thorough review of available data. "},
      :cg/Disputed {:rdfs/label "Disputed"
                    :dc/description "Although there has been an assertion of a gene-disease relationship, the initial evidence is not compelling from today’s perspective and/or conflicting evidence has arisen."},
    
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

      :cg/Approver {:rdfs/label "Approver"
                    :dc/description "The approval of a given resource."}
      :cg/Publisher {:rdfs/label "Publisher"
                     :dc/description "The publication of a given resource."}
      :cg/SecondaryContributor {:rdfs/label "Secondary Contributor"
                                :dc/description "An additional contribution to the resource, typically made by a group with complementary expertise to the primary contributor."}
      
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

(defn schema-by-id []
  (-> (add-types (schema))
      add-member-refs
      add-property-class-refs))
 
