(ns groove-api.bulwark
  (:require [groove-api.db :as db]
            [groove-api.models.groove :refer [Groove]]
            [groove-api.util.utils :refer [format-groove]]
            [groove-api.db :refer [get-grooves-by-date-range]]
            [schema-tools.core :as st]
            [ring.util.http-response :refer [ok unauthorized conflict created]]
            [groove-api.util.utils :refer [parseLong convert-date]]))

(defn- validate-permission [user id]
  (println (str "Userid: " user " accessing data for user " id))
  (println (str "access allowed? " (= (parseLong user) (parseLong id))))
  (= (parseLong user) (parseLong id)))

(defn- validate-user-permission [req id]
    (validate-permission (:id (:identity req)) id))

(defn- has-habit [userId]
  (println userId))

(defn get-by-dates [request userId habit startDate endDate]
  (if (validate-user-permission request userId)
    (ok (db/get-grooves-by-date-range userId habit (convert-date startDate) (convert-date endDate)))
    (unauthorized {:error "Not authorized"})))

(defn get-all-by-dates [request userId startDate endDate]
  (if (validate-user-permission request userId)
    (ok (db/get-all-grooves-by-date-range userId (convert-date startDate) (convert-date endDate)))
    (unauthorized {:error "Not authorized"})))

(defn update-groove [groove request]
  (let [loggedInUser (parseLong (:id (:identity request)))
        habitId (parseLong (:habit_id groove))
        ownerId (parseLong (:owner_id groove))
        validUser (validate-permission loggedInUser ownerId)
        habit (db/get-user-habit loggedInUser habitId)
        grooveId (db/groove-id-by-user-habit-date loggedInUser habitId (convert-date (:date groove)))]
  (if (and validUser (not (empty? habit)))
    (if (nil? grooveId)
        (created (str "/groove/" ) (db/create-groove (format-groove groove))) ; this should probably use the id as param after groove/
        (do (db/update-groove grooveId {:state (:state groove)}) 
          (ok groove)))
    (unauthorized {:error "Not authorized"}))))

(defn create-habit [habit req] ;; One only gets this far if the user is authenticated
  (if (validate-user-permission req (:owner_id habit))
    (let [dbHabit (db/get-habit-by-name (:name habit))]
      (if (empty? dbHabit)
        (ok (db/create-user-habit (db/create-habit (:name habit)) (:owner_id habit)))
        (if (empty? (db/get-user-habit (:owner_id habit) (:id dbHabit)))
          (ok (db/create-user-habit dbHabit (:owner_id habit)))
          (conflict {:error "You are already tracking that habit"}))))
    (unauthorized {:error "unauthorized"})))

(defn get-habits [userId request]
  (if (validate-user-permission request userId)
    (let [habits (db/get-all-habits-by-userId userId)]
      (ok habits))
    (unauthorized {:error "unauthorized"})))

;;(defn select [user req model])
;;(defn update! [])
;;(defn delete! [])
;;(defn insert! [])
