(ns groove-api.db
  (:require [toucan.db :as db]
            [clojure.set :refer [rename-keys]]
            [groove-api.models.user :refer [User]]
            [groove-api.models.user_habit :refer [User_habit]]
            [groove-api.models.habit :refer [Habit]]
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
  (println (str "\n\n\n\n\n" user "\n\n\n\n"))
  (let [digest (hashers/derive (:password user))]
    (println digest)
    (println (assoc user :password digest))
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

(defn get-groove-by-user-habit-date [userId habitId date]
  (db/select Groove  :owner_id userId :habit_id habitId :date date))

(defn create-groove [groove]
  (db/insert! Groove groove))

(defn update-groove [grooveId updated]
  (db/update! Groove grooveId updated))

(defn get-user-data [identifier]
  (let [user (User :username identifier)]
    (when-not (nil? user)
      {:user-data (-> user
                      (st/dissoc :digest)
                      (st/dissoc :refresh_token))
       :password (:digest user)})))

(defn get-user-habit [userId habitId]
  (db/select User_habit :owner_id userId :habit_id habitId))

(defn create-habit [habit]
  (let [hbt (db/insert! Habit :name habit)]
    (println hbt)
    hbt))

(defn get-habit-by-name [name]
  (db/select Habit :name name)) 

(defn create-user-habit [habit ownerId]
  (db/insert! User_habit :owner_id ownerId :habit_id (:id habit)))
