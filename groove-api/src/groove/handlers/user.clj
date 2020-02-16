(ns groove.handlers.user
  (:require [clojure.string :as str]
            [ring.util.http-response :refer [ok not-found forbidden created]]
            [groove.db :as db]
            [groove.handlers.response :refer [response-handler]]
            [groove.bulwark :as blwrk]
            [groove.models.user :refer [User]]
            [groove.mail :refer [mail]]
            [groove.util.utils :refer [create-activation-token truthy? build-grooves-by-habits]]
            [environ.core :refer [env]]
            [groove.util.validation :refer :all]))


(defn create-new-user [user]
  (let [user (assoc user :email (.toLowerCase (:email user)) :username (.toLowerCase (:username user)))
        activation-token (create-activation-token)
        date (.plusDays (java.time.LocalDate/now) 1)
        db-user (blwrk/new-user user)]
    (blwrk/new-activation-token! activation-token (:id db-user) date)
    (when false;(not (truthy? (:istest env)))
      (do
        (println (str "sending mail to " (:email user)))
        (future (mail :to (:email user)
              :from "noreply@rutta.no"
              :user (:mailuser env)
              :password (:mailpassword env)
              :mailport (:mailport env)
              :mailhost (:mailhost env)
              :ssl true
              :port (:mailport env)
              :subject "Welcome to Rutta!"
              :text (str "<h1>Welcome to Rutta!</h1>" "Please follow this link for activation<br/>" "<a href=\"http://localhost:8080/activation?token=" activation-token"\">Click here</a>")))))
    db-user))

(defn create-user [user]
  (let [username-query (blwrk/get-user-by-field :username (:username user))
        email-query (blwrk/get-user-by-field :email (:email user))
        email-exists? (not-empty email-query)
        username-exists? (not-empty username-query)]
    (cond
     (and username-exists? email-exists?) {:error "Username and Email already exist"}
     username-exists? {:error "Username already exist"}
     email-exists? {:error "Email already exist"}
     :else (create-new-user user))))

(defn create-user-handler [user]
  (response-handler :POST create-user user))


(defn activate-user-handler [token]
  (response-handler :POST blwrk/activate-user token))

(defn update-user-password [request]
  (let [db-token (db/get-password-token (:token request))]
    (if (nil? db-token)
      {:error "Invalid token"}
      (if (valid-token? db-token)
        (do
          (blwrk/invalidate-password-token! (:token_id db-token)(:user_id db-token))
          (blwrk/update-user-password! {:user_id (:user_id db-token) :password (:password request)}))
      {:error "Invalid token"}))))

(defn update-user-handler [request]
  (response-handler :POST update-user-password request))

(defn forgot-password-handler [email]
  (println (str "\n\n\n" "email: " email))
  (if (valid-email? email)
    (let [user (blwrk/get-user-by-field :email (.toLowerCase email))]
      (if (nil? user)
        {:error "Email not found"}
        (let [token (blwrk/new-password-token! (:id (:user-data  user)) (.plusDays (java.time.LocalDate/now) 1) (create-activation-token))]
          (when false;(not (truthy? (:istest env)))
            (do
              (println (str "sending mail to " (:email user)))
              (future (mail :to (:email user)
                            :from "noreply@rutta.no"
                            :user (:mailuser env)
                            :password (:mailpassword env)
                            :mailport (:mailport env)
                            :mailhost (:mailhost env)
                            :ssl true
                            :port (:mailport env)
                            :subject "Password reset" :text (str "<h1>A request to reset the password for the account has been made.</h1>\n" "<p>The link below is valid for 24 hours.</p>\n""Please follow this link to create a new password \n" "<a href=\"http://localhost:8080/Password?token=" token"\">Click here</a>")))))
          token)))
    {:error "Invalid email"}))

(defn forgot-password [email]
  (response-handler :POST forgot-password-handler email))

(defn get-all-data [request]
  (let [user (blwrk/get-user-by-field :id (:id (:identity request)))
        habits (build-grooves-by-habits (blwrk/get-all-grooves-and-habits-by-date-range request))
        teams (blwrk/get-all-teams request)]
    (println user)
    (if (or (nil? user) (empty? user))
      {:error "Cannot find user"}
      {:user 
       {:id (:id (:user-data user))
        :username (:username (:user-data user))
        :email (:email (:user-data user))
        :habits habits
        :teams teams}})))

(defn get-all-data-handler [request]
  (response-handler :GET get-all-data request))





