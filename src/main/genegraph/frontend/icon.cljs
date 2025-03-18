(ns genegraph.frontend.icon)


;; from https://heroicons.com/

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

