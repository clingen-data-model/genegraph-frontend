(ns genegraph.frontend.display.features
  (:require [re-frame.core :as re-frame]
            [re-graph.core :as re-graph]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.icon :as icon]
            [clojure.set :as set]))

(defn feature-with-assertion-type [feature typename strength-values]
  (let [strength-set (set strength-values)]
    (some
     (fn [{:keys [__typename assertions]}]
       (and (= typename __typename)
            (some #(strength-set
                    (get-in % [:evidenceStrength :curie]))
                  assertions)))
     (:subjectOf feature))))

#_(defn feature-with-dosage-sufficient [feature]
  (some
   (fn [{:keys [__typename assertions]}]
     (and (= "GeneticConditionMechanismProposition" __typename)
          (some #(= "CG:DosageSufficientEvidence"
                    (get-in % [:evidenceStrength :curie]))
                assertions)))
   (:subjectOf feature)))

(defn feature-with-dosage-sufficient [feature]
  (feature-with-assertion-type
   feature
   "GeneticConditionMechanismProposition"
   ["CG:DosageSufficientEvidence"]))

(defn feature-with-gene-validity-sufficient [feature]
  (feature-with-assertion-type
   feature
   "GeneValidityProposition"
   ["CG:Moderate" "CG:Strong" "CG:Definitive"]))

(defn feature-with-any-annotation [feature]
  ())

(defn group-features-for-cnv [features]
  (let [feature-set (set features)
        dosage-sufficient-features
        (set (filter feature-with-dosage-sufficient features))
        gene-validity-sufficient (filter feature-with-gene-validity-sufficient
                                         (set/difference feature-set dosage-sufficient-features))]
    {:dosage-sufficient dosage-sufficient-features
     :gene-validity gene-validity-sufficient
     :others (set/difference feature-set  dosage-sufficient-features gene-validity-sufficient)}))


