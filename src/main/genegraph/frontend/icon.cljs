(ns genegraph.frontend.icon)


;; from https://heroicons.com/

(def home
  [:svg
   {:class "size-6 shrink-0",
    :fill "none",
    :viewBox "0 0 24 24",
    :stroke-width "1.5",
    :stroke "currentColor",
    :aria-hidden "true",
    :data-slot "icon"}
   [:path
    {:stroke-linecap "round",
     :stroke-linejoin "round",
     :d
     "m2.25 12 8.954-8.955c.44-.439 1.152-.439 1.591 0L21.75 12M4.5 9.75v10.125c0 .621.504 1.125 1.125 1.125H9.75v-4.875c0-.621.504-1.125 1.125-1.125h2.25c.621 0 1.125.504 1.125 1.125V21h4.125c.621 0 1.125-.504 1.125-1.125V9.75M8.25 21h8.25"}]])

(def arrow-down-tray
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :fill "none",
    :viewBox "0 0 24 24",
    :stroke-width "1.5",
    :stroke "currentColor",
    :class "size-6"}
   [:path
    {:stroke-linecap "round",
     :stroke-linejoin "round",
     :d
     "M3 16.5v2.25A2.25 2.25 0 0 0 5.25 21h13.5A2.25 2.25 0 0 0 21 18.75V16.5M16.5 12 12 16.5m0 0L7.5 12m4.5 4.5V3"}]])

(def document-text
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :fill "none",
    :viewBox "0 0 24 24",
    :stroke-width "1.5",
    :stroke "currentColor",
    :class "size-6"}
   [:path
    {:stroke-linecap "round",
     :stroke-linejoin "round",
     :d
     "M19.5 14.25v-2.625a3.375 3.375 0 0 0-3.375-3.375h-1.5A1.125 1.125 0 0 1 13.5 7.125v-1.5a3.375 3.375 0 0 0-3.375-3.375H8.25m0 12.75h7.5m-7.5 3H12M10.5 2.25H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 0 0-9-9Z"}]])

(def funnel
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :fill "none",
    :viewBox "0 0 24 24",
    :stroke-width "1.5",
    :stroke "currentColor",
    :class "size-6"}
   [:path
    {:stroke-linecap "round",
     :stroke-linejoin "round",
     :d
     "M12 3c2.755 0 5.455.232 8.083.678.533.09.917.556.917 1.096v1.044a2.25 2.25 0 0 1-.659 1.591l-5.432 5.432a2.25 2.25 0 0 0-.659 1.591v2.927a2.25 2.25 0 0 1-1.244 2.013L9.75 21v-6.568a2.25 2.25 0 0 0-.659-1.591L3.659 7.409A2.25 2.25 0 0 1 3 5.818V4.774c0-.54.384-1.006.917-1.096A48.32 48.32 0 0 1 12 3Z"}]])

(def book-open
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :fill "none",
    :viewBox "0 0 24 24",
    :stroke-width "1.5",
    :stroke "currentColor",
    :class "size-6"}
   [:path
    {:stroke-linecap "round",
     :stroke-linejoin "round",
     :d
     "M12 6.042A8.967 8.967 0 0 0 6 3.75c-1.052 0-2.062.18-3 .512v14.25A8.987 8.987 0 0 1 6 18c2.305 0 4.408.867 6 2.292m0-14.25a8.966 8.966 0 0 1 6-2.292c1.052 0 2.062.18 3 .512v14.25A8.987 8.987 0 0 0 18 18a8.967 8.967 0 0 0-6 2.292m0-14.25v14.25"}]])

