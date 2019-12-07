(ns groove-api.handlers.groove
  (:require [groove-api.util.validation :refer :all]
            [groove-api.middleware.token-auth :refer :all]
            [groove-api.util.utils :refer :all]
            [groove-api.auth.token :refer [create-token]]
            [groove-api.db :refer :all]
            [groove-api.middleware :refer [auth-mw]]
            [toucan.db :as db]
            [ring.util.http-response :refer [created ok not-found]]
            [buddy.auth.middleware :refer [wrap-authentication]]
            [buddy.auth :refer [authenticated? throw-unauthorized]]
            [groove-api.access :as access]
            [schema.core :as s]
            [buddy.auth.backends.token :refer [jws-backend]]
            [environ.core :refer [env]]
            [buddy.sign.jwt :as jwt]
            [compojure.api.meta :refer [restructure-param]]
            [ring.middleware.session :refer [wrap-session]]
            [buddy.auth.middleware :refer [wrap-authentication wrap-authorization]]
	    [buddy.core.codecs :refer :all]
            [buddy.auth.accessrules :refer [wrap-access-rules]]
            [buddy.auth.backends.httpbasic :refer [http-basic-backend]]
            [ring.util.http-response :refer [created unauthorized]]
            [groove-api.models.groove :refer [Groove]]
            [groove-api.models.user :refer [User]]
            [buddy.hashers :as hashers]
            [ring.util.http-response :refer [created ok not-found]]))

(defn create-groove-handler [req]
  (id->created (db/insert! Groove (format-groove req))))

(defn get-grooves-handler []
  (do
    (println "i am inside the handler")
  (ok (db/select Groove))))

(defn update-groove-handler [id req]
   (if (db/update! Groove req)
     (ok)
     (create-groove-handler req)))

(defn groove->response [day]
  (if day
    (ok day)
    (not-found)))
(defn authenticated [req]
  (authenticated? req))

