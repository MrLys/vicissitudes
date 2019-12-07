(ns groove-api.middleware
  (:require [schema-tools.core :as st]
            [environ.core :refer [env]]
            [groove-api.models.user :refer :all]
            [groove-api.auth.token :refer :all]
            [groove-api.db :refer [update-refresh-token]]
            [ring.util.http-response :refer [unauthorized]]
            [ring.util.http-response :refer [created ok not-found]]
            [buddy.hashers :as hashers]
            [buddy.auth :refer [authenticated?]]
            [buddy.auth.accessrules :refer [restrict]]
            [buddy.auth.backends.token :refer [jws-backend]]
            [buddy.auth.backends.httpbasic :refer [http-basic-backend]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]))

(defn access-error [req val] (unauthorized val))

(defn get-user-data [identifier]
  (let [user (User :username identifier)]
    (when-not (nil? user)
      {:user-data (-> user 
			(st/dissoc :digest)
			(st/dissoc :refresh_token))
       :password (:digest user)})))

(defn basic-auth [request, auth-data]
  (let [identifier	(:username auth-data)
	password	(:password auth-data)
	username 	(:username auth-data)
	user-info  	(get-user-data identifier)]
      (if (and user-info (hashers/check password (:password user-info)))
	(:user-data user-info)
	false)))


(defn auth-credentials-reponse [request]
	(println request)
  (let [user          (:identity request)
        refresh-token (str (java.util.UUID/randomUUID))
        _ (update-refresh-token request refresh-token)]
    (ok {:id		(:id user)
	 :username      (:username user)
	 :token         (create-token user)
	 :refreshToken  refresh-token})))

(defn auth-mw [handler]
  (fn [request]
    (if (authenticated? request)
      (handler request)
      (unauthorized {:error "Not authorized"}))))
;; The jws-backend does a lot of magic.
;; it does the unsigning of the token and checks for its validity
;; and expiration. It also expects a Token with the name Token.
;; If the request passes the authentication, the content of the
;; token is put into the request in the :identity field. This
;; is also what is checked by the authenticated? function.
(def my-backend (jws-backend {:secret (env :secret) :options {:alg :hs512}}))
(def my-basic-backend (http-basic-backend {:authfn basic-auth}))
(defn wrap-token-auth [handler]
	(let [backend (jws-backend {:secret (env :secret) :options {:alg :hs512}})]
	(-> handler
		(auth-mw)
		(wrap-authorization backend)
		(wrap-authentication backend))))


(defn wrap-basic-auth [handler]
	(let [backend (http-basic-backend {:authfn basic-auth})]
	(-> handler
		(auth-mw)
		(wrap-authorization backend)
		(wrap-authentication backend))))


(defn wrap-restricted [handler rule]
  (restrict handler {:handler  rule
		     :on-error access-error}))