(def folder
  [:svg
   {:class "size-6 shrink-0",
    :fill "none",
    :viewBox "0 0 24 24",
    :stroke-width "1.5",
    :stroke "currentColor",
    :aria-hidden "true",
    :data-slot "icon"}
   [:path
    {:stroke-linecap "round",
     :stroke-linejoin "round",
     :d
     "M2.25 12.75V12A2.25 2.25 0 0 1 4.5 9.75h15A2.25 2.25 0 0 1 21.75 12v.75m-8.69-6.44-2.12-2.12a1.5 1.5 0 0 0-1.061-.44H4.5A2.25 2.25 0 0 0 2.25 6v12a2.25 2.25 0 0 0 2.25 2.25h15A2.25 2.25 0 0 0 21.75 18V9a2.25 2.25 0 0 0-2.25-2.25h-5.379a1.5 1.5 0 0 1-1.06-.44Z"}]])

(def arrow
  [:svg
   {:class "size-5 flex-none text-gray-400",
    :viewBox "0 0 20 20",
    :fill "currentColor",
    :aria-hidden "true",
    :data-slot "icon"}
   [:path
    {:fill-rule "evenodd",
     :d
     "M8.22 5.22a.75.75 0 0 1 1.06 0l4.25 4.25a.75.75 0 0 1 0 1.06l-4.25 4.25a.75.75 0 0 1-1.06-1.06L11.94 10 8.22 6.28a.75.75 0 0 1 0-1.06Z",
     :clip-rule "evenodd"}]])

(def document-text-outline
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :fill "none",
    :viewBox "0 0 24 24",
    :stroke-width "1.5",
    :stroke "currentColor",
    :class "size-6"}
   [:path
    {:stroke-linecap "round",
     :stroke-linejoin "round",
     :d
     "M19.5 14.25v-2.625a3.375 3.375 0 0 0-3.375-3.375h-1.5A1.125 1.125 0 0 1 13.5 7.125v-1.5a3.375 3.375 0 0 0-3.375-3.375H8.25m0 12.75h7.5m-7.5 3H12M10.5 2.25H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 0 0-9-9Z"}]])

(def document-micro
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :viewBox "0 0 16 16",
    :fill "currentColor",
    :class "size-4"}
   [:path
    {:d
     "M2.5 3.5A1.5 1.5 0 0 1 4 2h4.879a1.5 1.5 0 0 1 1.06.44l3.122 3.12a1.5 1.5 0 0 1 .439 1.061V12.5A1.5 1.5 0 0 1 12 14H4a1.5 1.5 0 0 1-1.5-1.5v-9Z"}]])

(def paper-clip-outline
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :fill "none",
    :viewBox "0 0 24 24",
    :stroke-width "1.5",
    :stroke "currentColor",
    :class "size-6"}
   [:path
    {:stroke-linecap "round",
     :stroke-linejoin "round",
     :d
     "m18.375 12.739-7.693 7.693a4.5 4.5 0 0 1-6.364-6.364l10.94-10.94A3 3 0 1 1 19.5 7.372L8.552 18.32m.009-.01-.01.01m5.699-9.941-7.81 7.81a1.5 1.5 0 0 0 2.112 2.13"}]])


(def building-office-outline
  [:svg
 {:xmlns "http://www.w3.org/2000/svg",
  :fill "none",
  :viewBox "0 0 24 24",
  :stroke-width "1.5",
  :stroke "currentColor",
  :class "size-6"}
 [:path
  {:stroke-linecap "round",
   :stroke-linejoin "round",
   :d
   "M3.75 21h16.5M4.5 3h15M5.25 3v18m13.5-18v18M9 6.75h1.5m-1.5 3h1.5m-1.5 3h1.5m3-6H15m-1.5 3H15m-1.5 3H15M9 21v-3.375c0-.621.504-1.125 1.125-1.125h3.75c.621 0 1.125.504 1.125 1.125V21"}]])

