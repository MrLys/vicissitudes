(ns groove-api.handlers.habit
  (:require [ring.util.http-response :refer [ok not-found created]]
            [groove-api.models.habit :refer [Habit]]
            [groove-api.bulwark :refer [create-habit get-habits]]))

(defn id->created [id]
  (created (str "/habits/" id) {:id id}))

(defn- lower-habit-name [habit]
  (assoc habit :name (.toLowerCase (:name habit))))

(defn create-habit-handler [habit request]
  (create-habit (lower-habit-name habit) request))

(defn habit->response [habit]
  (if habit
    (ok habit)
    (not-found)))

(defn get-habit-handler [habitId request]
  (get-habits habitId request))

(defn get-habits-handler [request]
  (get-habits request))

