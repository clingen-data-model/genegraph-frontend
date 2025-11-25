(ns genegraph.frontend.charts
  (:require
   [reagent.core :as r]
   ["plotly.js-basic-dist" :as Plotly]))


(defn plotly-chart [{:keys [data layout config]}]
  (let [container-ref (r/atom nil)]
    (r/create-class
     {:display-name "plotly-chart"

      :component-did-mount
      (fn [this]
        (let [node @container-ref]
          (Plotly/newPlot node (clj->js data) (clj->js layout) (clj->js config))))

      :component-did-update
      (fn [this _old-args]
        (let [props (-> this r/argv second)
              node @container-ref]
          (Plotly/react node
                        (clj->js (:data props))
                        (clj->js (:layout props))
                        (clj->js (:config props)))))

      :reagent-render
      (fn [_]
        ;; Attach the ref to the div
        [:div
         {:ref #(reset! container-ref %)
          :style {:width "100%" :height "100%"}}])})))
