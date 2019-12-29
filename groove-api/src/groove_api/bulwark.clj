(ns groove-api.bulwark
  (:require [groove-api.db :as db]
            [groove-api.models.groove :refer [Groove]]
            [groove-api.db :refer [get-grooves-by-date-range]]
            [schema-tools.core :as st]
            [ring.util.http-response :refer [ok unauthorized conflict created no-content not-modified not-found]]
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


(defn get-by-dates [request user-id user-habit-id start-date end-date]
  (if (validate-user-permission request user-id)
    (ok (db/get-grooves-by-date-range user-id user-habit-id start-date end-date))
    (unauthorized {:error "Not authorized"})))

(defn get-all-by-dates [request user-id start-date end-date]
  (if (validate-user-permission request user-id)
    (ok (db/get-all-grooves-by-date-range user-id start-date end-date))
    (unauthorized {:error "Not authorized"})))

(defn update-groove [groove request]
  (let [logged-in-user (parseLong (:id (:identity request)))
        user-habit-id (parseLong (:user_habit_id groove))
        owner-id (parseLong (:owner_id groove))
        valid-user (validate-permission logged-in-user owner-id)
        habit (db/get-user-habit user-habit-id)
        groove-id (db/groove-id-by-user-habit-date logged-in-user user-habit-id (:date groove))]
   (if (and valid-user (not (empty? habit)))
     (if (nil? groove-id)
         (created (str "/groove/" ) (db/create-groove (format-groove groove))) ; this should probably use the id as param after groove/
         (do (db/update-groove groove-id (:state groove) :state )
           (ok groove)))
     (unauthorized {:error "Not authorized"}))))

(defn create-habit [habit req] ;; One only gets this far if the user is authenticated
  (if (validate-user-permission req (:owner_id habit))
    (let [db-habit (db/get-habit-by-name (:name habit))]
      (if (empty? db-habit)
        (ok (sanitize (db/create-user-habit (db/create-habit (:name habit)) (:owner_id habit))))
        (if (nil? (db/get-user-habit (:owner_id habit) (:id db-habit)))
          (ok (sanitize (db/create-user-habit db-habit (:owner_id habit))))
          (conflict {:error "You are already tracking that habit"}))))
    (unauthorized {:error "unauthorized"})))

(defn get-habits [request]
  (let [habits (db/get-all-habits-by-user-id (get-user-id request))]
    (if (empty? habits)
      (not-found)
      (ok habits))))

(defn get-habit [habit-id request]
  (let [user-habit (db/get-user-habit (get-user-id request) habit-id)]
    (if (not (nil? user-habit))
      (ok (db/get-habit-by-id habit-id))
      (unauthorized {:error "Unauthorized"}))))

(defn- activation-helper [id]
  (if (db/activate-user id)
    (no-content)
    (not-modified)))

(defn new-activation-token [id date]
  (db/new-activation-token id date))

(defn activate-user [token request]
  (let [token (db/get-token-by-token token)]
    (if valid-token? ;; user can be activated
      (activation-helper (:user_id token))
      (unauthorized {:error "unauthorized"}))))

(defn new-user [user]
  (db/new-user user))
