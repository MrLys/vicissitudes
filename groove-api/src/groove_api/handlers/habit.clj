(ns groove-api.handlers.habit
  (:require [ring.util.http-response :refer [ok not-found created]]
            [groove-api.models.habit :refer [Habit]]
            [groove-api.bulwark :refer [create-habit]]
            [toucan.db :as db]))

(defn id->created [id]
  (created (str "/habits/" id) {:id id}))


(defn create-habit-handler [habit request]
  (create-habit habit request))

(defn habit->response [habit]
  (if habit
    (ok habit)
    (not-found)))

(defn get-habit-handler [id]
  (-> (Habit id)
      habit->response))

(defn get-habits-handler []
  (->> (db/select Habit)
       ok))

