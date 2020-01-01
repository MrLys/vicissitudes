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


(defn new-user! [user]
  (let [digest (hashers/derive (:password user))]
    (db/insert! User (rename-keys (assoc user :password digest) {:password :digest}))))

(defn new-user [user]
  (user->created (new-user! user)))

(defn update-refresh-token [user refresh-token]
  (db/update! User (:id user) :refresh_token refresh-token))

(defn get-registered-user-by-email [field]
  (db/select-one User :email field)) ;; username and email must be unique

(defn get-registered-user-by-username [field]
  (db/select-one User :username field));; username and email must be unique

(defn get-all-users []
  (db/select User))

(defn get-all-grooves-and-habits-by-date-range [user-id start end]
  (db/query {:select [:user_habit.owner_id :user_habit.id :habit.name :groove.date :groove.state]
	     :from [:habit]
	     :where [:= :user_habit.owner_id user-id]
	     :join [:user_habit [:= :habit.id :user_habit.habit_id]]
	     :left-join [:groove [:= :groove.owner_id :user_habit.owner_id]]}))

(defn get-grooves-by-date-range [user_id user-habit-id start end]
  (db/select Groove :owner_id user_id :user_habit_id user-habit-id :date [:>= start] :date [:<= end] {:order-by [:date]}))

(defn get-all-grooves-by-date-range [user_id start end]
  (db/select Groove :owner_id user_id :date [:>= start] :date [:<= end] {:order-by [:date]}))

(defn get-groove-by-user-habit-date [user-id user-habit-id date]
  (db/select Groove  :owner_id user-id :user_habit_id user-habit-id :date date))

(defn groove-id-by-user-habit-date [user-id user-habit-id date]
  (db/select-one-id Groove  :owner_id user-id :user_habit_id user-habit-id :date date))

(defn create-groove [groove]
  (db/insert! Groove groove))

(defn update-groove [grooveId updated field]
  (db/update! Groove grooveId field updated))

(defn get-user-data [identifier]
  (let [user (User :username identifier)]
    (when-not (nil? user)
      {:user-data (-> user
                      (st/dissoc :digest)
                      (st/dissoc :refresh_token))
       :password (:digest user)})))

(defn get-user-id-by-email [email]
  (db/select-one-id User :email email))

(defn get-user [user-id]
  (db/select-one User :id user-id))

(defn create-habit [habit]
  (db/insert! Habit :name habit))

(defn get-habit-by-name [name]
  (db/select-one Habit :name name))

(defn get-all-habits-by-user-id [user-id]
  (db/query {:select [:user_habit.id :habit.name]
             :from [:habit]
             :where [:= :user_habit.owner_id user-id]
             :join [:user_habit [:= :habit.id :user_habit.habit_id]]}))

(defn get-habit-by-id [habit-id]
  (db/select-one Habit habit-id))

(defn create-user-habit [habit ownerId]
  (db/insert! User_habit :owner_id ownerId :habit_id (:id habit)))

(defn get-user-habit
  ([user-id habit-id] (db/select-one User_habit :owner_id user-id :habit_id habit-id))
  ([user-habit-id] (db/select-one User_habit :id user-habit-id)))

(defn get-user-habits [user-id]
  (db/select User_habit :owner_id user-id))


(defn new-activation-token [token user-id date]
  (db/insert! Activation_token :user_id user-id :token token :expiration date))

(defn get-token-by-token [token]
  (db/select-one Activation_token :token token))

(defn get-token-by-user-id [id]
  (db/select-one Activation_token :user_id id))


(defn activate-user [user-id]
  (db/update! User user-id :activated true))
