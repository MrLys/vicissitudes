(ns groove.middleware
  (:require [schema-tools.core :as st]
            [environ.core :refer [env]]
            [clojure.tools.logging :as log]
            [groove.models.user :refer :all]
            [groove.auth.token :refer :all]
            [groove.db :refer [update-refresh-token]]
            [groove.bulwark :as blwrk]
            [ring.util.http-response :refer [unauthorized]]
            [ring.util.http-response :refer [created ok not-found]]
	    [ring.middleware.cookies :refer [wrap-cookies]]
            [buddy.hashers :as hashers]
            [buddy.auth :refer [authenticated?]]
            [buddy.auth.accessrules :refer [restrict]]
            [buddy.auth.backends.token :refer [jws-backend]]
            [buddy.auth.backends.httpbasic :refer [http-basic-backend]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]))
(def ^:private max-age-refresh-token (* 60 60 24 31))

(defn get-user-data [identifier]
  (let [user (User :username identifier)]
    (when-not (nil? user)
      {:user-data (-> user
                      (st/dissoc :digest)
                      (st/dissoc :refresh_token))
       :password (:digest user)})))

(defn basic-auth [request, auth-data]
  (let [identifier (:username auth-data)
        password (:password auth-data)
        username (:username auth-data)
        user-info (get-user-data identifier)]
    (if (and user-info (hashers/check password (:password user-info)))
      (:user-data user-info)
      false)))


(defn auth-credentials-reponse [request]
  (let [user          (:identity request)
	refresh-token (str (java.util.UUID/randomUUID))
	_ (blwrk/update-refresh-token  request refresh-token)]
    {:status 200
     :headers {}
     :cookies {"refresh_token" {:value refresh-token :max-age max-age-refresh-token :http-only true}}
     :body {:id (:id user)
            :username (:username user)
	    :token (create-token user)
	    :expiry max-age-refresh-token}}
    ))
    ;(assoc (ok {:id (:id user)
    ;     :username      (:username user)
    ;     :token         (create-token user)
    ;     }) :cookies {"refresh_token" {:value refresh-token :max-age max-age-refresh-token :http-only true}})))

(defn- format-cookies [request]
  (let [token (:token request)
        tokens (re-seq #"[\w-_]+" token)]
       {:payload (str (nth tokens 0) "." (nth tokens 1)) :signature (nth tokens 2) :res token}))

(defn login-authenticate [request]
  (let [data (auth-credentials-reponse request)]
    (assoc (ok data) :cookies {"groove_cookie" {:value (:token data)}})))
;--(assoc (ok (:res data)) :cookies {"payload_cookie" {:value (:payload data)}
;--                          "signature_cookie" {:value (:signature data), :http-only true}})))
(defn- activated? [user]
  (log/info (str "\n\n\n" (:activated user) "\n\n\n"))
  (:activated user))

(defn auth-mw-activated [handler]
  (fn [request]
    (if (authenticated? request)
      (let [user (blwrk/get-user (:id (:identity request)))]
        (if (not (activated? user))
          (unauthorized {:error "Account is not activated"})
          (handler request)))
        (unauthorized {:error "Not authorized"}))))

(defn auth-mw [handler]
  (fn [request]
    (log/info (:cookies request))
    (if (authenticated? request)
      (do
        (log/info (str "Yes, authenticated" handler))
      (let [refresh-token (str (java.util.UUID/randomUUID))
            isValidRefreshToken (blwrk/valid-refresh-token request)
	    _ (blwrk/update-refresh-token request refresh-token)]
          (when isValidRefreshToken
            (handler request))))
      (unauthorized {:error "Not authorized - invalid token"}))))


;; The jws-backend does a lot of magic.
;; it does the unsigning of the token and checks for its validity
;; and expiration. It also expects a Token with the name Token.
;; If the request passes the authentication, the content of the
;; token is put into the request in the :identity field. This
;; is also what is checked by the authenticated? function.

(defn wrap-token-auth [handler]
  (let [backend (jws-backend {:secret (env :secret) :token-name "Bearer" :options {:alg :hs512}})]
    (-> handler
        (auth-mw)
        (wrap-authorization backend)
        (wrap-authentication backend)
	(wrap-cookies))))


(defn wrap-basic-auth [handler]
  (let [backend (http-basic-backend {:authfn basic-auth})]
    (-> handler
        (auth-mw-activated)
        (wrap-authorization backend)
        (wrap-authentication backend))))


