(ns genegraph.frontend.page.docs
  "Documentation browser for the schema defined in genegraph.new-schema.

  The schema is a flat vector of maps, each with an :id and a :type of
  :rdfs/Class, :rdf/Property, :skos/Collection (a value set) or
  :skos/Concept (a member of a value set). Relationships between terms are
  expressed by reference (a class lists its properties under cardinality
  keys, a property names its :range and :value-set, a value set names its
  :skos/member), so most of this namespace is concerned with inverting
  those references so a term can be read from either direction."
  (:require [re-frame.core :as rf]
            [clojure.string :as s]
            [genegraph.new-schema :as new-schema]))

;; * Schema indexes
;;
;; The schema is static, so these are all computed once at load time.
;; Terms may appear more than once in the source vector; indexing by :id
;; keeps the last definition and drops the duplicates.

(def index
  (reduce #(assoc %1 (:id %2) %2) {} new-schema/schema))

(defn terms-of-type [type]
  (->> (vals index)
       (filter #(= type (:type %)))
       (sort-by :id)))

(def cardinalities
  "Keys under which a class lists its properties, in display order. The
  schema uses both :zeroOrMoreOf and :zeroOrManyOf for the same thing."
  [{:key :oneOf :label "Required" :qualifier "exactly one"}
   {:key :oneOrMoreOf :label "Required" :qualifier "one or more"}
   {:key :zeroOrOneOf :label "Optional" :qualifier "at most one"}
   {:key :zeroOrMoreOf :label "Optional" :qualifier "any number"}
   {:key :zeroOrManyOf :label "Optional" :qualifier "any number"}])

(def property-usage
  "property id -> [{:class class-id :cardinality cardinality-map}]"
  (reduce
   (fn [usage class]
     (reduce
      (fn [usage cardinality]
        (reduce
         (fn [usage property]
           (update usage property (fnil conj []) {:class (:id class)
                                                  :cardinality cardinality}))
         usage
         (get class (:key cardinality))))
      usage
      cardinalities))
   {}
   (terms-of-type :rdfs/Class)))

(def value-set-usage
  "value set id -> [property id]"
  (reduce
   (fn [usage property]
     (if-let [vs (:value-set property)]
       (update usage vs (fnil conj []) (:id property))
       usage))
   {}
   (terms-of-type :rdf/Property)))

(def range-usage
  "id of anything used as a range -> [property id]"
  (reduce
   (fn [usage property]
     (if-let [r (:range property)]
       (update usage r (fnil conj []) (:id property))
       usage))
   {}
   (terms-of-type :rdf/Property)))

(def concept-membership
  "concept id -> [value set id]"
  (reduce
   (fn [membership value-set]
     (reduce
      (fn [membership concept]
        (update membership concept (fnil conj []) (:id value-set)))
      membership
      (:skos/member value-set)))
   {}
   (terms-of-type :skos/Collection)))

;; * State

(rf/reg-event-db
 ::select-term
 (fn [db [_ term]]
   (assoc db ::selected-term term)))

(rf/reg-sub
 ::selected-term
 :-> ::selected-term)

(rf/reg-event-db
 ::set-filter-text
 (fn [db [_ text]]
   (assoc db ::filter-text text)))

(rf/reg-sub
 ::filter-text
 :-> ::filter-text)

(rf/reg-event-db
 ::toggle-group
 (fn [db [_ type]]
   (update db ::open-groups #(if (contains? % type)
                               (disj % type)
                               (conj (or % #{}) type)))))

(rf/reg-sub
 ::open-groups
 (fn [db _] (::open-groups db #{:rdfs/Class})))

(rf/reg-event-db
 ::toggle-internal
 (fn [db _]
   (update db ::show-internal? not)))

(rf/reg-sub
 ::show-internal?
 :-> ::show-internal?)

;; * Display helpers

(def type-labels
  {:rdfs/Class "Class"
   :rdf/Property "Property"
   :skos/Collection "Value Set"
   :skos/Concept "Concept"})

(def maturity-labels
  {:cg/Draft "Draft"
   :cg/Volatile "Volatile"
   :cg/Internal "Internal"})

(def maturity-styles
  {:cg/Draft "bg-sky-50 text-sky-700 ring-sky-600/20"
   :cg/Volatile "bg-amber-50 text-amber-800 ring-amber-600/20"
   :cg/Internal "bg-gray-100 text-gray-600 ring-gray-500/20"})

(defn badge [text style]
  [:span
   {:class (str "inline-flex items-center rounded-md px-2 py-1 text-xs font-medium ring-1 ring-inset " style)}
   text])

(defn maturity-badge [term]
  (when-let [m (:maturity term)]
    (badge (get maturity-labels m (name m))
           (get maturity-styles m "bg-gray-100 text-gray-600 ring-gray-500/20"))))

(defn type-badge [term]
  (badge (get type-labels (:type term) (str (:type term)))
         "bg-gray-100 text-gray-700 ring-gray-500/20"))

(defn term-ref
  "A link to another term. Terms with no definition of their own -- the
  primitives used as ranges, such as :String -- are rendered as plain text."
  [id]
  (if (contains? index id)
    [:button
     {:type "button"
      :class "text-left font-medium text-sky-700 hover:text-sky-900 hover:underline"
      :on-click #(rf/dispatch [::select-term id])}
     (str id)]
    [:span {:class "text-gray-500"} (str id)]))

(defn description [term]
  (when-let [d (:description term)]
    [:p {:class "max-w-3xl text-sm text-gray-700"} d]))

(defn notes [term]
  (let [show-internal? @(rf/subscribe [::show-internal?])]
    [:div
     (when-let [n (:note term)]
       [:div
        {:class "mt-4 max-w-3xl border-l-2 border-gray-200 pl-4 text-sm text-gray-500"}
        [:span {:class "font-medium text-gray-600"} "Note: "]
        n])
     (when (and show-internal? (:internal-note term))
       [:div
        {:class "mt-4 max-w-3xl border-l-2 border-amber-300 pl-4 text-sm text-amber-800"}
        [:span {:class "font-medium"} "Internal note: "]
        (:internal-note term)])
     (when (and show-internal? (:internal-options term))
       [:div
        {:class "mt-4 max-w-3xl border-l-2 border-amber-300 pl-4 text-sm text-amber-800"}
        [:span {:class "font-medium"} "Internal options: "]
        (s/join ", " (:internal-options term))])]))

(defn section-heading [title]
  [:div
   {:class "border-b border-gray-200 pb-3 pt-10"}
   [:h3 {:class "text-base font-semibold text-gray-900"} title]])

(defn empty-note [text]
  [:p {:class "pt-4 text-sm text-gray-500 italic"} text])

;; * Term tables
;;
;; Everything below renders a list of terms as a three column grid:
;; the term itself, how it relates to the term being displayed, and its
;; description.

(defn term-row [id middle]
  ^{:key id}
  [:li
   {:class "grid col-span-3 grid-cols-subgrid py-3"}
   [:div (term-ref id)]
   [:div {:class "text-sm text-gray-500"} middle]
   [:div {:class "text-sm text-gray-500"} (:description (get index id))]])

(defn term-table [rows]
  [:ul
   {:role "list"
    :class "grid grid-cols-[minmax(12rem,auto)_minmax(8rem,auto)_minmax(0,1fr)] gap-x-6 divide-y divide-gray-100"}
   rows])

(defn property-range
  "The range of a property, pointing at the value set where the range is a
  concept drawn from one."
  [property]
  [:div
   {:class "flex flex-col gap-1"}
   (when-let [r (:range property)] (term-ref r))
   (when-let [vs (:value-set property)]
     [:div {:class "text-xs"} (term-ref vs)])])

(defn property-rows [property-ids]
  (for [id property-ids]
    (term-row id (property-range (get index id)))))

;; * Term detail

(defmulti term-detail :type)

(defmethod term-detail :rdfs/Class [class]
  [:div
   (doall
    (for [{:keys [key label qualifier]} cardinalities
          :when (seq (get class key))]
      ^{:key key}
      [:div
       (section-heading (str label " properties (" qualifier ")"))
       (term-table (doall (property-rows (get class key))))]))
   (when-let [properties (get range-usage (:id class))]
     [:div
      (section-heading "Referenced by")
      (term-table
       (doall
        (for [id (sort properties)]
          (term-row id "range"))))])])

(defmethod term-detail :rdf/Property [property]
  [:div
   [:div
    (section-heading "Range")
    [:div
     {:class "pt-4 text-sm"}
     (property-range property)
     (when-let [jt (:json-type property)]
       [:div {:class "pt-1 text-gray-500"} "JSON representation: " (str jt)])]]
   (when-let [members (:skos/member (get index (:value-set property)))]
     [:div
      (section-heading "Permitted values")
      (term-table (doall (for [id members] (term-row id nil))))])
   [:div
    (section-heading "Used by")
    (if-let [usage (get property-usage (:id property))]
      (term-table
       (doall
        (for [{:keys [class cardinality]} (sort-by :class usage)]
          (term-row class (str (:label cardinality)
                               ", "
                               (:qualifier cardinality))))))
      (empty-note "Not used by any class in the schema."))]])

(defmethod term-detail :skos/Collection [value-set]
  [:div
   [:div
    (section-heading "Members")
    (term-table (doall (for [id (:skos/member value-set)] (term-row id nil))))]
   [:div
    (section-heading "Used by")
    (if-let [properties (get value-set-usage (:id value-set))]
      (term-table (doall (property-rows (sort properties))))
      (empty-note "Not used by any property in the schema."))]])

(defmethod term-detail :skos/Concept [concept]
  [:div
   (section-heading "Member of value sets")
   (if-let [value-sets (get concept-membership (:id concept))]
     (term-table (doall (for [id (sort value-sets)] (term-row id nil))))
     (empty-note "Not a member of any value set in the schema."))])

(defmethod term-detail :default [_]
  [:div])

(defn term-page [term]
  [:div
   [:div
    {:class "border-b border-gray-200 pb-5"}
    [:div
     {:class "flex gap-3 items-baseline"}
     [:h2 {:class "text-xl font-semibold text-gray-900"} (str (:id term))]
     (type-badge term)
     (maturity-badge term)]
    (when-let [r (:reference term)]
      [:div
       {:class "pt-2 text-sm text-gray-500"}
       "Aligned with " [:span {:class "font-medium text-gray-700"} (str r)]])
    [:div {:class "pt-3"} (description term)]
    (notes term)]
   (term-detail term)])

;; * Overview

(defn overview []
  [:div
   [:p
    {:class "max-w-3xl text-sm text-gray-700"}
    "The data model supporting ClinGen curations, and especially Gene
     Validity, is large, complex, and expanding. Select a term to see its
     documentation; starting with "
    (term-ref :cg/Statement)
    " is recommended."]
   [:div
    (section-heading "Classes")
    [:ul
     {:role "list", :class "divide-y divide-gray-100"}
     (doall
      (for [class (terms-of-type :rdfs/Class)]
        ^{:key (:id class)}
        [:li
         {:class "py-4"}
         [:div
          {:class "flex gap-3 items-baseline"}
          (term-ref (:id class))
          (maturity-badge class)]
         [:p {:class "max-w-3xl pt-1 text-sm text-gray-500"} (:description class)]]))]]])

;; * Navigation

(def nav-sections
  [{:type :rdfs/Class :label "Classes"}
   {:type :rdf/Property :label "Properties"}
   {:type :skos/Collection :label "Value Sets"}
   {:type :skos/Concept :label "Concepts"}])

(defn matches-filter? [term filter-text]
  (or (s/blank? filter-text)
      (let [q (s/lower-case filter-text)]
        (or (s/includes? (s/lower-case (str (:id term))) q)
            (s/includes? (s/lower-case (or (:description term) "")) q)))))

(defn nav-group [{:keys [type label]} filter-text selected]
  (let [filtering? (not (s/blank? filter-text))
        terms (filterv #(matches-filter? % filter-text) (terms-of-type type))
        open? (or filtering?
                  (contains? @(rf/subscribe [::open-groups]) type))]
    ^{:key type}
    [:div
     {:class "pt-4"}
     [:button
      {:type "button"
       :class "flex w-full items-baseline gap-2 text-left text-xs font-semibold uppercase tracking-wide text-gray-500 hover:text-gray-900"
       :on-click #(rf/dispatch [::toggle-group type])}
      [:span {:class "w-3"} (if open? "▾" "▸")]
      label
      [:span {:class "font-normal normal-case tracking-normal"}
       (str "(" (count terms) ")")]]
     (when open?
       [:ul
        {:role "list", :class "pl-5 pt-1"}
        (doall
         (for [term terms]
           ^{:key (:id term)}
           [:li
            [:button
             {:type "button"
              :class (if (= selected (:id term))
                       "block w-full truncate rounded px-2 py-1 text-left text-sm bg-sky-50 font-medium text-sky-800"
                       "block w-full truncate rounded px-2 py-1 text-left text-sm text-gray-700 hover:bg-gray-50")
              :on-click #(rf/dispatch [::select-term (:id term)])}
             (name (:id term))]]))])]))

(defn nav-column [filter-text selected]
  [:div
   {:class "w-72 shrink-0 sticky top-8 max-h-[calc(100vh-6rem)] overflow-y-auto pr-2"}
   [:input
    {:type "search"
     :placeholder "Filter terms"
     :value (or filter-text "")
     :on-change #(rf/dispatch [::set-filter-text (-> % .-target .-value)])
     :class "block w-full rounded-md border-0 py-1.5 px-3 text-sm text-gray-900 ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-sky-600"}]
   (doall
    (for [section nav-sections]
      (nav-group section filter-text selected)))])

;; * Page

(defn main []
  (let [selected @(rf/subscribe [::selected-term])
        filter-text @(rf/subscribe [::filter-text])]
    [:div
     {:class "px-12 py-12"}
     [:div
      {:class "flex items-baseline justify-between gap-4"}
      [:h1
       {:class "text-2xl/7 font-bold text-gray-900 sm:text-3xl sm:tracking-tight"}
       "Documentation"]
      #_[:label
       {:class "flex items-center gap-2 text-sm text-gray-500"}
       [:input
        {:type "checkbox"
         :checked (boolean @(rf/subscribe [::show-internal?]))
         :on-change #(rf/dispatch [::toggle-internal])
         :class "rounded border-gray-300 text-sky-600 focus:ring-sky-600"}]
       "Show internal notes"]]
     [:div
      {:class "flex items-start gap-10 pt-8"}
      (nav-column filter-text selected)
      [:div
       {:class "min-w-0 flex-1"}
       (if-let [term (get index selected)]
         (term-page term)
         (overview))]]]))
