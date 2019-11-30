(ns groove-api.util.utils
  (:require [clj-time.format :as f]
            [clj-time.core :as t]
            [clj-time.coerce :as c]))

(defn format-groove [groove]
  (assoc groove :state (.toLowerCase (:state groove))))

(defn convert-date [groove]
  (assoc groove :date (java.sql.Timestamp. (.getTime (:date groove)))))