(def presentation-chart-outline
  [:svg
 {:xmlns "http://www.w3.org/2000/svg",
  :fill "none",
  :viewBox "0 0 24 24",
  :stroke-width "1.5",
  :stroke "currentColor",
  :class "size-6"}
 [:path
  {:stroke-linecap "round",
   :stroke-linejoin "round",
   :d
   "M3.75 3v11.25A2.25 2.25 0 0 0 6 16.5h2.25M3.75 3h-1.5m1.5 0h16.5m0 0h1.5m-1.5 0v11.25A2.25 2.25 0 0 1 18 16.5h-2.25m-7.5 0h7.5m-7.5 0-1 3m8.5-3 1 3m0 0 .5 1.5m-.5-1.5h-9.5m0 0-.5 1.5m.75-9 3-3 2.148 2.148A12.061 12.061 0 0 1 16.5 7.605"}]])

(def document-arrow-down-outline
  [:svg
 {:xmlns "http://www.w3.org/2000/svg",
  :fill "none",
  :viewBox "0 0 24 24",
  :stroke-width "1.5",
  :stroke "currentColor",
  :class "size-6"}
 [:path
  {:stroke-linecap "round",
   :stroke-linejoin "round",
   :d
   "M19.5 14.25v-2.625a3.375 3.375 0 0 0-3.375-3.375h-1.5A1.125 1.125 0 0 1 13.5 7.125v-1.5a3.375 3.375 0 0 0-3.375-3.375H8.25m.75 12 3 3m0 0 3-3m-3 3v-6m-1.5-9H5.625c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125V11.25a9 9 0 0 0-9-9Z"}]])

(def building-office-mini
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :viewBox "0 0 20 20",
    :fill "currentColor",
    :class "size-5"}
   [:path
    {:fill-rule "evenodd",
     :d
     "M4 16.5v-13h-.25a.75.75 0 0 1 0-1.5h12.5a.75.75 0 0 1 0 1.5H16v13h.25a.75.75 0 0 1 0 1.5h-3.5a.75.75 0 0 1-.75-.75v-2.5a.75.75 0 0 0-.75-.75h-2.5a.75.75 0 0 0-.75.75v2.5a.75.75 0 0 1-.75.75h-3.5a.75.75 0 0 1 0-1.5H4Zm3-11a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-1a.5.5 0 0 1-.5-.5v-1ZM7.5 9a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h1a.5.5 0 0 0 .5-.5v-1a.5.5 0 0 0-.5-.5h-1ZM11 5.5a.5.5 0 0 1 .5-.5h1a.5.5 0 0 1 .5.5v1a.5.5 0 0 1-.5.5h-1a.5.5 0 0 1-.5-.5v-1Zm.5 3.5a.5.5 0 0 0-.5.5v1a.5.5 0 0 0 .5.5h1a.5.5 0 0 0 .5-.5v-1a.5.5 0 0 0-.5-.5h-1Z",
     :clip-rule "evenodd"}]])

(def chart-pie-mini
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :viewBox "0 0 20 20",
    :fill "currentColor",
    :class "size-5"}
   [:path
    {:d
     "M12 9a1 1 0 0 1-1-1V3c0-.552.45-1.007.997-.93a7.004 7.004 0 0 1 5.933 5.933c.078.547-.378.997-.93.997h-5Z"}]
   [:path
    {:d
     "M8.003 4.07C8.55 3.994 9 4.449 9 5v5a1 1 0 0 0 1 1h5c.552 0 1.008.45.93.997A7.001 7.001 0 0 1 2 11a7.002 7.002 0 0 1 6.003-6.93Z"}]])

(def document-arrow-down-mini
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :viewBox "0 0 20 20",
    :fill "currentColor",
    :class "size-5"}
   [:path
    {:fill-rule "evenodd",
     :d
     "M4.5 2A1.5 1.5 0 0 0 3 3.5v13A1.5 1.5 0 0 0 4.5 18h11a1.5 1.5 0 0 0 1.5-1.5V7.621a1.5 1.5 0 0 0-.44-1.06l-4.12-4.122A1.5 1.5 0 0 0 11.378 2H4.5Zm4.75 6.75a.75.75 0 0 1 1.5 0v2.546l.943-1.048a.75.75 0 0 1 1.114 1.004l-2.25 2.5a.75.75 0 0 1-1.114 0l-2.25-2.5a.75.75 0 1 1 1.114-1.004l.943 1.048V8.75Z",
     :clip-rule "evenodd"}]])

