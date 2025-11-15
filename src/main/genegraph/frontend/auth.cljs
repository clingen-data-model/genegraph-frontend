(ns genegraph.frontend.auth
  (:require [re-frame.core :as rf]
            [reitit.frontend.easy :as rfe]
            [genegraph.frontend.icon :as icon]
            [re-graph.core :as re-graph]
            ["firebase/app" :as firebase]
            ["firebase/auth" :as auth]))


(defn login-dropdown []
  [:div
   {:class "inline-block relative"}
   [:button
    {:class
     "inline-flex w-full justify-center gap-x-1.5 rounded-md bg-white px-3 py-2 text-sm font-semibold text-gray-900 shadow-xs inset-ring-1 inset-ring-gray-300 hover:bg-gray-50 dark:bg-white/10 dark:text-white dark:shadow-none dark:inset-ring-white/5 dark:hover:bg-white/20"
     :on-click #(rf/dispatch [::show-auth])}
    "Sign in"
    icon/arrow]
   (when @(rf/subscribe [::show-auth?])
     [:div
      {:class
       "absolute right-0 z-10 mt-2 w-56 origin-top-right rounded-md bg-white shadow-lg ring-1 ring-gray-300 ring-opacity-5 focus:outline-none"}
      [:div
       {:class "py-1"}
       [:a
        {:on-click #(rf/dispatch [::sign-in-microsoft])
         :class
         "block px-4 py-2 text-sm text-gray-700 focus:bg-gray-100 focus:text-gray-900 focus:outline-hidden dark:text-gray-300 dark:focus:bg-white/5 dark:focus:text-white"}
        [:img
         {:class "h-10 w-auto"
          :src "img/ms.svg"
          :alt "Sign in with Microsoft"}]]
       [:a
        {:on-click #(rf/dispatch [::sign-in-google])
         :class
         "block px-4 py-2 text-sm text-gray-700 focus:bg-gray-100 focus:text-gray-900 focus:outline-hidden dark:text-gray-300 dark:focus:bg-white/5 dark:focus:text-white"}
        [:img
         {:class "h-10 w-auto"
          :src "img/google.svg"
          :alt "Sign in with Google"}]]]])])

(defn login-header-div []
  (let [user @(rf/subscribe [::user])]
    [:div
     {:class "flex flex-1 justify-end"}
     (if user
       [:div
        {:class "flex gap-2"}
        [:div
         {:class "text-sm/6 font-semibold text-gray-900 dark:text-white"}
         (.-displayName user)]
        [:a
         {:on-click #(rf/dispatch [::sign-out])
          :class "text-sm/6 font-semibold text-gray-900 dark:text-white"}
         "Log out"
         [:span {:aria-hidden "true"} "→"]]]
       (login-dropdown)
       #_[:a
          {:on-click #(rf/dispatch [::show-auth])
           :class "text-sm/6 font-semibold text-gray-900 dark:text-white"}
          "Log in"
          [:span {:aria-hidden "true"} "→"]])]))


;; Providers
(def google-provider (auth/GoogleAuthProvider.))
(def microsoft-provider (auth/OAuthProvider. "microsoft.com"))

