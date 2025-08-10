(ns genegraph.frontend.queries
  "GraphQL queries for resource display pages." 
  (:require [clojure.set :as set]))


(def fragments
  {"SequenceFeature"
   {3 {:fragment "{
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
   "EvidenceStrengthAssertion"
   {3 {:fragment "{
    curie
    iri
    evidenceStrength {
      curie
    }
    versions {
    ...Resource1
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
  }"
       :dependencies #{{:typename "GeneValidityProposition" :detail-level  1}
                       {:typename "Resource" :detail-level  1}
                       {:typename "GeneticConditionMechanismProposition" :detail-level  1}}}}
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

