(ns groove-api.handlers.user
  (:require [clojure.string :as str]
            [ring.util.http-response :refer [ok not-found forbidden created]]
            [groove-api.db :as db]
            [groove-api.bulwark :refer [activate-user]]
            [groove-api.models.user :refer [User]]
            [groove-api.mail-test :refer [mail]]
            [groove-api.util.validation :refer :all]))


(defn create-new-user [user]
  (let [user (assoc user :email (.toLowerCase (:email user)))
        activation_token (java.util.UUID/randomUUID)
        date (.plusDays (java.time.LocalDate/now) 1)
        dbUser (db/new-user user)
        userId (db/get-user-id-by-email (.toLowerCase (:email user)))]
    (db/new-activation-token activation_token userId date)
    (mail :to (:email user) :subject "Welcome to rutta!" :text (str "<h1>Welcome to rutta!</h1>\n" "Please follow this link for activation\n" "<a href=\"http://localhost:8080/activation?token=" activation_token"\">Click here</a>"))
    (ok dbUser)))

(defn user->response [user]
  (if user
    (ok user)
    (not-found)))

(defn get-user-handler [id]
  (-> (User id)
      user->response))

(defn get-users-handler []
  (->> (db/get-all-users)
       ok))


(defn create-user-handler2 [user]
  (let [username-query (db/get-registered-user-by-username (:username user))
        email-query (db/get-registered-user-by-email (:email user))
        email-exists? (not-empty email-query)
        username-exists? (not-empty username-query)]
    (cond
     (and username-exists? email-exists?) (forbidden {:error "Username and Email already exist"})
     username-exists? (forbidden {:error "Username already exist"})
     email-exists? (forbidden {:error "Email already exist"})
     :else (create-new-user user))))

(defn activate-user-handler [token request]
  (activate-user token request))
