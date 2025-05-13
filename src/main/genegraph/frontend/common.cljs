(ns genegraph.frontend.common
  "Common display elements that do not belong to a specific page, component, or namespace, but can be used by all.")

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
   "GeneValidityProposition" {:label "Gene Validity"
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
