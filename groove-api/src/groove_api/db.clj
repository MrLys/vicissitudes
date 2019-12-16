(ns groove-api.db
  (:require [toucan.db :as db]
            [clojure.set :refer [rename-keys]]
            [groove-api.models.user :refer [User]]
            [groove-api.models.user_habit :refer [User_habit]]
            [groove-api.models.habit :refer [Habit]]
            [groove-api.models.groove :refer [Groove]]
            [groove-api.models.activation_token :refer [Activation_token]]
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

(defn get-all-grooves-by-date-range [user_id start end]
  (db/select Groove :owner_id user_id :date [:>= start] :date [:<= end]))

(defn get-groove-by-user-habit-date [userId habitId date]
  (db/select Groove  :owner_id userId :habit_id habitId :date date))

(defn groove-id-by-user-habit-date [userId habitId date] 
  (db/select-one-id Groove  :owner_id userId :habit_id habitId :date date))

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

(defn get-user-id-by-email [email]
  (db/select-one-id User :email email))

(defn get-user [userId]
  (db/select-one User :id userId))

(defn create-habit [habit]
  (let [hbt (db/insert! Habit :name habit)]
    (println hbt)
    hbt))

(defn get-habit-by-name [name]
  (db/select Habit :name name)) 

(defn get-all-habits-by-userId [userId]
  (println (str "getting all habits for " userId))
  (db/debug-print-queries (db/query {:select [:habit.id :habit.name] 
             :from [:habit]
             :where [:= :user_habit.owner_id userId]
             :join [:user_habit [:= :habit.id :user_habit.habit_id]]}))
  (db/query {:select [:habit.id :habit.name] 
             :from [:habit]
             :where [:= :user_habit.owner_id userId]
             :join [:user_habit [:= :habit.id :user_habit.habit_id]]}))

(defn get-habit-by-id [habitId]
  (db/select-one Habit habitId))

(defn create-user-habit [habit ownerId]
  (db/insert! User_habit :owner_id ownerId :habit_id (:id habit)))

(defn get-user-habit [userId habitId]
  (db/select-one User_habit :owner_id userId :habit_id habitId))

(defn get-user-habits [userId]
  (db/select User_habit :owner_id userId))


(defn new-activation-token [token userId date]
  (db/insert! Activation_token :user_id userId :token token :expiration date))

(defn get-token-by-token [token]
  (db/select-one Activation_token :token token))

(defn activate-user [userId]
  (db/update! User userId :activated true))