(defn admin [req]
  (and (authenticated? req)
       (#{:admin} (:role (:identity req)))))



;-(defn- wrap-session-auth [handler]
;-  (let [backend (session-backend)]
;-  (-> handler
;-      (wrap-authentication backend)
;-      (wrap-session {:cookie-name cookie-name})
;-      (wrap-authorization backend))))



;--(defn wrap-rule [handler rule]
;--  (-> handler
;--      (wrap-access-rules {:rules [{:pattern #".*"
;--                                   :handler rule}]
;--                          :on-error access-error})))

;--(defmethod restructure-param :auth-rules
;--  [_ rule acc]
;--  (update-in acc [:middlewares] conj [wrap-rule rule]))
;--
;--(defmethod restructure-param :current-user
;--  [_ binding acc]
;--  (update-in acc [:letks] into [binding `(:identity ~'+compojure-api-request+)]))

;--(defn token-auth-mw [handler]
;--  (fn [request]
;--    (if (authenticated? request)
;--      (handler request)
;--      (unauthorized {:error "Not authorized"}))))
(defn get-user-info [identifier]
  (let [data (db/select User :username identifier)]
    (do
      (println (str (:username data) " " (:id data)))
      data)))


(defn basic-auth [request, auth-data]
  (let [identifier	(:username auth-data)
        password	(:password auth-data)
        username 	(:username auth-data)
        user-info  	(get-user-data identifier)]
    (if (and user-info (hashers/check password (:password user-info)))
      (:user-data user-info)
      false)))

(defn tt [user refresh-token]
  (do
    (println (str "this is the user name " (:username (:identity user)) " and id: " (:id (:identity user))))
    (println (str "this is the :refresh_token " refresh-token))
    (db/update! User 12 :refresh_token refresh-token)))

(defn auth-credentials-reponse [request]
  (println request)
  (let [user          (:identity request)
        refresh-token (str (java.util.UUID/randomUUID))
        _ (tt request refresh-token)]
    (ok {:id		(:id user)
         :username      (:username user)
         :token         (create-token user)
         :refreshToken  refresh-token})))

;; The jws-backend does a lot of magic.
;; it does the unsigning of the token and checks for its validity
;; and expiration. It also expects a Token with the name Token.
;; If the request passes the authentication, the content of the
;; token is put into the request in the :identity field. This
;; is also what is checked by the authenticated? function.


;--(defn wrap-restricted [handler rule]
;--  (restrict handler {:handler  rule
;--                     :on-error access-error}))
;--
;(-> (GET "/login" [:as request]
;	    :header-params [authorization :- String]
;	    :middleware [wrap-token-auth]
;	    (auth-credentials-reponse request)))

;-(defn- wrap-session-auth [handler]
;-  (let [backend (session-backend)]
;-  (-> handler
;-      (wrap-authentication backend)
;-      (wrap-session {:cookie-name cookie-name})
;-      (wrap-authorization backend))))


;--(defn access-error [req val]
;--  (unauthorized val))
;--
;--(defn wrap-rule [handler rule]
;--  (-> handler
;--      (wrap-access-rules {:rules [{:pattern #".*"
;--                                   :handler rule}]
;--                          :on-error access-error})))
;--
;--;--(defmethod restructure-param :auth-rules
;--;--  [_ rule acc]
;--;--  (update-in acc [:middlewares] conj [wrap-rule rule]))
;--;--
;--;--(defmethod restructure-param :current-user
;--;--  [_ binding acc]
;--;--  (update-in acc [:letks] into [binding `(:identity ~'+compojure-api-request+)]))
;--
;--;--(defn token-auth-mw [handler]
;--;--  (fn [request]
;--;--    (if (authenticated? request)
;--;--      (handler request)
;--;--      (unauthorized {:error "Not authorized"}))))
;--(defn get-user-info [identifier]
;--  (let [data (db/select User :username identifier)]
;--    (do
;--      (println (str (:username data) " " (:id data)))
;--      data)))
;--
;--(defn get-user-data [identifier]
;--  (let [user (User :username identifier)]
;--    (when-not (nil? user)
;--      {:user-data (-> user
;--                      (st/dissoc :digest)
;--                      (st/dissoc :refresh_token))
;--       :password (:digest user)})))
;--
;--(defn basic-auth [request, auth-data]
;--  (let [identifier	(:username auth-data)
;--        password	(:password auth-data)
;--        username 	(:username auth-data)
;--        user-info  	(get-user-data identifier)]
;--    (if (and user-info (hashers/check password (:password user-info)))
;--      (:user-data user-info)
;--      false)))
;--
;--(defn tt [user refresh-token]
;--  (do
;--    (println (str "this is the user name " (:username (:identity user)) " and id: " (:id (:identity user))))
;--    (println (str "this is the :refresh_token " refresh-token))
;--    (db/update! User 12 :refresh_token refresh-token)))
;--
;--(defn auth-credentials-reponse [request]
;--  (println request)
;--  (let [user          (:identity request)
;--        refresh-token (str (java.util.UUID/randomUUID))
;--        _ (tt request refresh-token)]
;--    (ok {:id		(:id user)
;--         :username      (:username user)
;--         :token         (create-token user)
;--         :refreshToken  refresh-token})))
;--
;--;; The jws-backend does a lot of magic.
;--;; it does the unsigning of the token and checks for its validity
;--;; and expiration. It also expects a Token with the name Token.
;--;; If the request passes the authentication, the content of the
;--;; token is put into the request in the :identity field. This
;--;; is also what is checked by the authenticated? function.
;--(def my-backend (jws-backend {:secret (env :secret) :options {:alg :hs512}}))
;--(def my-basic-backend (http-basic-backend {:authfn basic-auth}))
;--(defn wrap-token-auth [handler]
;--  (let [backend (jws-backend {:secret (env :secret) :options {:alg :hs512}})]
;--    (-> handler
;--        (auth-mw)
;--        (wrap-authorization backend)
;--        (wrap-authentication backend))))
;--
;--(defn wrap-basic-auth [handler]
;--  (let [backend (http-basic-backend {:authfn basic-auth})]
;--    (-> handler
;--        (auth-mw)
;--        (wrap-authorization backend)
;--        (wrap-authentication backend))))
;--
;--
;--
;--;(-> (GET "/login" [:as request]
;--;	    :header-params [authorization :- String]
;--;	    :middleware [wrap-token-auth]
;--;	    (auth-credentials-reponse request)))
