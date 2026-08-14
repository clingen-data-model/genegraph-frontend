(ns genegraph.new-schema)

(def schema
  [ ;; classes
 {:id :cg/Statement,
  :description "A claim of purported truth as made by a particular agent, on a particular occasion. Statements may be used to simply put forth a possible fact (i.e. a 'proposition') as true, or to provide a more nuanced assessment of the level of confidence or evidence supporting a particular proposition."
  :type :rdfs/Class
  :reference :sepio/Statement
  :note "Some values ommitted when records are unpublished."
  :oneOf [:cg/classification
          :cg/direction
          :cg/proposition
          :cg/specifiedBy
          :cg/version
          :dc/description]
  :oneOrMoreOf [:cg/contributions]
  :zeroOrOneOf [:cg/curationReasonDescription
                :cg/curationReasons
                :cg/GCISnapshot ;; one of, except when applied to other statements
                :cg/calculatedClassification ;; same
                :cg/score
                :cg/sequence
                :dc/isVersionOf]
  :zeroOrMoreOf [:cg/hasEvidenceLines
                 :cg/unusedReferences]}
 {:id :cg/GeneFunctionStudyResult,
  :description "The result of an experimental study of gene function,
      curated as gene-level experimental evidence. Only evidence supporting
      the role of the gene in the disease, or in phenotypic features related
      to the disease entity of interest, is scored here; evidence that a
      variant merely damages gene function is instead used to upgrade
      case-level variant scoring."
  :type :rdfs/Class
  :maturity :cg/Draft
  :oneOf [:cg/interpretation
          :rdfs/label
          :dc/source]
  :zeroOrOneOf [:cg/modelSystem
                :dc/description]        ; 124 without a description
  }
 {:id :cg/GeneDiseaseValidityProposition,
  :description "The proposition that variation in a given gene causes a given
      disease under a given mode of inheritance."
  :type :rdfs/Class
  :maturity :cg/Draft
  :oneOf [:cg/predicate
          :cg/modeOfInheritanceQualifier
          :cg/objectCondition           ; 2 with doubled conditions
          :cg/subjectGene]}
 {:id :cg/CaseControlStudyResult,
  :description "The result of a study in which statistical analysis is used
      to evaluate enrichment of variants in cases compared to controls. Each
      study is assessed independently on variant detection methodology,
      statistical power, bias and confounding factors."
  :type :rdfs/Class
  :maturity :cg/Draft
  :internal-note "Examine values for alignment. Let's try to get everything here to draft if possible"
  :oneOf [:cg/caseCohort
          :cg/controlCohort
          :rdfs/label
          :dc/source]
  :zeroOrOneOf [:cg/lowerConfidenceLimit
                :cg/upperConfidenceLimit
                :cg/statisticalSignificanceValueType
                :cg/statisticalSignificanceType
                :cg/statisticalSignificanceValue
                :cg/pValue
                :dc/description]
  :oneOrMoreOf [:cg/supportingMethodTypes]} ; only one of, but attribute is plural
 {:id :cg/VariantObservationStudyResult
  :description "The observation of a particular variant in a particular
      individual, together with the zygosity and origin of that variant,
      forming the variant component of case-level genetic evidence."
  :type :rdfs/Class
  :maturity :cg/Draft
  :oneOf [:rdf/type
          :dc/source
          :cg/variant]
  :zeroOrOneOf [:cg/zygosity
                :cg/variantObservedIn
                :cg/paternityMaternityConfirmed
                :cg/variantOrigin]}
 {:id :cg/Family
  :description "A family whose members are genotyped to determine whether a
      variant co-segregates with disease. A family may be used in a LOD
      score calculation only when it meets the size requirements for the
      mode of inheritance and demonstrates no unexplainable
      non-segregations."
  :type :rdfs/Class
  :maturity :cg/Draft
  :oneOf [:rdf/type
          :rdfs/label]
  :zeroOrMoreOf [:cg/member] ; really a oneOf, but could have more than one
  :zeroOrOneOf [:cg/modeOfInheritance
                :cg/ethnicity]
  :attributes []}
 {:id :cg/Cohort
  :description "A group of individuals constituting either the case or the
      control arm of a case-control study. Cases and controls should be
      analyzed by methods of equivalent analytical performance and matched
      on demographic and genetic ancestry, and the arms should be large
      enough to provide statistical power given disease prevalence, allele
      frequency, and expected effect size."
  :type :rdfs/Class
  :maturity :cg/Volatile
  :oneOf [:rdf/type
          :cg/numberGenotyped
          :cg/numberWithVariant]
  :zeroOrOneOf [:cg/detectionMethodText
                :cg/alleleFrequency     ; only 43 without
                :cg/relatedCondition] ; may want to make this one or more 
  }
 {:id :cg/UnscoreableEvidence
  :description "Evidence recorded for documentation and expert review but
      excluded from scoring. Curators mark evidence as nonscorable when it
      cannot be scored under the framework, for example when the phase of
      biallelic variants is entirely unknown."
  :type :rdfs/Class
  :maturity :cg/Internal
  :oneOf [:rdf/type
          :dc/source]
  :zeroOrOneOf [:dc/description]}
 {:id :cg/EvidenceLine
  :type :rdfs/Class
  :reference :sepio/EvidenceLine
  :description "An independent, evidence-based argument that may support or refute the validity of a specific proposition. The strength and direction of this argument is based on an interpretation of one or more pieces of information as evidence for or against the target proposition."
  :oneOf [:rdf/type
          :cg/specifiedBy]
  :zeroOrOneOf [:cg/directionOfEvidenceProvided ; should provably have this for everything
                :cg/scoreOfEvidenceProvided
                :cg/calculatedScore
                :dc/description]
  :zeroOrManyOf [:cg/hasEvidenceItems] ; one without evidence items, investigate? also one with > 100
  }
 {:id :cg/SegregationStudyResult
  :description "The result of a segregation study, in which family members
      are genotyped to determine whether a variant co-segregates with
      disease. LOD scores from all families meeting size requirements are
      summed, and points are then assigned according to the sequencing
      method used to investigate the linkage interval. Segregation
      implicates a locus rather than a variant, so studies must be assessed
      for whether other genes in the critical region were ruled out."
  :type :rdfs/Class
  :maturity :cg/Draft
  :oneOf [:rdf/type
          :dc/source
          :rdfs/label
          :cg/family]
  :zeroOrOneOf [:cg/proband
                :cg/meetsInclusionCriteria
                :cg/LODScore
                :cg/publishedLODScore
                :cg/estimatedLODScore
                :cg/phenotypeNegativeAlleleNegative
                :cg/phenotypePositiveAllelePositive
                :cg/phenotypeFreeText
                :dc/description
                :cg/supportingMethodTypes]
  :zeroOrManyOf [:cg/phenotypes]
  }
 {:id :cg/VariationDescriptor
  :type :rdfs/Class
  :maturity :cg/Volatile
  :description "A reference to a variant."
  :note "The GA4GH used to have a similar concept. This was removed from the spec but has not yet been replaced by an equivalent term."
  :zeroOrOneOf [:skos/prefLabel]        ;6 w/o
  :oneOf [:rdf/type
          :cg/canonicalReference]}   ; 197 w/o -- should invesitigate.
 {:id :cg/ProbandStudyResult
  :description "Case-level evidence describing a single proband: the
      variants identified, the phenotype documented, and the prior testing
      performed. A proband is counted toward supporting evidence only when
      the diagnosis is sufficiently documented and the variant is a
      plausible cause of disease, and may be scored for both variant
      evidence and segregation. No single proband may score more than 3
      points of genetic evidence."
  :type :rdfs/Class
  :oneOf [:rdf/type
          :dc/source
          :rdfs/label]
  :zeroOrOneOf [:cg/ageType
                :cg/ageUnit
                :cg/ageValue
                :cg/detectionMethodText
                :cg/ethnicity
                :cg/geneticTestingMethod
                :cg/genotypeConfirmationMethod
                :cg/previousTesting
                :cg/previousTestingDescription
                :cg/zygosity
                :cg/phaseStatusConfidence
                :cg/phenotypeFreeText
                :cg/sex]
  :oneOrMoreOf [:cg/hasVariant]
  :zeroOrMoreOf [:cg/phenotypes]}
 {:id :cg/Contribution,
  :type :rdfs/Class
  :reference :sepio/Contribution
  :description "An action or actions taken by a particular agent in the creation, modification, assessment, or deprecation of some entity (e.g. a Statement, Evidence Line, DataSet, Publication, etc.)."
  :oneOf [:rdf/type
          :cg/date                      ; 2 without, investigate
          :cg/activityType
          :cg/contributor]}
 {:id :cg/VariantFunctionalImpactEvidence
  :description "Evidence bearing on whether a variant is damaging to the
      function of the gene product. Functional impact must be experimentally
      demonstrated rather than predicted; in silico predictions alone are
      not generally sufficient."
  :type :rdfs/Class
  :maturity :cg/Volatile
  :oneOf [:rdf/type
          :cg/functionalDataSupport
          :dc/description]}
 ;; properties
 {:id :cg/unusedReferences
  :type :rdf/Property
  :maturity :cg/Internal
  :range :cg/Resource
  :description "References noted by the curator but not used in making the final classification. Typically the reason the references were unused will be noted; often they contain information that cannot be used when the relevant curation SOP is applied."}
 {:id :cg/GCISnapshot,
  :type :rdf/Property
  :maturity :cg/Internal
  :range :cg/Resource}
 {:id :cg/activityType,
  :type :rdf/Property
  :reference :sepio/activityType
  :description "The specific type of activity performed or role played by an agent in making the contribution (e.g. for a publication, agents may contribute as a primary author, editor, figure designer, data generator, etc. . Values of this property may be framed as activities or as contribution roles (e.g. using terms from the Contribution Role Ontology (CRO))."
  :range :skos/Concept
  :value-set :cg/ActivityTypeValueSet}
 {:id :cg/ageType
  :type :rdf/Property
  :maturity :cg/Volatile
  :note "Age and duration units have yet to be aligned at GA4GH level; will adjust to conform to standard as it is developed."
  :range :skos/Concept
  :value-set :cg/AgeTypeValueSet}
 {:id :cg/ageUnit
  :type :rdf/Property
  :maturity :cg/Volatile
  :range :skos/Concept
  :value-set :cg/AgeUnitValueSet
  :note "Age and duration units have yet to be aligned at GA4GH level; will adjust to conform to standard as it is developed."}
 {:id :cg/ageValue
  :type :rdf/Property
  :maturity :cg/Volatile
  :range :Number
  :note "Age and duration units have yet to be aligned at GA4GH level; will adjust to conform to standard as it is developed."}
 {:id :cg/numberGenotyped
  :description "The total number of individuals in the cohort who were
      genotyped or sequenced."
  :type :rdf/Property
  :maturity :cg/Draft
  :range :Number}
 {:id :cg/variant
  :description "The variant observed in an individual within a variant
      observation study result."
  :type :rdf/Property
  :maturity :cg/Draft
  :range :cg/VariationDescriptor
  :internal-note "now tied only to VariantObservationStudyResult"}
 {:id :cg/alleleFrequency,
  :description "The frequency of the allele in the cohort or in a reference
      population. For a variant to be considered potentially
      disease-causing, its population frequency must be consistent with
      disease prevalence, penetrance, and inheritance pattern."
  :type :rdf/Property
  :range :Number
  :maturity :cg/Draft}
 {:id :cg/variantOrigin
  :description "The origin of the observed allele, in particular whether it
      arose de novo. A variant is de novo when it is present in an affected
      individual but absent from appropriately tested parents, or when a
      parent carries it mosaically."
  :type :rdf/Property
  :range :skos/Concept
  :value-set :cg/VariantOriginValueSet
  :maturity :cg/Draft}
 {:id :cg/calculatedClassification
  :description "The classification assigned automatically from the summed
      points: Limited for 0.1-6, Moderate for 7-11, and Strong or Definitive
      for 12-18. An expert panel may modify it by at most one level, except
      that Disputed and Refuted may be chosen regardless of the calculated
      value."
  :type :rdf/Property
  :maturity :cg/Internal
  :range :skos/Concept
  :value-set :cg/ClassificationValueSet
  :note "Classification calculated based on sum of scores. May be adjusted by curator."}
 {:id :cg/calculatedScore,
  :description "The score computed automatically from the recorded evidence,
      before any adjustment by a curator or expert panel."
  :type :rdf/Property
  :maturity :cg/Internal
  :range :Number
  :note "Automatic score for EvidenceLine. Adjusted score by curator used for final classification."}
 {:id :cg/canonicalReference,
  :type :rdf/Property
  :maturity :cg/Volatile
  :range :cg/Variation
  :internal-note "only used as ID, consider ways to mark ID only fields in model."
  :note "Part of Variant Descriptor, currently looking for a replacement in GA4GH"}
 {:id :cg/caseCohort,
  :description "The cohort of affected individuals in a case-control study."
  :type :rdf/Property
  :range :cg/Cohort
  :maturity :cg/Draft}
 {:id :cg/classification,
  :type :rdf/Property
  :range :skos/Concept
  :value-set :cg/ClassificationValueSet
  :reference :sepio/classification
  :description "A single term or phrase summarizing the outcome of direction and strength assessments of a Statement's proposition, in terms of a classification of its subject."}
 {:id :cg/contributions,
  :type :rdf/Property
  :range :cg/Contribution
  :reference :sepio/contributions
  :description "describes specific contributions made by an human or software agent to the creation, modification, or administrative management of a data record or object."}
 {:id :cg/contributor,
  :type :rdf/Property
  :range :cg/Agent
  :internal-note "Is PROV stable enough to include directly for types? How does this interact with the CAM model?"
  :reference :sepio/contributor
  :description "The Agent that made the contribution."}
 {:id :cg/controlCohort,
  :description "The cohort of unaffected individuals against which cases are
      compared in a case-control study."
  :type :rdf/Property
  :range :cg/Cohort
  :maturity :cg/Draft}
 {:id :cg/curationReasonDescription,
  :description "Free-text rationale accompanying the reason a classification
      was published or republished."
  :type :rdf/Property
  :range :String
  :maturity :cg/Internal}
 {:id :cg/curationReasons,
  :description "The reason a classification is being published or
      republished. Curators must select one or more reasons, which determine
      the version number assigned to the curation."
  :type :rdf/Property
  :range :skos/Concept
  :value-set :cg/CurationReasonsValueSet
  :maturity :cg/Internal}
 {:id :cg/date,
  :type :rdf/Property
  :range :String
  :note "Expect ISO-8601 DateTime"
  :reference :sepio/date
  :description "The date that the Activity was completed."}
 {:id :cg/detectionMethodText,
  :description "Free-text description of the variant detection methodology.
      Cases and controls should ideally be analyzed using methods of
      equivalent analytical performance, including equivalent depth and
      quality of sequencing coverage."
  :type :rdf/Property
  :maturity :cg/Volatile
  :range :String
  :note "Free-text description of sequencing method."
  :internal-note "Probably not going to change--just carrying additional GCI Data"}
 {:id :cg/direction,
  :type :rdf/Property
  :reference :sepio/direction
  :range :skos/Concept
  :value-set :cg/DirectionValueSet
  :description "A term indicating whether the Statement supports, disputes, or remains neutral w.r.t. the validity of the Proposition it evaluates."}
 {:id :cg/directionOfEvidenceProvided,
  :type :rdf/Property
  :reference :sepio/directionOfEvidenceProvided
  :range :skos/Concept
  :value-set :cg/DirectionValueSet
  :description "The direction of support that the Evidence Line is determined to provide toward its target Proposition (supports, disputes, neutral)."}
 {:id :cg/LODScore,
  :description "The logarithm of the odds score quantifying the strength of
      co-segregation between a variant and disease in a family. Within this
      framework a simplified estimate assuming a recombination fraction of
      zero is used, and scores from all qualifying families are summed
      before points are assigned."
  :type :rdf/Property
  :maturity :cg/Draft
  :range :Number}
 {:id :cg/estimatedLODScore,
  :description "A LOD score estimated by the curator using the simplified
      formula, permitted only when the disorder is rare and highly
      penetrant, phenocopies are rare or absent, the family meets the
      minimum size for the mode of inheritance, and no unexplainable
      non-segregations are present."
  :type :rdf/Property
  :maturity :cg/Internal
  :range :Number
  :note "Reflects distinction between LOD score included in published literature and LOD score estimated using ClinGen Gene Curation SOP."}
 {:id :cg/ethnicity,
  :description "The reported ethnicity of an individual or cohort. Ancestry
      matching between cases and controls bears directly on whether a
      case-control association is credible."
  :type :rdf/Property
  :maturity :cg/Draft
  :range :skos/Concept
  :value-set :cg/EthnicityValueSet}
 {:id :cg/family,
  :type :rdf/Property
  :range :cg/Family
  :maturity :cg/Draft
  :description "Family referenced in SegregationStudyResult"}
 {:id :cg/geneticTestingMethod,
  :description "The method by which the variant was identified. Exome or
      genome sequencing provides more convincing evidence than a candidate
      gene approach, in which only one or a handful of genes are sequenced."
  :type :rdf/Property
  :maturity :cg/Draft
  :range :skos/Concept
  :value-set :cg/GeneticTestingMethodValueSet
  :internal-note "Need some thought around the terminology here. May be 'testingMethod and confirmationMethod."}
 {:id :cg/functionalDataSupport,
  :description "Whether functional data support the impact of the variant on
      the gene product."
  :type :rdf/Property
  :maturity :cg/Volatile
  :range :String
  :internal-note "Part of VariantFunctionalImpactEvidence, which needs to be reconcieved as a Study Finding. This is just a 'Yes' or 'No' string, not cast as a boolean value"}
 {:id :cg/hasEvidenceItems,
  :type :rdf/Property
  :range :cg/EvidenceItem
  :reference :sepio/hasEvidenceItems
  :description " An individual piece of information that was evaluated as evidence in building the argument represented by an Evidence Line."
  :internal-note "EvidenceItem will be diverse. May in in most cases be a study finding. Look at class/subclass relationships to substantiate this."}
 {:id :cg/hasEvidenceLines,
  :type :rdf/Property
  :range :cg/EvidenceLine
  :reference :sepio/hasEvidenceLines
  :description "An evidence-based argument that supports or disputes the validity of the proposition that a Statement assesses or puts forth as true. The strength and direction of this argument (whether it supports or disputes  the proposition, and how strongly) is based on an interpretation of one or more pieces of information as evidence (i.e. 'Evidence Items)."}
 {:id :cg/hasVariant,
  :description "A variant identified in the proband. Where biallelic
      variants cause disease, both variants must be recorded and each is
      evaluated independently before the scores are summed."
  :type :rdf/Property
  :range :cg/VariationDescriptor
  :maturity :cg/Draft
  :internal-note "The range of this should probably go beyond just a variationdescriptor at some point, but it's probably acceptable for now"}
 {:id :cg/interpretation,
  :type :rdf/Property
  :range :skos/Concept
  :value-set :cg/InterpretationValueSet
  :reference :sepio/interpretation
  :description "The outcome of an interpretation of lower-level data item(s) in a StudyResult, that express some broader conclusion or insight that was made (e.g. that data indicates a result to be is high, normal, or low)."}
 {:id :cg/lowerConfidenceLimit,
  :description "The lower bound of the confidence interval reported for the
      association between variant and disease. An interval that crosses 1.0
      indicates the comparison was not statistically significant."
  :type :rdf/Property
  :range :Number
  :maturity :cg/Draft}
 {:id :cg/meetsInclusionCriteria,
  :description "Whether this segregation result is included in the aggregate
      segregation score. A family may be recorded but excluded from the
      calculation, for example where including a weaker candidate-gene LOD
      score would reduce the points otherwise warranted."
  :type :rdf/Property
  :range :Boolean
  :maturity :cg/Volatile
  :internal-note "Indication whether or not a Segregation Result is used to calculate an aggregate result. Probably this should spark a discussion about whether."}
 {:id :cg/member,
  :description "An individual belonging to the family under study."
  :type :rdf/Property
  :range :cg/ProbandStudyResult
  :maturity :cg/Draft
  :internal-note "Families have probands that are members of them."}
 {:id :cg/supportingMethodTypes,
  :type :rdf/Property
  :reference :sepio/supportingMethodTypes
  :description "Types of methodological approaches that were executed to directly or indirectly support creation of the Information Entity."
  :maturity :cg/Draft
  :range :skos/Concept
  :value-set :cg/MethodValueSet
  :internal-note "Method of analysis for a CaseControlStudyResult. May be able to use more general term, but should commit either way. SEPIO uses supportingMethods and supportingMethodTypes--this seems like an appropriate use" }
 {:id :cg/modeOfInheritance,
  :description "The inheritance pattern observed in a family. Semidominant
      applies where monoallelic and biallelic variation share a molecular
      mechanism and produce the same disease entity, with the heterozygous
      state typically presenting an intermediate phenotype relative to the
      homozygous state."
  :type :rdf/Property
  :maturity :cg/Volatile
  :range :String
  :note "This currently a string description of mode of inheritance attached to a family result. This may be more applicable to a SegregationStudyResult. "
  :internal-flag #{:remodel}
  :internal-options ["Autosomal dominant/X-linked" "Semidominant" "Autosomal recessive"]}
 {:id :cg/modeOfInheritanceQualifier,
  :description "The mode of inheritance (MOI) under which the gene-disease
      relationship is curated. Common choices are autosomal dominant,
      autosomal recessive, X-linked, and semidominant; a relationship with
      an undetermined MOI cannot be scored and should generally not be
      classified above Limited."
  :type :rdf/Property
  :range :rdfs/Class
  :maturity :cg/Draft}
 {:id :cg/modelSystem,
  :description "The system in which a functional alteration experiment was
      conducted, distinguishing patient cells from non-patient cells."
  :type :rdf/Property
  :range :skos/Concept
  :value-set :cg/ModelSystemValueSet
  :maturity :cg/Draft}
 {:id :cg/numberWithVariant,
  :description "The number of individuals in the cohort found to carry the
      variant or variants under analysis."
  :range :Integer
  :type :rdf/Property
  :maturity :cg/Draft}
 {:id :cg/objectCondition,
  :description "The disease entity being curated, determined by applying the
      ClinGen Lumping and Splitting guidelines. Every curation record
      requires a Mondo Disease Ontology identifier for this entity."
  :range :rdfs/Class
  :type :rdf/Property
  :maturity :cg/Draft}
 {:id :cg/variantObservedIn,
  :type :rdf/Property
  :range :cg/ProbandStudyResult
  :maturity :cg/Draft
  :note "Inverse of hasVariant, attribute of VariantObservationStudyResult"}
 {:id :cg/pValue,
  :description "The p-value reported for the comparison between cases and
      controls. Factors such as multiple testing that affect the
      interpretation of uncorrected p-values should be considered when
      assigning points."
  :type :rdf/Property
  :range :Number
  :maturity :cg/Draft}
 {:id :cg/paternityMaternityConfirmed,
  :description "Whether both maternity and paternity of the proband have
      been confirmed, for example by short tandem repeat analysis or
      trio-based exome sequencing. Unconfirmed parental relationships are
      grounds for downgrading a de novo variant score."
  :type :rdf/Property
  :range :String
  :note "one of either 'none' 'Yes' or 'No'. Use internally to contribute to scoring the weight of inheritance evidence, particularly whether the variant is de-novo."
  :internal-note "This appears to be broken, need to confirm what's going on in other data set."
  :maturity :cg/Internal}
 {:id :cg/phaseStatusConfidence,
  :description "The level of confidence that two variants observed in an
      individual lie in trans. If the phase is entirely unknown the evidence
      should be marked nonscorable rather than assigned a phase status."
  :type :rdf/Property
  :range :skos/Concept
  :value-set :cg/PhaseStatusConfidenceValueSet
  :maturity :cg/Internal}
 {:id :cg/phenotypeFreeText,
  :description "Clinical features recorded as free text rather than coded
      terms, used to augment the information captured by HPO codes or where
      no appropriate HPO term exists."
  :type :rdf/Property
  :range :String
  :maturity :cg/Volatile
  :note "Free text used to describe phenotypes in place of coded terms. Could restructure as mappable concept."}
 {:id :cg/phenotypeNegativeAlleleNegative,
  :description "The number of individuals in the family who are neither
      affected nor carry the allele of interest. For recessive disorders
      these are counted as the unaffected individuals at the same risk as
      the proband of inheriting both altered alleles."
  :type :rdf/Property
  :range :Integer
  :maturity :cg/Draft
  :note "Number of individuals in study without either the phenotype or an allele of interest."}
 {:id :cg/phenotypePositiveAllelePositive,
  :description "The number of individuals in the family who are both
      affected and carry the allele of interest. For dominant and X-linked
      disorders only these individuals and obligate carriers are counted
      toward segregations."
  :type :rdf/Property
  :range :Integer
  :maturity :cg/Draft
  :note "Number of individuals in study with both the phenotype and an allele of interest."}
 {:id :cg/phenotypes,
  :description "The clinical features observed in an individual, collected
      as Human Phenotype Ontology (HPO) codes. HPO terms are strongly
      preferred, and sufficient detail should be captured to support the
      diagnosis."
  :type :rdf/Property
  :range :rdfs/Class
  :maturity :cg/Draft
  :note "Generally expected to be a set of classes in Human Phenotype Ontology (HPO)"}
 {:id :cg/predicate,
  :type :rdf/Property
  :range :skos/Concept
  :reference :sepio/predicate
  :description "The relationship declared to hold between the subject and the object of the Proposition."
  :note "For Gene Disease Validity, should be :cg/variantsInGeneCausalFor "}
 {:id :cg/previousTesting,
  :description "Whether the proband underwent prior genetic testing capable
      of ruling out alternative causes of disease. For non-specific or
      genetically heterogeneous conditions, limited prior testing is grounds
      for downgrading from default points."
  :type :rdf/Property
  :maturity :cg/Volatile
  :range :Boolean
  :internal-note "boolean value for whether the proband has been previously tested."}
 {:id :cg/previousTestingDescription,
  :description "Free-text description of the genetic testing the proband
      underwent before the variant of interest was identified."
  :type :rdf/Property
  :range :String
  :maturity :cg/Volatile}
 {:id :cg/proband,
  :description "The individual presenting to healthcare within a family,
      whose genotype phase is treated as unknown when counting segregations."
  :type :rdf/Property
  :maturity :cg/Draft
  :range :cg/ProbandStudyResult
  :internal-note "used by segregation, meant to indicate the individual presenting to healthcare within the family. Used when applying a variant score for that individual separate from the segregation score."}
 {:id :cg/proposition,
  :type :rdf/Property
  :range :cg/Proposition
  :reference :sepio/proposition
  :description "A possible fact that the Statement assesses or puts forth as true."
  :note "Should be GeneDiseaseValidityProposition."}
 {:id :cg/publishedLODScore,
  :description "A LOD score calculated by the authors of a publication.
      Where the authors have provided one, the curator should document it
      rather than estimating a new score; where the curator disagrees with
      it, no points should be assigned and the concern raised with expert
      reviewers."
  :type :rdf/Property
  :maturity :cg/Internal
  :range :Number
  :note "Reflects distinction between LOD score included in published literature and LOD score estimated using ClinGen Gene Curation SOP."}
 {:id :cg/relatedCondition,
  :description "The condition for which the cohort was ascertained."
  :type :rdf/Property
  :range :rdfs/Class
  :maturity :cg/Volatile
  :internal-note "Used in Cohort, not entirely sure how to leverage this. Currently poorly named--listed as commonDiagnosis in Case Control."
  :attention-before-final true}
 {:id :cg/score,
  :type :rdf/Property
  :range :Number
  :reference :sepio/score
  :description "A quantitative score that indicates the strength of a Proposition's validity assessment in the direction indicated (i.e. how strongly supported or disputed the Proposition is believed to be).  Depending on its implementation, a score may reflect how *confident* that agent is that the Proposition is true or false, or the *strength of evidence* they believe supports or disputes it."}
 {:id :cg/scoreOfEvidenceProvided,
  :type :rdf/Property
  :range :Number
  :reference :sepio/scoreOfEvidenceProvided
  :description "A quantitative score indicating the strength of support that an Evidence Line is determined to provide for or against its target Proposition, evaluated relative to the direction indicated by the directionOfEvidenceProvided value."}
 {:id :cg/genotypeConfirmationMethod,
  :description "The method used to confirm the genotype reported for an
      individual."
  :type :rdf/Property
  :maturity :cg/Draft
  :range :skos/Concept
  :value-set :cg/GeneticTestingMethodValueSet}
 {:id :cg/sequence,
  :type :rdf/Property
  :range :Integer
  :maturity :cg/Internal
  :description "Ordering of events on ClinGen Data Exchange"}
 {:id :cg/sex,
  :description "The reported sex of an individual. Sex bears on the
      interpretation of X-linked pedigrees, where affected probands are
      often hemizygous males or manifesting heterozygous females."
  :type :rdf/Property
  :maturity :cg/Draft
  :range :skos/Concept
  :value-set :cg/SexValueSet}
 {:id :cg/specifiedBy,
  :type :rdf/Property
  :range :skos/Concept
  :value-set :cg/SpecifiedByValueSet
  :reference :sepio/specifiedBy
  :description "A specification that describes all or part of the process that led to creation of the Information Entity."}
 {:id :cg/statisticalSignificanceType,
  :description "The statistical test or measure reported for the
      case-control comparison, such as an odds ratio or a Fisher exact test."
  :type :rdf/Property
  :maturity :cg/Volatile
  :range :String
  :note "Expect some alignment of statistical tests at the GA4GH level. This part of the model does not reflect such an alignment. This is a free-text field, unlikely to be re-used at the aligned level."}
 {:id :cg/statisticalSignificanceValue,
  :description "The value of the reported statistical measure. Where an odds
      ratio is presented, its magnitude should be consistent with a
      monogenic disease etiology."
  :type :rdf/Property
  :maturity :cg/Volatile
  :range :Number
  :note "Expect some alignment of statistical tests at the GA4GH level. This part of the model does not reflect such an alignment."}
 {:id :cg/statisticalSignificanceValueType,
  :description "The type of the reported statistical value, distinguishing
      for example a point estimate from an interval bound."
  :type :rdf/Property
  :maturity :cg/Volatile
  :range :String
  :note "Expect some alignment of statistical tests at the GA4GH level. This part of the model does not reflect such an alignment. This is a free-text field, unlikely to be re-used at the aligned level."}
 {:id :cg/subjectGene,
  :description "The gene whose relationship to a disease is being evaluated.
      Only the HGNC approved gene symbol may be used to create a curation
      record."
  :type :rdf/Property
  :range :skos/Concept
  :json-type :String
  :note "IRI of Gene."
  :internal-note "This is a case where the representation will differ between RDF and JSON, since JSON doesn't have a Resource type."
  :maturity :cg/Draft}
 {:id :cg/upperConfidenceLimit,
  :description "The upper bound of the confidence interval reported for the
      association between variant and disease."
  :type :rdf/Property
  :range :Number
  :maturity :cg/Volatile}
 {:id :cg/version
  :description "The version of a published classification. Version numbers
      are determined by the reason selected for publishing or republishing
      and are surfaced on the ClinGen website."
  :type :rdf/Property
  :range :String
  :maturity :cg/Draft}
 {:id :cg/zygosity,
  :description "The zygosity of the observed variant. For homozygous
      variants the variant score is doubled, as the variant is present on
      both alleles."
  :type :rdf/Property
  :range :skos/Concept
  :value-set :cg/ZygosityValueSet
  :maturity :cg/Draft}
 {:id :dc/description,
  :type :rdf/Property
  :maturity :cg/Draft
  :range :String
  :internal-note "Used everywhere in our model, but am deriving this from Dublin Core rather than SEPIO. Will probably leave for now."
  :tag #{:cg/Meta}}
 {:id :dc/isVersionOf,
  :type :rdf/Property
  :maturity :cg/Draft
  :range :cg/Resource
  :json-type :String
  :internal-note "This is needed as part of a provennance scheme, used to refer to a parent record that may have several (immutable) versions. Also haven't defined the type of Ur-records yet "
  :tag #{:cg/Meta}}
 {:id :dc/source,
  :type :rdf/Property
  :maturity :cg/Draft
  :range :cg/Resource
  :json-type :String
  :tag #{:cg/Meta}}
 {:id :rdf/type,
  :type :rdf/Property
  :maturity :cg/Draft
  :range :rdfs/Class
  :tag #{:cg/Meta}}
 {:id :rdfs/label,
  :type :rdf/Property
  :maturity :cg/Draft
  :range :String
  :tag #{:cg/Meta}}
 {:id :skos/prefLabel,
  :type :rdf/Property
  :maturity :cg/Draft
  :range :String
  :tag #{:cg/Meta}}
 ;; Value Sets
 {:id :cg/ActivityTypeValueSet,
  :description "The types of activity a contributor may have performed on a
      curation record."
  :type :skos/Collection
  :skos/member [:cg/Evaluated :cg/Approved :cg/Submitted]}
 {:id :cg/AgeTypeValueSet,
  :description "The events to which a recorded age may refer."
  :type :skos/Collection
  :skos/member
  [:cg/AgeAtReport :cg/AgeAtOnset :cg/AgeAtDeath :cg/AgeAtDiagnosis]}
 {:id :cg/AgeUnitValueSet,
  :description "The units in which a recorded age may be expressed."
  :type :skos/Collection
  :skos/member
  [:cg/Months
   :cg/WeeksGestation
   :cg/Days
   :cg/Hours
   :cg/Weeks
   :cg/Years]}
 {:id :cg/VariantOriginValueSet,
  :description "The possible origins of an observed allele."
  :type :skos/Collection
  :skos/member
  [:cg/DeNovoAlleleOrigin :cg/AlleleOrigin :cg/GermlineAlleleOrigin]}
 {:id :cg/CalculatedClassificationValueSet,
  :description "The classifications that can be reached automatically from
      the summed evidence points, excluding the contradictory-evidence
      classifications that only an expert panel may assign."
  :type :skos/Collection
  :skos/member
  [:cg/Limited
   :cg/Strong
   :cg/Definitive
   :cg/NoKnownDiseaseRelationship
   :cg/Moderate]}
 {:id :cg/ClassificationValueSet,
  :description "The gene-disease validity classifications that may be
      assigned by an expert panel, spanning supportive evidence, absence of
      evidence, and contradictory evidence."
  :type :skos/Collection
  :skos/member
  [:cg/Limited
   :cg/Strong
   :cg/Refuted
   :cg/Definitive
   :cg/NoKnownDiseaseRelationship
   :cg/Disputed
   :cg/Moderate]}
 {:id :cg/CurationReasonsValueSet,
  :description "The reasons that may be given for publishing or republishing
      a classification, which determine the version number assigned to the
      curation."
  :type :skos/Collection
  :skos/member
  [:cg/RecurationFrameworkChange
   :cg/RecurationCommunityRequest
   :cg/DiseaseNameUpdate
   :cg/RecurationDiscrepancyResolution
   :cg/RecurationErrorAffectingScoreorClassification
   :cg/RecurationTiming
   :cg/RecurationNewEvidence
   :cg/ErrorClarification
   :cg/NewCuration]}
 {:id :cg/DirectionOfEvidenceProvidedValueSet,
  :description "The directions an individual line of evidence may take with
      respect to a proposition."
  :type :skos/Collection
  :skos/member [:cg/Disputes :cg/Neutral :cg/Supports]}
 {:id :cg/EthnicityValueSet,
  :description "The ethnicity categories that may be recorded for an
      individual or cohort, captured separately from race."
  :type :skos/Collection
  :skos/member
  [:cg/NotHispanicOrLatino :cg/Unknown :cg/HispanicOrLatino]}
 {:id :cg/SexValueSet,
  :description "The sex values that may be recorded for an individual."
  :type :skos/Collection
  :skos/member
  [:cg/AmbiguousSex
   :cg/Unknown
   :cg/Other
   :cg/Male
   :cg/Female
   :cg/TransMale
   :cg/Intersex]}
 {:id :cg/SpecifiedByValueSet,
  :description "The criteria from the Gene-Disease Validity SOP under which
      a piece of evidence may be assessed and scored."
  :type :skos/Collection
  :skos/member
  [:cg/GeneValidityProbandADDeNovoCriteria
   :cg/GeneValidityCriteria11
   :cg/GeneValidityOverallExperimentalEvidenceCriteria
   :cg/GeneValidityNonNullVariantCriteria
   :cg/GeneValidityPatientCellRescueCriteria
   :cg/GeneValidityOverallFunctionalEvidenceCriteria
   :cg/GeneValidityBiochemicalFunctionCriteria
   :cg/GeneValidityUncategorizedProbandCriteria
   :cg/GeneValidityCriteria8
   :cg/GeneValidityProteinInteractionCriteria
   :cg/GeneValidityCriteria6
   :cg/GeneValidityNonHumanModelOrganismCriteria
   :cg/GeneValidityOverallAutosomalDominantDeNovoVariantEvidenceCriteria
   :cg/GeneValidityOverallGeneticEvidenceCriteria
   :cg/GeneValidityMaximumProbandScoreCriteria
   :cg/GeneValiditySegregationEvidenceCriteria
   :cg/GeneValidityCriteria10
   :cg/GeneValidityCellCultureRescueCriteria
   :cg/GeneValidityOverallFunctionalAlterationEvidenceCriteria
   :cg/GeneValidityNonHumanRescueCriteria
   :cg/GeneValidityCriteria5
   :cg/GeneValidityCriteria9
   :cg/GeneValidityNullVariantCriteria
   :cg/GeneValidityHumanRescueCriteria
   :cg/GeneValidityOverallModelAndRescueEvidenceCriteria
   :cg/GeneValidityOverallAutosomalDominantOtherVariantEvidenceCriteria
   :cg/GeneValidityProbandARDeNovoNullVariantCriteria
   :cg/GeneValidityCriteria12
   :cg/GeneValidityPatientCellFunctionalAlterationCriteria
   :cg/GeneValidityGeneExpressionCriteria
   :cg/GeneValidityOverallCaseControlEvidenceCriteria
   :cg/GeneValidityCellCultureModelOrganismCriteria
   :cg/GeneValidityCriteria4
   :cg/GeneValidityNonPatientCellFunctionalAlterationCriteria
   :cg/GeneValidityProbandADNonNullCriteria
   :cg/GeneValidityProbandADNullCriteria
   :cg/GeneValidityCriteria7
   :cg/GeneValidityOverallAutosomalDominantNullVariantEvidenceCriteria
   :cg/GeneValidityCaseControlAggregateVariantAnalysisCriteria
   :cg/GeneValidityProbandARNonNullCriteria
   :cg/GeneValidityCaseControlSingleVariantAnalysisCriteria]}
 {:id :cg/ZygosityValueSet,
  :description "The zygosity states a variant observation may take."
  :type :skos/Collection
  :skos/member
  [:cg/Homozygous
   :cg/BiallelicCompoundHeterozygous
   :cg/Heterozygous
   :cg/Hemizygous
   :cg/MonoallelicHeterozygous
   :cg/TwoVariantsInTrans
   :cg/BiallelicHomozygous]}
 {:id :cg/GeneticTestingMethodValueSet,
  :description "The laboratory methods by which a variant may have been
      identified or confirmed."
  :type :skos/Collection,
  :skos/member [:cg/GenePanels
                :cg/SangerSequencing
                :cg/ChromosomalMicroarray
                :cg/RestrictionDigest
                :cg/Genotyping
                :cg/SSCP
                :cg/ExomeSequencing
                :cg/LinkageAnalysis
                :cg/Other
                :cg/PCR
                :cg/WholeGenomeSequencing
                :cg/DenaturingGradientGel
                :cg/HomozygosityMapping
                :cg/HighResolutionMelting]}
 {:id :cg/InterpretationValueSet,
  :description "The experimental evidence types that may be assigned to a
      gene function study result, spanning gene function, functional
      alteration, model systems, and rescue."
  :type :skos/Collection,
  :skos/member [:obo/MI_0933
                :obo/MI_0935
                :cg/GeneFunctionConsistentWithPhenotype
                :cg/GeneFunctionSimilarToOtherKnownDiseaseCausingGene
                :obo/MI_0208
                :cg/GeneAlteredInAffectedPatients
                :obo/MI_0915
                :cg/GeneSpecificTreatmentRescuesPhenotype
                :cg/ProteinAlterationDisruptsModelOrganism
                :cg/GeneAlterationProducesDiseaseConsistentPhenotype
                :cg/GeneExpressedInDiseaseRelevantTissues]}
 {:id :cg/MethodValueSet
  :description "The analysis methods underlying scored evidence: the study
      design of a case-control analysis, and the sequencing approach used to
      investigate a linkage interval."
  :type :skos/Collection
  :skos/member [:cg/SingleVariantAnalysis
                :cg/AggregateVariantAnalysis
                :cg/CandidateGeneSequencing
                :cg/AllGenesSequencing]}
 {:id :cg/ModelSystemValueSet,
  :description "The systems in which a functional alteration experiment may
      be conducted."
  :type :skos/Collection
  :skos/member [:cg/NonPatientCells
                :cg/PatientCells]}
 {:id :cg/PhaseStatusConfidenceValueSet
  :description "The levels of confidence that two variants observed in an
      individual lie in trans."
  :type :skos/Collection
  :skos/member [:cg/ProvenInTrans
                :cg/SuspectedInTrans]}
 {:id :cg/DirectionValueSet,
  :description "The directions a body of evidence may take with respect to a
      proposition."
  :type :skos/Collection
  :skos/member [:cg/Neutral :cg/Supports]}
 {:id :cg/Evaluated, :type :skos/Concept}
 {:id :cg/Approved,
  :description "The classification was approved by the expert panel."
  :type :skos/Concept}
 {:id :cg/Submitted, :type :skos/Concept}
 {:id :cg/AgeAtReport,
  :description "The recorded age refers to the age of the individual at the
      time of the report."
  :type :skos/Concept}
 {:id :cg/AgeAtOnset,
  :description "The recorded age refers to the age at onset of the
      phenotype."
  :type :skos/Concept}
 {:id :cg/AgeAtDeath,
  :description "The recorded age refers to the age at death."
  :type :skos/Concept}
 {:id :cg/AgeAtDiagnosis,
  :description "The recorded age refers to the age at which the diagnosis
      was made."
  :type :skos/Concept}
 {:id :cg/Months, :type :skos/Concept}
 {:id :cg/WeeksGestation,
  :description "Age expressed in weeks of gestation, for ages recorded
      before birth."
  :type :skos/Concept}
 {:id :cg/Days, :type :skos/Concept}
 {:id :cg/Hours, :type :skos/Concept}
 {:id :cg/Weeks, :type :skos/Concept}
 {:id :cg/Years, :type :skos/Concept}
 {:id :cg/DeNovoAlleleOrigin,
  :description "The allele arose de novo: it is present in the affected
      individual but absent from appropriately tested parents, or a parent
      carries it mosaically. Postzygotic mosaicism is also treated as a de
      novo occurrence."
  :type :skos/Concept}
 {:id :cg/AlleleOrigin, :type :skos/Concept}
 {:id :cg/GermlineAlleleOrigin, :type :skos/Concept}
 {:id :cg/Limited,
  :description "The gene-disease relationship is considered plausible by
      experts but the evidence is insufficient to score as Moderate, for
      example a small number of cases with well-defined phenotypes and
      plausible variants. Requires 0.1-6 points. Not to be applied where
      none of the presented evidence is compelling, in which case Disputed
      should be considered."
  :type :skos/Concept}
 {:id :cg/Strong,
  :description "The role of the gene in disease has been independently
      demonstrated in at least two separate studies, with considerable
      genetic evidence from numerous unrelated probands and no convincing
      contradictory evidence. Requires at least 12 points but without
      replication over time."
  :type :skos/Concept}
 {:id :cg/Definitive,
  :description "The role of the gene in the disease has been repeatedly
      demonstrated in both research and clinical diagnostic settings and
      upheld over time, generally at least two independent scored
      publications of human genetic evidence over at least three years.
      Requires 12-18 points together with replication over time, and no
      convincing contradictory evidence."
  :type :skos/Concept}
 {:id :cg/NoKnownDiseaseRelationship,
  :description "No evidence for a causal role of the gene in the disease of
      interest has been reported in the literature or public databases. The
      gene may be a candidate based on linkage intervals, animal models, or
      pathway membership, but no report has directly implicated it in the
      specified disease. This category was known as No Reported Evidence
      prior to August 2019."
  :type :skos/Concept}
 {:id :cg/Moderate,
  :description "There is moderate evidence for a causal role of the gene in
      the disease, typically some convincing genetic evidence with or
      without supporting experimental data. The relationship may not have
      been independently reported, but no convincing contradictory evidence
      has emerged. Requires 7-11 points."
  :type :skos/Concept}
 {:id :cg/Limited,
  :description "The gene-disease relationship is considered plausible by
      experts but the evidence is insufficient to score as Moderate, for
      example a small number of cases with well-defined phenotypes and
      plausible variants. Requires 0.1-6 points. Not to be applied where
      none of the presented evidence is compelling, in which case Disputed
      should be considered."
  :type :skos/Concept}
 {:id :cg/Strong,
  :description "The role of the gene in disease has been independently
      demonstrated in at least two separate studies, with considerable
      genetic evidence from numerous unrelated probands and no convincing
      contradictory evidence. Requires at least 12 points but without
      replication over time."
  :type :skos/Concept}
 {:id :cg/Refuted,
  :description "Evidence refuting the reported role of the gene in the
      specified disease has been reported and significantly outweighs any
      supporting evidence, for example where the original probands were
      later found to have an alternative cause of disease or not to have the
      disease in question."
  :type :skos/Concept}
 {:id :cg/Definitive,
  :description "The role of the gene in the disease has been repeatedly
      demonstrated in both research and clinical diagnostic settings and
      upheld over time, generally at least two independent scored
      publications of human genetic evidence over at least three years.
      Requires 12-18 points together with replication over time, and no
      convincing contradictory evidence."
  :type :skos/Concept}
 {:id :cg/NoKnownDiseaseRelationship,
  :description "No evidence for a causal role of the gene in the disease of
      interest has been reported in the literature or public databases. The
      gene may be a candidate based on linkage intervals, animal models, or
      pathway membership, but no report has directly implicated it in the
      specified disease. This category was known as No Reported Evidence
      prior to August 2019."
  :type :skos/Concept}
 {:id :cg/Disputed,
  :description "An assertion of a gene-disease relationship exists, but the
      initial evidence is not compelling from today's perspective or
      conflicting evidence has arisen, for example where all reported cases
      score zero on expert review or the reported variants prove too common
      in the population."
  :type :skos/Concept}
 {:id :cg/Moderate,
  :description "There is moderate evidence for a causal role of the gene in
      the disease, typically some convincing genetic evidence with or
      without supporting experimental data. The relationship may not have
      been independently reported, but no convincing contradictory evidence
      has emerged. Requires 7-11 points."
  :type :skos/Concept}
 {:id :cg/RecurationFrameworkChange,
  :description "The curation was reevaluated to be consistent with a newer
      version of the curation framework (e.g. SOP, specification, etc.)."
  :type :skos/Concept}
 {:id :cg/RecurationCommunityRequest,
  :description "The curation was reevaluated at the request of a user,
      either internal or external to ClinGen."
  :type :skos/Concept}
 {:id :cg/DiseaseNameUpdate,
  :description "An administrative update in which the disease term was
      updated without changing the overall scope of the original curation,
      for example a Mondo update."
  :type :skos/Concept}
 {:id :cg/RecurationDiscrepancyResolution,
  :description "A conflict with another group (e.g. ClinVar submitters for
      VCEPs, GenCC submitters for GCEPs) prompted reevaluation."
  :type :skos/Concept}
 {:id :cg/RecurationErrorAffectingScoreorClassification,
  :description "An error resulted in incorrect scoring and/or
      classification, prompting reevaluation."
  :type :skos/Concept}
 {:id :cg/RecurationTiming,
  :description "The classification is being re-evaluated because the
      recommended interval for recuration has elapsed."
  :type :skos/Concept}
 {:id :cg/RecurationNewEvidence,
  :description "New evidence was evaluated since this classification was
      last published."
  :type :skos/Concept}
 {:id :cg/ErrorClarification,
  :description "An administrative update covering changes that do not alter
      the overall classification and do not require rereview by the group,
      such as typo corrections, the addition of clarifying language to free
      text fields, and adding contributors."
  :type :skos/Concept}
 {:id :cg/NewCuration,
  :description "The gene-disease relationship is being published for the
      first time."
  :type :skos/Concept}
 {:id :cg/Neutral, :type :skos/Concept}
 {:id :cg/Supports,
  :description "The evidence supports the proposition."
  :type :skos/Concept}
 {:id :cg/Disputes,
  :description "The evidence contradicts the proposition and may bring the
      gene-disease relationship into question. Evidence in this direction
      cannot be assigned a score but is flagged as contradictory evidence
      for expert review."
  :type :skos/Concept}
 {:id :cg/Neutral, :type :skos/Concept}
 {:id :cg/Supports,
  :description "The evidence supports the proposition."
  :type :skos/Concept}
 {:id :cg/NotHispanicOrLatino,
  :description "The individual or cohort is reported as not Hispanic or
      Latino. Ethnicity is recorded separately from race in the curation
      interface."
  :type :skos/Concept}
 {:id :cg/Unknown,
  :description "No value was reported in the source publication."
  :type :skos/Concept}
 {:id :cg/HispanicOrLatino,
  :description "The individual or cohort is reported as Hispanic or Latino.
      Ethnicity is recorded separately from race in the curation interface."
  :type :skos/Concept}
 {:id :cg/AmbiguousSex, :type :skos/Concept}
 {:id :cg/Unknown,
  :description "No value was reported in the source publication."
  :type :skos/Concept}
 {:id :cg/Other,
  :description "A value not covered by the other options in the set."
  :type :skos/Concept}
 {:id :cg/Male, :type :skos/Concept}
 {:id :cg/Female, :type :skos/Concept}
 {:id :cg/TransMale, :type :skos/Concept}
 {:id :cg/Intersex, :type :skos/Concept}
 {:id :cg/GeneValidityProbandADDeNovoCriteria,
  :description "Criteria for scoring a proband with a de novo variant under
      a monoallelic mode of inheritance. De novo status requires that
      appropriately tested parents do not carry the variant."
  :type :skos/Concept}
 {:id :cg/GeneValidityCriteria11
  :type :skos/Concept
  :description "Version 11 of the ClinGen Gene Disease Validity SOP"}
 {:id :cg/GeneValidityOverallExperimentalEvidenceCriteria,
  :description "The total experimental evidence score for the gene-disease
      relationship, capped at 6 points regardless of the individual category
      tallies."
  :type :skos/Concept}
 {:id :cg/GeneValidityNonNullVariantCriteria,
  :description "Criteria for scoring a variant with gene impact other than a
      predicted or proven null, including missense variants, small in-frame
      insertions and deletions, and variants producing gain of function or
      dominant-negative effects. Upgrading requires experimentally
      demonstrated functional impact."
  :type :skos/Concept}
 {:id :cg/GeneValidityPatientCellRescueCriteria,
  :description "Criteria for scoring in the Functional Alteration – Patient Cells category of experimental data.  The evidence should demonstrate that the phenotype can be rescued in patient cells. Default 1 point, range 0-2."
  :type :skos/Concept}
 {:id :cg/GeneValidityOverallFunctionalEvidenceCriteria,
  :description "The combined score for the Experimental Evidence category Function, comprising biochemical function, protein interaction, and expression evidence, capped at 2 points."
  :type :skos/Concept}
 {:id :cg/GeneValidityBiochemicalFunctionCriteria,
  :description "Criteria for scoring in the Function – Biochemical Function category of experimental evidence. The evidence should demonstrate that the gene product performs a biochemical function shared with other genes known to cause the disease, or consistent with the phenotype. Default 0.5 points, range 0-2."
  :type :skos/Concept}
 {:id :cg/GeneValidityUncategorizedProbandCriteria
  :type :skos/Concept
  :description "Too little is known about the provennance of the variant in the guidelines to properly classify the proband. This evidence should be assigned a score of 0."}
 {:id :cg/GeneValidityCriteria8
  :type :skos/Concept
  :description "Version 8 of the ClinGen Gene Disease Validity SOP"}
 {:id :cg/GeneValidityProteinInteractionCriteria,
  :description "Criteria for scoring in the Function – Protein Interaction category of experimental evidence.  The evidence should demonstrate that the gene product interacts with proteins previously implicated in the disease of interest, demonstrated experimentally rather than predicted. Default 0.5 points, range 0-2."
  :type :skos/Concept}
 {:id :cg/GeneValidityCriteria6
  :type :skos/Concept
  :description "Version 6 of the ClinGen Gene Disease Validity SOP"}
 {:id :cg/GeneValidityNonHumanModelOrganismCriteria,
  :description "Criteria for scoring in the Models – Non-human model organism category of experimental evidence.  The evidence should demonstrate that a non-human model organism with a disrupted copy of the gene shows a phenotype consistent with the human disease state. Default 2 points, range 0-4."
  :type :skos/Concept}
 {:id
  :cg/GeneValidityOverallAutosomalDominantDeNovoVariantEvidenceCriteria,
  :description "The aggregate score contributed by de novo variants under a
      monoallelic mode of inheritance."
  :type :skos/Concept}
 {:id :cg/GeneValidityOverallGeneticEvidenceCriteria,
  :description "The total genetic evidence score for the gene-disease
      relationship, summing case-level, segregation, and case-control
      evidence, capped at 12 points."
  :type :skos/Concept}
 {:id :cg/GeneValidityMaximumProbandScoreCriteria,
  :description "The cap on genetic evidence contributed by any single
      proband, which may not exceed 3 points."
  :type :skos/Concept}
 {:id :cg/GeneValiditySegregationEvidenceCriteria,
  :description "The score contributed by segregation evidence, assigned from the LOD score summed across all qualifying families and the sequencing method used to investigate the linkage interval. Capped at 3 points."
  :type :skos/Concept}
 {:id :cg/GeneValidityCriteria10,
  :type :skos/Concept
  :description "Version 10 of the ClinGen Gene Disease Validity SOP"}
 {:id :cg/GeneValidityCellCultureRescueCriteria,
  :description "Criteria for scoring in the Rescue – Rescue in cell culture model category of experimental evidence.  The evidence should demonstrate that the phenotype can be rescued in a cell culture model engineered to express the variant of interest. Default 1 point, range 0-2."
  :type :skos/Concept}
 {:id :cg/GeneValidityOverallFunctionalAlterationEvidenceCriteria,
  :description "The combined score for the “Functional alteration” category of experimental evidence, comprising evidence from patient and non-patient cells, capped at 2 points."
  :type :skos/Concept}
 {:id :cg/GeneValidityNonHumanRescueCriteria,
  :description "Criteria for scoring in the Rescue – Rescue in non-human model organism category of experimental evidence. The evidence that the phenotype can be rescued in a non-human model organism. Default 2 points, range 0-4."
  :type :skos/Concept}
 {:id :cg/GeneValidityCriteria5,
  :type :skos/Concept
  :description "Version 5 of the ClinGen Gene Disease Validity SOP"}
 {:id :cg/GeneValidityCriteria9
  :type :skos/Concept
  :description "Version 9 of the ClinGen Gene Disease Validity SOP"}
 {:id :cg/GeneValidityNullVariantCriteria,
  :description "Criteria for scoring a predicted or proven null variant, a
      category including nonsense, frameshift, canonical splice site
      variants, and single or multi-exon deletions. Consider downgrading
      where nonsense-mediated decay is not predicted or an altered gene
      product is still made."
  :type :skos/Concept}
 {:id :cg/GeneValidityHumanRescueCriteria,
  :description "Criteria for scoring in the Rescue – Rescue in Human category of experimental evidence. The evidence should demonstrate that the disease phenotype can be rescued in humans, for example by successful enzyme replacement therapy. Default 2 points, range 0-4."
  :type :skos/Concept}
 {:id :cg/GeneValidityOverallModelAndRescueEvidenceCriteria,
  :description "The combined score for the Models and Rescue category of experimental evidence, comprised of evidence from the model system and/or rescue sub-categories, capped at 4 points."
  :type :skos/Concept}
 {:id
  :cg/GeneValidityOverallAutosomalDominantOtherVariantEvidenceCriteria,
  :description "The aggregate score contributed by variants with gene impact
      other than predicted or proven nulls, under a monoallelic mode of
      inheritance."
  :type :skos/Concept}
 {:id :cg/GeneValidityProbandARDeNovoNullVariantCriteria,
  :description "Criteria for scoring a proband with a de novo null variant
      under a biallelic mode of inheritance."
  :type :skos/Concept}
 {:id :cg/GeneValidityCriteria12
  :type :skos/Concept
  :description "Version 12 of the ClinGen Gene Disease Validity SOP"}
 {:id :cg/GeneValidityPatientCellFunctionalAlterationCriteria,
  :description "Criteria for scoring in the Functional Alteration – Patient Cells category of experimental evidence. The evidence should demonstrate that patient cells (in which gene function is disrupted) show a phenotype consistent with the human disease process. Default 1 point, range 0-2."
  :type :skos/Concept}
 {:id :cg/GeneValidityGeneExpressionCriteria,
  :description "Criteria for scoring in the Function – Expression category of experimental evidence.  The evidence should demonstrate that the gene is expressed in tissues relevant to the disease or altered in expression in affected individuals. Default 0.5 points, range 0-2."
  :type :skos/Concept}
 {:id :cg/GeneValidityOverallCaseControlEvidenceCriteria,
  :description "The total score contributed by case-control studies, each
      assessed at 0-6 points, capped at 12 points for the category."
  :type :skos/Concept}
 {:id :cg/GeneValidityCellCultureModelOrganismCriteria,
  :description "Criteria for scoring evidence in the Models – Cell Culture Model” category of experimental evidence.  The evidence should demonstrate that a cell culture model with a disrupted copy of the gene shows a phenotype consistent with the human disease state. Default 1 point, range 0-2."
  :type :skos/Concept}
 {:id :cg/GeneValidityCriteria4,
  :type :skos/Concept
  :description "Version 4 of the ClinGen Gene Disease Validity SOP"}
 {:id :cg/GeneValidityNonPatientCellFunctionalAlterationCriteria,
  :description "Criteria for scoring in the Functional Alteration – Non-patient cells category of experimental evidence.  The evidence should demonstrate that non-patient cultured cells in which gene function is disrupted show a phenotype consistent with the human disease process. Default 0.5 points, range 0-1."
  :type :skos/Concept}
 {:id :cg/GeneValidityProbandADNonNullCriteria,
  :description "Criteria for scoring a proband with a non-null variant with
      gene impact under a monoallelic mode of inheritance."
  :type :skos/Concept}
 {:id :cg/GeneValidityProbandADNullCriteria,
  :description "Criteria for scoring a proband with a predicted or proven
      null variant under a monoallelic mode of inheritance."
  :type :skos/Concept}
 {:id :cg/GeneValidityCriteria7
  :type :skos/Concept
  :description "Version 7 of the ClinGen Gene Disease Validity SOP"}
 {:id
  :cg/GeneValidityOverallAutosomalDominantNullVariantEvidenceCriteria,
  :description "The aggregate score contributed by predicted or proven null
      variants under a monoallelic mode of inheritance."
  :type :skos/Concept}
 {:id :cg/GeneValidityCaseControlAggregateVariantAnalysisCriteria,
  :description "Criteria for scoring a case-control study in which two or
      more variants are evaluated in aggregate for statistical enrichment in
      cases compared to controls. Assigned 0-6 points on study quality."
  :type :skos/Concept}
 {:id :cg/GeneValidityProbandARNonNullCriteria,
  :description "Criteria for scoring a proband with non-null variants under
      a biallelic mode of inheritance, where each variant is evaluated
      independently and the scores summed."
  :type :skos/Concept}
 {:id :cg/GeneValidityCaseControlSingleVariantAnalysisCriteria,
  :description "Criteria for scoring a case-control study in which
      individual variants are independently evaluated for statistical
      enrichment in cases compared to controls. Assigned 0-6 points on study
      quality."
  :type :skos/Concept}
 {:id :cg/Homozygous, :type :skos/Concept}
 {:id :cg/BiallelicCompoundHeterozygous, :type :skos/Concept}
 {:id :cg/Heterozygous, :type :skos/Concept}
 {:id :cg/Hemizygous, :type :skos/Concept}
 {:id :cg/MonoallelicHeterozygous, :type :skos/Concept}
 {:id :cg/TwoVariantsInTrans, :type :skos/Concept}
 {:id :cg/BiallelicHomozygous, :type :skos/Concept}
 {:id :cg/GenePanels, :type :skos/Concept}
 {:id :cg/SangerSequencing, :type :skos/Concept}
 {:id :cg/ChromosomalMicroarray, :type :skos/Concept}
 {:id :cg/RestrictionDigest, :type :skos/Concept}
 {:id :cg/Genotyping, :type :skos/Concept}
 {:id :cg/SSCP, :type :skos/Concept}
 {:id :cg/ExomeSequencing, :type :skos/Concept}
 {:id :cg/LinkageAnalysis, :type :skos/Concept}
 {:id :cg/Other,
  :description "A value not covered by the other options in the set."
  :type :skos/Concept}
 {:id :cg/PCR, :type :skos/Concept}
 {:id :cg/WholeGenomeSequencing, :type :skos/Concept}
 {:id :cg/DenaturingGradientGel, :type :skos/Concept}
 {:id :cg/HomozygosityMapping, :type :skos/Concept}
 {:id :cg/HighResolutionMelting, :type :skos/Concept}
 {:id :obo/MI_0933,
  :description "Diverging genetic interaction, offered when curating protein
      interaction evidence as the negative genetic interaction type: two
      genetic perturbations combined result in a phenotype more severe or
      penetrant than expected given the phenotypes of the individual
      perturbations."
  :type :skos/Concept}
 {:id :obo/MI_0935,
  :description "Converging genetic interaction, offered when curating
      protein interaction evidence as the positive genetic interaction type:
      two genetic perturbations combined result in a phenotype less severe
      or penetrant than expected, making the organism more wild type in
      character with regard to that phenotype."
  :type :skos/Concept}
 {:id :cg/GeneFunctionConsistentWithPhenotype,
  :description "The gene product performs a biochemical function consistent
      with the phenotype of the disease of interest. The function must have
      been proven experimentally rather than predicted."
  :type :skos/Concept}
 {:id :cg/GeneFunctionSimilarToOtherKnownDiseaseCausingGene,
  :description "The gene product performs a biochemical function shared with
      other genes already known to cause the disease of interest. The
      function of both gene products must have been proven experimentally
      rather than predicted, and the other gene should have compelling
      evidence for its own disease relationship."
  :type :skos/Concept}
 {:id :obo/MI_0208,
  :description "Genetic interaction: an effect in which two genetic
      perturbations, when combined, result in a phenotype that is not merely
      explained by the addition of the effects of the individual
      perturbations. One of the four interaction types offered when curating
      protein interaction evidence."
  :type :skos/Concept}
 {:id :cg/GeneAlteredInAffectedPatients,
  :description "Expression of the gene is altered in individuals who have
      the disease, shown for example by examining tissue or cell samples
      from affected individuals whose molecular etiology is unknown."
  :type :skos/Concept}
 {:id :obo/MI_0915,
  :description "Physical association: interaction between molecules within
      the same physical complex, often identified under conditions
      suggesting the molecules are in close proximity but not necessarily in
      direct contact with each other. One of the four interaction types
      offered when curating protein interaction evidence."
  :type :skos/Concept}
 {:id :cg/GeneSpecificTreatmentRescuesPhenotype,
  :description "The disease phenotype can be rescued by a treatment specific
      to the gene: where the mechanism is loss of function, by exogenous
      wild-type gene, gene product, or targeted gene editing; where it is
      gain of function, by a treatment that specifically blocks the action
      of the variant."
  :type :skos/Concept}
 {:id :cg/ProteinAlterationDisruptsModelOrganism,
  :description "Disruption of the gene product in a model organism produces
      a phenotype consistent with the human disease state."
  :type :skos/Concept}
 {:id :cg/GeneAlterationProducesDiseaseConsistentPhenotype,
  :description "A non-human model organism or cell culture model with a
      disrupted copy of the gene shows a phenotype consistent with the human
      disease state. Cell culture models should recapitulate features of the
      diseased tissue."
  :type :skos/Concept}
 {:id :cg/GeneExpressedInDiseaseRelevantTissues,
  :description "The gene is expressed in tissues relevant to the disease of
      interest, shown by methods detecting RNA transcripts or protein
      expression."
  :type :skos/Concept}
 {:id :cg/SingleVariantAnalysis,
  :description "A case-control study design in which individual variants are
      evaluated for statistical enrichment in cases compared to controls.
      More than one variant may be analyzed, provided each is assessed
      independently with appropriate correction for multiple testing."
  :type :skos/Concept}
 {:id :cg/AggregateVariantAnalysis,
  :description "A case-control study design in which the statistical
      enrichment of two or more variants taken together is assessed in cases
      compared to controls, whether by genotyping specific variants or by
      sequencing the entire gene."
  :type :skos/Concept}
 {:id :cg/CandidateGeneSequencing,
  :description "An approach in which only one or a handful of genes within a
      linkage region are sequenced. Because it cannot rule out the effects
      of other variants in the interval, segregation evidence obtained this
      way is awarded fewer points."
  :type :skos/Concept}
 {:id :cg/AllGenesSequencing,
  :description "An approach in which exome or genome sequencing was
      performed, or all genes in the linkage region were sequenced.
      Segregation evidence obtained this way is awarded more points, as
      other candidates in the interval have been assessed."
  :type :skos/Concept}
 {:id :cg/NonPatientCells,
  :description "Cultured cells not derived from an affected individual, in
      which gene function is disrupted experimentally."
  :type :skos/Concept}
 {:id :cg/PatientCells,
  :description "Cells derived from an individual affected by the disease of
      interest."
  :type :skos/Concept}
 {:id :cg/ProvenInTrans,
  :description "Two variants in an individual have been confirmed to lie on
      opposite alleles."
  :type :skos/Concept}
 {:id :cg/SuspectedInTrans,
  :description "Two variants in an individual are inferred to lie on
      opposite alleles, for example where parental testing was completed but
      phase was not established directly."
  :type :skos/Concept}
 ] )