(def magnifying-glass
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :fill "none",
    :viewBox "0 0 24 24",
    :stroke-width "1.5",
    :stroke "currentColor",
    :class "size-6"}
   [:path
    {:stroke-linecap "round",
     :stroke-linejoin "round",
     :d
     "m21 21-5.197-5.197m0 0A7.5 7.5 0 1 0 5.196 5.196a7.5 7.5 0 0 0 10.607 10.607Z"}]])

(def question-mark-circle-mini
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :viewBox "0 0 20 20",
    :fill "currentColor",
    :class "size-5"}
   [:path
    {:fill-rule "evenodd",
     :d
     "M18 10a8 8 0 1 1-16 0 8 8 0 0 1 16 0ZM8.94 6.94a.75.75 0 1 1-1.061-1.061 3 3 0 1 1 2.871 5.026v.345a.75.75 0 0 1-1.5 0v-.5c0-.72.57-1.172 1.081-1.287A1.5 1.5 0 1 0 8.94 6.94ZM10 15a1 1 0 1 0 0-2 1 1 0 0 0 0 2Z",
     :clip-rule "evenodd"}]])

(def chat-bubble-bottom-center-text-mini
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :viewBox "0 0 20 20",
    :fill "currentColor",
    :class "size-5"}
   [:path
    {:fill-rule "evenodd",
     :d
     "M18 10a8 8 0 1 1-16 0 8 8 0 0 1 16 0ZM8.94 6.94a.75.75 0 1 1-1.061-1.061 3 3 0 1 1 2.871 5.026v.345a.75.75 0 0 1-1.5 0v-.5c0-.72.57-1.172 1.081-1.287A1.5 1.5 0 1 0 8.94 6.94ZM10 15a1 1 0 1 0 0-2 1 1 0 0 0 0 2Z",
     :clip-rule "evenodd"}]])

(def home-mini
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :viewBox "0 0 20 20",
    :fill "currentColor",
    :class "size-5"}
   [:path
    {:fill-rule "evenodd",
     :d
     "M9.293 2.293a1 1 0 0 1 1.414 0l7 7A1 1 0 0 1 17 11h-1v6a1 1 0 0 1-1 1h-2a1 1 0 0 1-1-1v-3a1 1 0 0 0-1-1H9a1 1 0 0 0-1 1v3a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1v-6H3a1 1 0 0 1-.707-1.707l7-7Z",
     :clip-rule "evenodd"}]])

(def envelope-mini
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :fill "none",
    :viewBox "0 0 24 24",
    :stroke-width "1.5",
    :stroke "currentColor",
    :class "size-6"}
   [:path
    {:stroke-linecap "round",
     :stroke-linejoin "round",
     :d
     "M3.75 3v11.25A2.25 2.25 0 0 0 6 16.5h2.25M3.75 3h-1.5m1.5 0h16.5m0 0h1.5m-1.5 0v11.25A2.25 2.25 0 0 1 18 16.5h-2.25m-7.5 0h7.5m-7.5 0-1 3m8.5-3 1 3m0 0 .5 1.5m-.5-1.5h-9.5m0 0-.5 1.5m.75-9 3-3 2.148 2.148A12.061 12.061 0 0 1 16.5 7.605"}]])

