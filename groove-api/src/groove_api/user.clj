(ns groove-api.user
  (:require [schema.core :as s]
            [clojure.string :as str]
            [clojure.set :refer [rename-keys]]
            [toucan.db :as db]
            [groove-api.models.user :refer [User]]
            [groove-api.util.validation :refer :all]
            [compojure.api.sweet :refer [GET POST]]
            [buddy.hashers :as hashers]
            [ring.util.http-response :refer [ok not-found conflict created]]))


(defn id->created [id]
  (created (str "/users/" id) {:id id}))

(defn build-user-from-db [user]
  {:username (:value (:username user)) :email (:value (:email user)) :id (:id user)})

(defn user->created [user]
  (created (str "/user/" (:id user)) (build-user-from-db user)))


(s/defschema UserRequestSchema
  {:username (s/constrained s/Str valid-username?)
   :email (s/constrained s/Str valid-email?)
   :password (s/constrained s/Str valid-password?)})

(defn create-user-handler [create-user-req]
  (id->created (:id (db/insert! User create-user-req))))

(defn user->response [user]
  (if user
    (ok user)
    (not-found)))

(defn get-user-handler [id]
  (-> (User id)
      user->response))

(defn get-users-handler []
  (->> (db/select User)
       ok))
(defn create-new-user [user]
  (let [digest (hashers/derive (:password user))]
  (user->created (db/insert! User (rename-keys (assoc user :password digest) {:password :digest})))))


(defn get-registered-user-by-email [field]
  (db/select User :email field))

(defn get-registered-user-by-username [field]
  (db/select User :username field))

(defn create-user-handler2 [user]
  (let [username-query (get-registered-user-by-username (:username user))
        email-query (get-registered-user-by-email (:email user))
        email-exists? (not-empty email-query)
        username-exists? (not-empty username-query)]
    (cond
     (and username-exists? email-exists?) (conflict {:error "Username and Email already exist"})
     username-exists? (conflict {:error "Username already exist"})
     email-exists? (conflict {:error "Email already exist"})                                   
     :else (create-new-user user))))


(def user-routes
  [(POST "/users" []
         :tags ["User"]
         :body [create-user-req UserRequestSchema]
         (create-user-handler create-user-req))
    (POST "/user" []
         :body [user UserRequestSchema]
         (create-user-handler2 user))
   (GET "/users/:id" []
        :path-params [id :- s/Int]
        (get-user-handler id))
   (GET "/users" []
        (get-users-handler))])
