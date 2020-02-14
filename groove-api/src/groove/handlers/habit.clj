(ns groove.handlers.habit
  (:require [ring.util.http-response :refer [ok not-found created]]
            [groove.models.habit :refer [Habit]]
            [groove.handlers.response :refer [response-handler]]
            [groove.util.utils :refer [parseLong build-grooves-by-habits]]
            [groove.bulwark :refer [create-habit get-habit get-habits get-all-grooves-and-habits-by-date-range]]))




(defn- lower-habit-name [habit]
  (assoc habit :name (.toLowerCase (:name habit))))

(defn create-habit-handler [habit request]
  (response-handler :POST create-habit (lower-habit-name habit) request))

(defn get-habit-handler [habit-id request]
  (response-handler :GET get-habit habit-id request))

(defn get-habits-handler [request]
  (response-handler :GET get-habits request))

(defn get-all-grooves-by-habit [req start end]
  (response-handler :GET #(build-grooves-by-habits (%1 %2 %3 %4)) get-all-grooves-and-habits-by-date-range req start end))

