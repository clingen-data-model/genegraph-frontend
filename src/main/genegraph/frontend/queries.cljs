(ns genegraph.frontend.queries
  "GraphQL queries for resource display pages." 
  (:require [clojure.set :as set]))


(def fragments
  {"CanonicalVariant"
   {2 {:fragment "{
    copyChange {
     curie
    }
    assertions {
      ...Resource1
      ...EvidenceStrengthAssertion1
    }
}"}}
   "SequenceFeature"
   {3 {:fragment "{
  overlappingFeatures {
    ...Resource1
    ...SequenceFeature1
  }
  overlappingVariants(overlap_kind: \"equal\") {
    ...Resource1
    ...CanonicalVariant2
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
      ...GeneValidityProposition1
      ...GeneticConditionMechanismProposition1
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
}"
       :dependencies
       #{{:typename "GeneValidityProposition" :detail-level  1}
         {:typename "GeneticConditionMechanismProposition" :detail-level  1}
         {:typename "SequenceFeature" :detail-level  1}
         {:typename "Resource" :detail-level  1}
         {:typename "CanonicalVariant" :detail-level 2}
         {:typename "EvidenceStrengthAssertion" :detail-level 1}}}
    1 {:fragment "{
  __typename
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
      ...GeneValidityProposition1
      ...GeneticConditionMechanismProposition1
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
}"
       :dependencies #{{:typename "GeneValidityProposition" :detail-level  1}
                       {:typename "GeneticConditionMechanismProposition" :detail-level  1}}}}
   
   "EvidenceLine"
   {2 {:fragment "{
     ...Resource1
      strengthScore
      specifiedBy {
        curie
      }
      evidenceStrength {
        curie
      }
      evidence {
        curie
      }
    }"
       :dependencies
       #{{:typename "Resource" :detail-level 1}}}}
   "EvidenceStrengthAssertion"
   {3 {:fragment "{
    label    
    version
    description
    conflictingAssertions {
      ...EvidenceStrengthAssertion2
    }
    classification {
      curie
      label
    }
    curationReasons {
      curie
    }
    specifiedBy {
      curie
      label
    }
    curationReasonDescription
    evidenceStrength {
      curie
      label
    }
    strengthScore
    evidence {
      ...EvidenceLine2
    }
    versions {
    ...EvidenceStrengthAssertion2
    }
    subject {
      __typename
      type {
        curie
        label
      }
      ...GeneValidityProposition1
      ...GeneticConditionMechanismProposition1
      ...VariantPathogenicityProposition1
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
  }"
       :dependencies #{{:typename "GeneValidityProposition"
                        :detail-level  1}
                       {:typename "VariantPathogenicityProposition"
                        :detail-level  1}
                       {:typename "EvidenceLine"
                        :detail-level 2}
                       {:typename "Resource"
                        :detail-level  1}
                       {:typename "EvidenceStrengthAssertion"
                        :detail-level 2}
                       {:typename "GeneticConditionMechanismProposition"
                        :detail-level  1}}}
    2 {:fragment "{
    curie
    iri
    label
    version
    evidenceStrength {
      curie
      label
    }
    classification {
      curie
      label
    }
    curationReasons {
      curie
      label
    }
    curationReasonDescription
    subject {
      __typename
      type {
        curie
        label
      }
      ...GeneValidityProposition1
      ...GeneticConditionMechanismProposition1
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
  }"
       :dependencies #{{:typename "GeneValidityProposition"
                        :detail-level  1}
                       {:typename "GeneticConditionMechanismProposition"
                        :detail-level  1}}}
    1 {:fragment "{
    classification {
      curie
    }
}"}}
   "GeneValidityProposition"
   {1 {:fragment "{
  modeOfInheritance {
    curie
    label
  }
  disease {
    curie
    label
  }
  gene {
    curie
    label
  }
}"}}
   "GeneticConditionMechanismProposition"
   {1 {:fragment "{
  mechanism {
    curie
  }
  condition {
    label
    curie
  }
}"}}
   "VariantPathogenicityProposition"
   {1 {:fragment "{
        variant {
          ...Resource1
          iri
          label
          copyChange {
            curie
            label
          }
          overlappingFeatures {
            ...Resource1
            assertions {
              __typename
              curie
              label
              type {
                curie
                label
              }
              evidenceStrength {
                curie
                label
              }
            }
          }
        }
}"}}
   "Resource"
   {1 {:fragment "{
  __typename
  iri
  curie
  label
  type {
    curie
    label
  }
}"}}})
"            ...SequenceFeature1"

(defn fragment-str [{:keys [typename detail-level]}]
  (str "\nfragment "
       typename
       detail-level
       " on "
       typename
       " "
       (get-in fragments [typename detail-level :fragment])
       "\n"))

(def base-resource-query
  "query ($iri: String) {
  resource(iri: $iri) {
    ...Resource1
    ...SequenceFeature3
    ...EvidenceStrengthAssertion3
  }
}")

(defn sub-dependencies [new-deps existing-deps]
  (let [deps (set/union new-deps existing-deps)
        sub-deps (set/difference
                  (apply
                   set/union
                   (map #(get-in fragments [(:typename %)
                                            (:detail-level %)
                                            :dependencies])
                        new-deps))
                  deps)]
    (if (seq sub-deps)
      (sub-dependencies sub-deps deps)
      deps)))

(defn query->direct-dependencies [query]
  (->> (re-seq #"([A-Za-z]+)(\d)" query)
       (map (fn [[_ t l]] {:typename t
                           :detail-level (js/parseInt l)}))
       set))

(defn query->dependencies [query]
  (sub-dependencies
   (query->direct-dependencies query)
   #{}))

(defn compile-query [query]
  (str
   query
   (reduce
    str
    (map fragment-str (query->dependencies query)))))

(def compiled-base-query
  (compile-query base-resource-query))

(println compiled-base-query)