(def beaker-mini
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :viewBox "0 0 20 20",
    :fill "currentColor",
    :class "size-5"}
   [:path
    {:fill-rule "evenodd",
     :d
     "M8.5 3.528v4.644c0 .729-.29 1.428-.805 1.944l-1.217 1.216a8.75 8.75 0 0 1 3.55.621l.502.201a7.25 7.25 0 0 0 4.178.365l-2.403-2.403a2.75 2.75 0 0 1-.805-1.944V3.528a40.205 40.205 0 0 0-3 0Zm4.5.084.19.015a.75.75 0 1 0 .12-1.495 41.364 41.364 0 0 0-6.62 0 .75.75 0 0 0 .12 1.495L7 3.612v4.56c0 .331-.132.649-.366.883L2.6 13.09c-1.496 1.496-.817 4.15 1.403 4.475C5.961 17.852 7.963 18 10 18s4.039-.148 5.997-.436c2.22-.325 2.9-2.979 1.403-4.475l-4.034-4.034A1.25 1.25 0 0 1 13 8.172v-4.56Z",
     :clip-rule "evenodd"}]])


(def beaker-micro
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :viewBox "0 0 16 16",
    :fill "currentColor",
    :class "size-4"}
   [:path
    {:fill-rule "evenodd",
     :d
     "M11 3.5v2.257c0 .597.237 1.17.659 1.591l2.733 2.733c.39.39.608.918.608 1.469a2.04 2.04 0 0 1-1.702 2.024C11.573 13.854 9.803 14 8 14s-3.573-.146-5.298-.426A2.04 2.04 0 0 1 1 11.55c0-.551.219-1.08.608-1.47l2.733-2.732A2.25 2.25 0 0 0 5 5.758V3.5h-.25a.75.75 0 0 1 0-1.5h6.5a.75.75 0 0 1 0 1.5H11ZM6.5 5.757V3.5h3v2.257a3.75 3.75 0 0 0 1.098 2.652l.158.158a3.36 3.36 0 0 0-.075.034c-.424.2-.916.194-1.335-.016l-1.19-.595a4.943 4.943 0 0 0-2.07-.52A3.75 3.75 0 0 0 6.5 5.757Z",
     :clip-rule "evenodd"}]])

(def building-office-micro
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :viewBox "0 0 16 16",
    :fill "currentColor",
    :class "size-4"}
   [:path
    {:fill-rule "evenodd",
     :d
     "M3.75 2a.75.75 0 0 0 0 1.5H4v9h-.25a.75.75 0 0 0 0 1.5H6a.5.5 0 0 0 .5-.5v-3A.5.5 0 0 1 7 10h2a.5.5 0 0 1 .5.5v3a.5.5 0 0 0 .5.5h2.25a.75.75 0 0 0 0-1.5H12v-9h.25a.75.75 0 0 0 0-1.5h-8.5ZM6.5 4a.5.5 0 0 0-.5.5V5a.5.5 0 0 0 .5.5H7a.5.5 0 0 0 .5-.5v-.5A.5.5 0 0 0 7 4h-.5ZM6 7a.5.5 0 0 1 .5-.5H7a.5.5 0 0 1 .5.5v.5A.5.5 0 0 1 7 8h-.5a.5.5 0 0 1-.5-.5V7Zm3-3a.5.5 0 0 0-.5.5V5a.5.5 0 0 0 .5.5h.5A.5.5 0 0 0 10 5v-.5a.5.5 0 0 0-.5-.5H9Zm-.5 3a.5.5 0 0 1 .5-.5h.5a.5.5 0 0 1 .5.5v.5a.5.5 0 0 1-.5.5H9a.5.5 0 0 1-.5-.5V7Z",
     :clip-rule "evenodd"}]])

(def chart-pie-micro
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :viewBox "0 0 16 16",
    :fill "currentColor",
    :class "size-4"}
   [:path
    {:d
     "M13.975 6.5c.028.276-.199.5-.475.5h-4a.5.5 0 0 1-.5-.5v-4c0-.276.225-.503.5-.475A5.002 5.002 0 0 1 13.974 6.5Z"}]
   [:path
    {:d
     "M6.5 4.025c.276-.028.5.199.5.475v4a.5.5 0 0 0 .5.5h4c.276 0 .503.225.475.5a5 5 0 1 1-5.474-5.475Z"}]])

