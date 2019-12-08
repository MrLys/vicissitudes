(ns groove-api.db
  (:require [toucan.db :as db]
            [clojure.set :refer [rename-keys]]
            [groove-api.models.user :refer [User]]
            [groove-api.models.groove :refer [Groove]]
            [groove-api.util.validation :refer :all]
            [buddy.hashers :as hashers]
            [schema-tools.core :as st]
            [ring.util.http-response :refer [ok not-found conflict created]]))


(defn user->created [user]
  (created (str "/user/" (:id user)) user))

(defn id->created [id]
  (created (str "/users/" id) {:id id}))

(defn new-user [user]
  (let [digest (hashers/derive (:password user))]
    (user->created (db/insert! User (rename-keys (assoc user :password digest) {:password :digest})))))

(defn update-refresh-token [user refresh-token] 
  (db/update! User (:id user) :refresh_token refresh-token))
(defn get-registered-user-by-email [field]
  (db/select User :email field))

(defn get-registered-user-by-username [field]
  (db/select User :username field))

(defn get-all-users []
  (db/select User))

(defn get-grooves-by-date-range [user_id habit start end]
  (db/select Groove :owner_id user_id :habit_id habit :date [:> start] :date [:< end]))

(defn get-user-data [identifier]
  (let [user (User :username identifier)]
    (when-not (nil? user)
      {:user-data (-> user
                      (st/dissoc :digest)
                      (st/dissoc :refresh_token))
       :password (:digest user)})))
