(ns groove-api.handlers.user
  (:require [clojure.string :as str]
            [ring.util.http-response :refer [ok not-found conflict created]]
            [groove-api.db :refer :all]
            [groove-api.db :refer [new-user]]
            [groove-api.models.user :refer [User]]
            [groove-api.util.validation :refer :all]))


(defn create-new-user [user]
  (ok (new-user [user])))
  
(defn user->response [user]
  (if user
    (ok user)
    (not-found)))

(defn get-user-handler [id]
  (-> (User id)
      user->response))

(defn get-users-handler []
  (->> (get-all-users)
       ok))


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