(def document-arrow-down-micro
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :viewBox "0 0 16 16",
    :fill "currentColor",
    :class "size-4"}
   [:path
    {:fill-rule "evenodd",
     :d
     "M4 2a1.5 1.5 0 0 0-1.5 1.5v9A1.5 1.5 0 0 0 4 14h8a1.5 1.5 0 0 0 1.5-1.5V6.621a1.5 1.5 0 0 0-.44-1.06L9.94 2.439A1.5 1.5 0 0 0 8.878 2H4Zm4 3.5a.75.75 0 0 1 .75.75v2.69l.72-.72a.75.75 0 1 1 1.06 1.06l-2 2a.75.75 0 0 1-1.06 0l-2-2a.75.75 0 0 1 1.06-1.06l.72.72V6.25A.75.75 0 0 1 8 5.5Z",
     :clip-rule "evenodd"}]])

(def question-mark-circle-micro
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :viewBox "0 0 16 16",
    :fill "currentColor",
    :class "size-4"}
   [:path
    {:fill-rule "evenodd",
     :d
     "M15 8A7 7 0 1 1 1 8a7 7 0 0 1 14 0Zm-6 3.5a1 1 0 1 1-2 0 1 1 0 0 1 2 0ZM7.293 5.293a1 1 0 1 1 .99 1.667c-.459.134-1.033.566-1.033 1.29v.25a.75.75 0 1 0 1.5 0v-.115a2.5 2.5 0 1 0-2.518-4.153.75.75 0 1 0 1.061 1.06Z",
     :clip-rule "evenodd"}]])

(def chat-bubble-bottom-center-text-micro
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :viewBox "0 0 16 16",
    :fill "currentColor",
    :class "size-4"}
   [:path
    {:fill-rule "evenodd",
     :d
     "M1 8.74c0 .983.713 1.825 1.69 1.943.904.108 1.817.19 2.737.243.363.02.688.231.85.556l1.052 2.103a.75.75 0 0 0 1.342 0l1.052-2.103c.162-.325.487-.535.85-.556.92-.053 1.833-.134 2.738-.243.976-.118 1.689-.96 1.689-1.942V4.259c0-.982-.713-1.824-1.69-1.942a44.45 44.45 0 0 0-10.62 0C1.712 2.435 1 3.277 1 4.26v4.482Zm3-3.49a.75.75 0 0 1 .75-.75h6.5a.75.75 0 0 1 0 1.5h-6.5A.75.75 0 0 1 4 5.25ZM4.75 7a.75.75 0 0 0 0 1.5h2.5a.75.75 0 0 0 0-1.5h-2.5Z",
     :clip-rule "evenodd"}]])

(def home-micro
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :viewBox "0 0 16 16",
    :fill "currentColor",
    :class "size-4"}
   [:path
    {:d
     "M8.543 2.232a.75.75 0 0 0-1.085 0l-5.25 5.5A.75.75 0 0 0 2.75 9H4v4a1 1 0 0 0 1 1h1a1 1 0 0 0 1-1v-1a1 1 0 1 1 2 0v1a1 1 0 0 0 1 1h1a1 1 0 0 0 1-1V9h1.25a.75.75 0 0 0 .543-1.268l-5.25-5.5Z"}]])

(def envelope-micro
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :viewBox "0 0 16 16",
    :fill "currentColor",
    :class "size-4"}
   [:path
    {:d
     "M2.5 3A1.5 1.5 0 0 0 1 4.5v.793c.026.009.051.02.076.032L7.674 8.51c.206.1.446.1.652 0l6.598-3.185A.755.755 0 0 1 15 5.293V4.5A1.5 1.5 0 0 0 13.5 3h-11Z"}]
   [:path
    {:d
     "M15 6.954 8.978 9.86a2.25 2.25 0 0 1-1.956 0L1 6.954V11.5A1.5 1.5 0 0 0 2.5 13h11a1.5 1.5 0 0 0 1.5-1.5V6.954Z"}]])


