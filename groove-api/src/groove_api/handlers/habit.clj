(ns groove-api.handlers.habit
  (:require [ring.util.http-response :refer [ok not-found created]]
            [groove-api.models.habit :refer [Habit]]
            [groove-api.handlers.response :refer [response-handler]]
            [groove-api.util.utils :refer [parseLong]]
            [groove-api.bulwark :refer [create-habit get-habit get-habits get-all-grooves-and-habits-by-date-range]]))

(defn- lower-habit-name [habit]
  (assoc habit :name (.toLowerCase (:name habit))))

(defn create-habit-handler [habit request]
  (response-handler :POST create-habit (lower-habit-name habit) request))

(defn get-habit-handler [habit-id request]
  (response-handler :GET get-habit habit-id request))

(defn get-habits-handler [request]
  (response-handler :GET get-habits request))

(defn get-all-grooves-by-habit [req start end]
  (response-handler :GET get-all-grooves-and-habits-by-date-range req start end))

