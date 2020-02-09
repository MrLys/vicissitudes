(ns groove.db
  (:require [toucan.db :as db]
            [clojure.set :refer [rename-keys]]
            [groove.models.user :refer [User]]
            [groove.models.user_habit :refer [User_habit]]
            [groove.models.password_token :refer [Password_token]]
            [groove.models.habit :refer [Habit]]
            [groove.models.groove :refer [Groove]]
            [groove.models.activation_token :refer [Activation_token]]
            [groove.util.validation :refer :all]
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

(defn get-registered-user-by-field [field value]
  (db/select-one User field value)) ;; username and email must be unique


(defn get-all-users []
  (db/select User))

(defn get-all-grooves-and-habits-by-date-range [user-id start end]
  (db/query {:select [:user_habit.owner_id :user_habit.id :habit.name :groove.date :groove.state]
	     :from [:user_habit]
	     :where [:= :user_habit.owner_id user-id]
	     :join [:habit [:= :user_habit.habit_id :habit.id]]
	     :left-join [:groove [:= :user_habit.id :groove.user_habit_id]]
             :order-by [:date]}))
	     ;:join [:groove [:= :groove.owner_id :user_habit.owner_id]]}))

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

(defn update-groove [groove-id updated field]
  (db/update! Groove groove-id field updated))

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

(defn create-user-habit [habit owner-id]
  (db/insert! User_habit :owner_id owner-id :habit_id (:id habit)))

(defn get-user-habit-by-user-id-and-user-habit-id [user-id user-habit-id]
 (db/select-one User_habit :owner_id user-id :id user-habit-id))

(defn get-user-habit
  ([user-id habit-id] (db/select-one User_habit :owner_id user-id :habit_id habit-id))
  ([user-habit-id] (db/select-one User_habit :id user-habit-id)))

(defn get-user-habits [user-id]
  (db/select User_habit :owner_id user-id))


(defn new-activation-token! [token user-id date]
  (db/insert! Activation_token :user_id user-id :token token :expiration date))

(defn new-password-token! [user-id date token]
  (db/insert! Password_token :user_id user-id :token token :expiration date))

(defn get-token-by-token [token]
  (db/select-one Activation_token :token token))

(defn get-token-by-user-id [id]
  (db/select-one Activation_token :user_id id))


(defn activate-user [user-id]
  (db/update! User user-id :activated true))

(defn get-password-token [token]
  (db/select-one Password_token :token token))

(defn invalidate-password-token! [id user-id]
  (db/update-where! Password_token {:id id :user_id user-id} :used true))

(defn update-user! [user-id field value]
  (db/update! User :id user-id field value))