(def link-micro
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :viewBox "0 0 16 16",
    :fill "currentColor",
    :class "size-4"}
   [:path
    {:fill-rule "evenodd",
     :d
     "M8.914 6.025a.75.75 0 0 1 1.06 0 3.5 3.5 0 0 1 0 4.95l-2 2a3.5 3.5 0 0 1-5.396-4.402.75.75 0 0 1 1.251.827 2 2 0 0 0 3.085 2.514l2-2a2 2 0 0 0 0-2.828.75.75 0 0 1 0-1.06Z",
     :clip-rule "evenodd"}]
   [:path
    {:fill-rule "evenodd",
     :d
     "M7.086 9.975a.75.75 0 0 1-1.06 0 3.5 3.5 0 0 1 0-4.95l2-2a3.5 3.5 0 0 1 5.396 4.402.75.75 0 0 1-1.251-.827 2 2 0 0 0-3.085-2.514l-2 2a2 2 0 0 0 0 2.828.75.75 0 0 1 0 1.06Z",
     :clip-rule "evenodd"}]])

(def list-bullet
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :fill "none",
    :viewBox "0 0 24 24",
    :stroke-width "1.5",
    :stroke "currentColor",
    :class "size-6"}
   [:path
    {:stroke-linecap "round",
     :stroke-linejoin "round",
     :d
     "M8.25 6.75h12M8.25 12h12m-12 5.25h12M3.75 6.75h.007v.008H3.75V6.75Zm.375 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0ZM3.75 12h.007v.008H3.75V12Zm.375 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Zm-.375 5.25h.007v.008H3.75v-.008Zm.375 0a.375.375 0 1 1-.75 0 .375.375 0 0 1 .75 0Z"}]])


(def link
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :fill "none",
    :viewBox "0 0 24 24",
    :stroke-width "1.5",
    :stroke "currentColor",
    :class "size-6"}
   [:path
    {:stroke-linecap "round",
     :stroke-linejoin "round",
     :d
     "M13.19 8.688a4.5 4.5 0 0 1 1.242 7.244l-4.5 4.5a4.5 4.5 0 0 1-6.364-6.364l1.757-1.757m13.35-.622 1.757-1.757a4.5 4.5 0 0 0-6.364-6.364l-4.5 4.5a4.5 4.5 0 0 0 1.242 7.244"}]])


(def document-check
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :fill "none",
    :viewBox "0 0 24 24",
    :stroke-width "1.5",
    :stroke "currentColor",
    :class "size-10"}
   [:path
    {:stroke-linecap "round",
     :stroke-linejoin "round",
     :d
     "M10.125 2.25h-4.5c-.621 0-1.125.504-1.125 1.125v17.25c0 .621.504 1.125 1.125 1.125h12.75c.621 0 1.125-.504 1.125-1.125v-9M10.125 2.25h.375a9 9 0 0 1 9 9v.375M10.125 2.25A3.375 3.375 0 0 1 13.5 5.625v1.5c0 .621.504 1.125 1.125 1.125h1.5a3.375 3.375 0 0 1 3.375 3.375M9 15l2.25 2.25L15 12"}]])

(def filter-icon
  [:svg
   {:xmlns "http://www.w3.org/2000/svg",
    :fill "none",
    :viewBox "0 0 24 24",
    :stroke-width "1.5",
    :stroke "currentColor",
    :class "size-6"}
   [:path
    {:stroke-linecap "round",
     :stroke-linejoin "round",
     :d
     "M12 3c2.755 0 5.455.232 8.083.678.533.09.917.556.917 1.096v1.044a2.25 2.25 0 0 1-.659 1.591l-5.432 5.432a2.25 2.25 0 0 0-.659 1.591v2.927a2.25 2.25 0 0 1-1.244 2.013L9.75 21v-6.568a2.25 2.25 0 0 0-.659-1.591L3.659 7.409A2.25 2.25 0 0 1 3 5.818V4.774c0-.54.384-1.006.917-1.096A48.32 48.32 0 0 1 12 3Z"}]])
