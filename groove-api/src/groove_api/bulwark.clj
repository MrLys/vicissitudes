(ns groove-api.bulwark
  (:require [groove-api.db :as db]
            [groove-api.models.groove :refer [Groove]]
            [groove-api.db :refer [get-grooves-by-date-range]]
            [schema-tools.core :as st]
            [groove-api.util.validation :refer [valid-token?]]
            [groove-api.util.utils :refer [format-groove parseLong sanitize]]))

(defn- get-user-id [request]
  (parseLong (:id (:identity request))))

(defn get-user [user-id]
  (db/get-user user-id))

(defn- validate-permission [user id]
  (println (str "Userid: " user " accessing data for user " id))
  (= (parseLong user) (parseLong id)))

(defn- validate-user-permission [req id]
    (validate-permission (:id (:identity req)) id))


(defn get-by-dates [request user-habit-id start-date end-date]
    (db/get-grooves-by-date-range (get-user-id request) user-habit-id start-date end-date))

(defn get-all-by-dates [request start-date end-date]
    (db/get-all-grooves-by-date-range (get-user-id request) start-date end-date))

(defn update-groove [groove request]
  (let [logged-in-user (get-user-id request)
        user-habit-id (parseLong (:user_habit_id groove))
        owner-id (parseLong (:owner_id groove))
        valid-user? (validate-permission logged-in-user owner-id)
        habit (db/get-user-habit user-habit-id)
        groove-id (db/groove-id-by-user-habit-date logged-in-user user-habit-id (:date groove))]
   (if (and valid-user? (not (empty? habit)))
     (if (nil? groove-id)
         (db/create-groove (format-groove groove)) ; this should probably use the id as param after groove/
         (do (db/update-groove groove-id (:state groove) :state )
           groove))
     {:error "Not authorized"})))

(defn create-habit [habit req] ;; One only gets this far if the user is authenticated
  (if (validate-user-permission req (:owner_id habit))
    (let [db-habit (db/get-habit-by-name (:name habit))]
      (if (empty? db-habit)
        (sanitize (db/create-user-habit (db/create-habit (:name habit)) (:owner_id habit)))
        (if (nil? (db/get-user-habit (:owner_id habit) (:id db-habit)))
          (sanitize (db/create-user-habit db-habit (:owner_id habit)))
          {:error "You are already tracking that habit"})))
    {:error "Not authorized"}))

(defn get-habits [request]
  (db/get-all-habits-by-user-id (get-user-id request)))

(defn get-habit [habit-id request]
  (db/get-user-habit (get-user-id request) habit-id))

(defn- activation-helper [id]
  (if (db/activate-user id)
    "Activated"
    {:error "Cannot activate user"}))

(defn new-activation-token [token id date]
  (println id)
  (db/new-activation-token token id date))

(defn activate-user [token request]
  (let [token (db/get-token-by-token token)]
    (if (valid-token? token) ;; user can be activated
      (activation-helper (:user_id token))
      {:error "Not authorized"})))

(defn new-user [user]
  (db/new-user! user))

; Using user-id ensures the requester only receives data connected to the user
(defn get-all-grooves-and-habits-by-date-range [req start end]
  (db/get-all-grooves-and-habits-by-date-range (get-user-id req) start end))

