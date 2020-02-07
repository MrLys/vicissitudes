(ns groove-api.handlers.habit
  (:require [ring.util.http-response :refer [ok not-found created]]
            [groove-api.models.habit :refer [Habit]]
            [groove-api.handlers.response :refer [response-handler]]
            [groove-api.util.utils :refer [parseLong]]
            [groove-api.bulwark :refer [create-habit get-habit get-habits get-all-grooves-and-habits-by-date-range]]))


(defn build-grooves-by-habits [grooves]
  (println grooves)
  (reduce (fn [coll x]
            (assoc coll
                   (keyword (:name x))
                   {:name (:name x) :id (:id x) :owner_id (:owner_id x) :grooves (vec (conj (:grooves ((keyword (:name x)) coll)) (dissoc x :name)))}))
          {}
          grooves))

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