;; ## Re-frame Events
;; ```clojure
;; (ns myapp.events
;;   (:require [re-frame.core :as rf]
;;             ["firebase/auth" :as auth]
;;             [myapp.firebase :as firebase]))

;; Initialize auth state listener
(comment
  (-> (get-in @re-frame.db/app-db [:user])
      .getIdToken
      (.then #(println %)))
  )

#_(-> (auth/getAuth)
    .-currentUser
    .getIdToken
    js/Promise.resolve
    (.then #(js/console.log %)))

#_(-> (auth/getAuth)
    .-currentUser
#_    .getIdToken
#_    js/Promise.resolve
#_    (.then #(js/console.log %)))

(rf/reg-event-fx
 ::init-auth
 (fn [_ _]
   (auth/onAuthStateChanged
    (auth/getAuth)
    (fn [user]
      (when user
        (-> user
            .getIdToken
            js/Promise.resolve
            (.then #(rf/dispatch [:update-user-token %]))))
      (rf/dispatch
       [::set-user user])))
   {}))



(rf/reg-event-db
 ::show-auth
 (fn [db _]
   (js/console.log "show auth " (::show-auth db))
   (assoc db ::show-auth (not (::show-auth db)))))

(rf/reg-sub
 ::show-auth?
 :-> ::show-auth)

;; Set user in db
(rf/reg-event-db
 ::set-user
 (fn [db [_ user]]
   (assoc db :user user :loading? false)))

;; Set loading state
(rf/reg-event-db
 ::set-loading
 (fn [db [_ loading?]]
   (assoc db :loading? loading?)))

;; Google sign in
(rf/reg-event-fx
 ::sign-in-google
 (fn [_ _]
   {:fx [[:dispatch [::set-loading true]]
         [:auth/sign-in (auth/GoogleAuthProvider.)]]}))

;; Microsoft sign in
(rf/reg-event-fx
 ::sign-in-microsoft
 (fn [_ _]
   {:fx [[:dispatch [::set-loading true]]
         [:auth/sign-in (auth/OAuthProvider. 'microsoft.com')]]}))

;; Sign out
(rf/reg-event-fx
 ::sign-out
 (fn [_ _]
   {:fx [[:auth/sign-out]]}))

;; Auth effects
(rf/reg-fx
 :auth/sign-in
 (fn [provider]
   (-> (auth/signInWithPopup (auth/getAuth) #_firebase/firebase-auth
                             provider)
       (.then #(rf/dispatch [::set-loading false]))
       (.catch #(do (rf/dispatch [::set-loading false])
                    (js/console.error "Auth error:" %))))))

(rf/reg-fx
 :auth/sign-out
 (fn []
   (auth/signOut (auth/getAuth))))

(rf/reg-sub
 ::user
 (fn [db _]
   (:user db)))

(rf/reg-sub
 ::loading?
 (fn [db _]
   (:loading? db)))

(rf/reg-sub
 ::authenticated?
 (fn [_]
   (rf/subscribe [::user]))
 (fn [user _]
   (some? user)))


(defn auth-button [provider-name icon-class on-click disabled?]
  [:button
   {:class (str "w-full flex items-center justify-center px-4 py-3 "
                "border border-gray-300 rounded-lg shadow-sm "
                "bg-white text-gray-700 hover:bg-gray-50 "
                "disabled:opacity-50 disabled:cursor-not-allowed "
                "transition-colors duration-200")
    :disabled disabled?
    :on-click on-click}
   [:div {:class "flex items-center space-x-3"}
    [:i {:class icon-class}]
    [:span {:class "font-medium"}
     (str "Sign in with " provider-name)]]])

(defn loading-spinner []
  [:div {:class "animate-spin rounded-full h-6 w-6 border-b-2 border-blue-600"}])

(defn auth-header [user]
  [:div {:class "text-center"}
   [:h2 {:class "mt-6 text-3xl font-extrabold text-gray-900"}
    (if user "Welcome!" "Sign in to your account")]
   (when-not user
     [:p {:class "mt-2 text-sm text-gray-600"}
      "Choose your preferred sign-in method"])])

(defn authenticated-div [user]
  [:div {:class "bg-white p-8 rounded-lg shadow-md"}
   [:div {:class "text-center space-y-4"}
    [:div {:class "w-16 h-16 bg-green-100 rounded-full flex items-center justify-center mx-auto"}
     [:i {:class "fas fa-check text-green-600 text-2xl"}]]
    [:h3 {:class "text-lg font-medium text-gray-900"}
     (str "Hello, " (-> user :displayName))]
    [:p {:class "text-sm text-gray-500"}
     (-> user :email)]
    [:button
     {:class (str "w-full flex justify-center py-2 px-4 "
                  "border border-transparent rounded-md shadow-sm "
                  "text-sm font-medium text-white bg-red-600 "
                  "hover:bg-red-700 focus:outline-none focus:ring-2 "
                  "focus:ring-offset-2 focus:ring-red-500")
      :on-click #(rf/dispatch [::sign-out])}
     "Sign Out"]]])

(defn unauthenticated-div [loading?]
  [:div {:class "bg-white p-8 rounded-lg shadow-md"}
   [:div {:class "space-y-4"}
    (if loading?
      ;; Loading state
      [:div {:class "text-center py-8"}
       [loading-spinner]
       [:p {:class "mt-2 text-sm text-gray-600"} "Signing you in..."]]
      
      ;; Auth buttons
      [:div {:class "space-y-3"}
       [auth-button
        "Google"
        "fab fa-google text-red-500"
        #(rf/dispatch [::sign-in-google])
        loading?]
       
       [auth-button
        "Microsoft"
        "fab fa-microsoft text-blue-500"
        #(rf/dispatch [::sign-in-microsoft])
        loading?]])]])

(defn auth-component []
  (let [user @(rf/subscribe [::user])
        loading? @(rf/subscribe [::loading?])]
    [:div {:class "min-h-screen flex items-center justify-center bg-gray-50 py-12 px-4 sm:px-6 lg:px-8"}
     (auth-header user)
     [:div  {:class "max-w-md w-full space-y-8"}
      (if user
        (authenticated-div user)
        (unauthenticated-div loading?))]]))

(rf/reg-sub
 ::current-user
 (fn [db _]
   (if (= "prod" genegraph.frontend.ENV)
     nil
     {:id 0
      :name "test"})))


