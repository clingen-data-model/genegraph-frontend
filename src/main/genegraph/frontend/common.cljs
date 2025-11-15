(ns genegraph.frontend.common
  "Common display elements that do not belong to a specific page, component, or namespace, but can be used by all."
  (:require [clojure.string :as s]
            [re-frame.core :as rf]
            [reitit.frontend.easy :as rfe]))

(defn inc-detail [old new]
  (if (and old (< (::detail-level new) (::detail-level old)))
    old
    new))

(rf/reg-sub
 :resource
 (fn [db [_ curie]]
   (get db curie)))

(rf/reg-event-db
 ::set-target-object
 (fn [db [_ target]]
   (assoc db ::target-object target)))

(defn resource-href [resource]
  (rfe/href :routes/resource
            {:id (:curie resource)}))

(defn kw->iri-id [k]
  (-> k str (s/replace #":" "") (s/replace #"/" "_")))

(defn iri-id->kw [id]
  (apply keyword (s/split id #"_")))

(rf/reg-event-db
 ::set-secondary-view
 (fn [db [_ data]]
   (js/console.log "setting secondary view")
   (assoc db :secondary-view data)))

(defmulti main-view :__typename)

(defmethod main-view :default [e]
  [:div "no view defined"])

(defmulti secondary-view :__typename)

(defmethod secondary-view :default [e]
  [:div "no view defined"])

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
                                  :tooltip "Autosomal Recessive"}
   "CG:NoAssessment" {:label "No Assessment"
                      :tooltip "No Assessment"}
   "CG:DosageMapConflict" {:label "Dosage Map Conflict"
                           :tooltip "Dosage Map Conflict"}
   "CG:ErrorInVariantDescription" {:label "Variant Error"
                                   :tooltip "Error in Variant Description"}})

(def submitter-labels
  {"GENCC:000111" "PanelApp Australia",
   "GENCC:000115" "Broad Center for Mendelian Genomics",
   "GENCC:000104" "Genomics England PanelApp",
   "GENCC:000116" "Baylor College of Medicine Research Center",
   "GENCC:000112" "G2P",
   "GENCC:000113" "Franklin by Genoox",
   "GENCC:000105" "Illumina",
   "GENCC:000107" "Laboratory for Molecular Medicine",
   "GENCC:000102" "ClinGen",
   "GENCC:000106" "Invitae",
   "GENCC:000110" "Orphanet",
   "GENCC:000108" "Myriad Women’s Health",
   "GENCC:000101" "Ambry Genetics",
   "GENCC:000114" "King Faisal Specialist Hospital and Research Center"})

(defn pill [curie]
  (let [{:keys [label]} (curie->pill curie {:label curie :tooltip curie})]
    [:div
     {:class "bg-gray-100 m-1 px-2 rounded-full h-fit"}
     label]))
