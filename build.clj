;; Run with Babashka (bb build.clj)

(require '[babashka.process :refer [shell process exec]])
(require '[babashka.cli :as cli])
(require '[clojure.string :as str])

(def app-name "genegraph-frontend")

(defn version []
  (-> (shell {:out :string} "git rev-list HEAD --count")
      :out
      str/trim))

(def current-version (version))

(defn image-tag []
  (str
   "us-east1-docker.pkg.dev/"
   "clingen-dx/"
   "genegraph-prod/"
   app-name
   ":v"
   (version)))

(defn update-index-html []
  (spit "public/index.html"
        (str/replace (slurp "public/index.html")
                     #"main.*js"
                     (str "main." current-version ".js"))))

(defn revert-index-html []
  (spit "public/index.html"
        (str/replace (slurp "public/index.html")
                     #"main.*js"
                     "main.js")))

(defn build-assets []
  #_(shell "yarn release")
  (shell (str "yarn shadow-cljs release app --config-merge '{:release-version "
              current-version
              "}'")))



(defn docker-push []
  (shell "docker"
         "buildx"
         "build"
         "."
         "--platform"
         "linux/arm64"
         "-t"
         (image-tag)
         "--push"))

;; The Ingress and Cert for this
;; are maintained in genegraph-api

(defn kubernetes-deployment []
  {:apiVersion "apps/v1"
   :kind "Deployment"
   :metadata {:name app-name}
   :spec
   {:selector {:matchLabels {:app app-name}}
    :template
    {:metadata {:labels {:app app-name}}
     :spec
     {:containers
      [{:name app-name
        :image (image-tag)
        :ports [{:name "genegraph-port" :containerPort 80}]
        :resources {:requests {:memory "400Mi" :cpu "50m"}
                    :limits {:memory "600Mi"}}}]
      :tolerations [{:key "kubernetes.io/arch"
                     :operator "Equal"
                     :value "arm64"
                     :effect "NoSchedule"}]
      :affinity {:nodeAffinity {:requiredDuringSchedulingIgnoredDuringExecution
                                {:nodeSelectorTerms
                                 [{:matchExpressions
                                   [{:key "kubernetes.io/arch"
                                     :operator "In"
                                     :values ["arm64"]}]}]}}}}}}})

(defn kubernetes-service []
  {:apiVersion "v1"
   :kind "Service"
   :metadata {:name app-name}
   :spec {:selector {:app app-name}
          :type "NodePort"
          :ports [{:protocol "TCP"
                   :port 80
                   :targetPort 80}]}})

(defn kubectl-apply [input]
  (shell {:in (json/generate-string input)}
         "kubectl"
         "apply"
         "-f"
         "-"))

(update-index-html)
(build-assets)
(docker-push)
(kubectl-apply (kubernetes-deployment))
(kubectl-apply  (kubernetes-service))
(revert-index-html)




