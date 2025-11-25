(ns genegraph.frontend.page.reports
  (:require [genegraph.frontend.charts :as charts]
            [re-frame.core :as rf]))

(rf/reg-event-db
:chart/set
(fn [db [_ chart-map]]
;; chart-map should contain {:data ... :layout ... :config ...}
(assoc db :chart chart-map)))



(def chart1
  {:data [{:x "stonks" "stonks!" "stonks!!"
           :y [1 2 3]
           :name "stonks!!11!"
           :type "bar"}]})

(def chart2
  {:data [{:x "stonks" "stonks!" "stonks!!"
           :y [3 2 1]
           :name "stonks!!11!"
           :type "bar"}]})


(rf/reg-sub
:chart/data
(fn [db _]
(:chart db chart1)))

(defn reports-div []
  [:div
   [:div
    {:on-click #(rf/dispatch [:chart/set chart1])}
    "report1"]
   [:div
    {:on-click #(rf/dispatch [:chart/set chart2])}
    "report2"]
   [charts/plotly-chart @(rf/subscribe [:chart/data])]])
