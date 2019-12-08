(ns groove-api.util.utils
  (:require [clj-time.format :as f]
            [clj-time.core :as t]
            [clj-time.coerce :as c]))

(defn format-groove [groove]
  (assoc groove :state (.toLowerCase (:state groove))))

(defn convert-date [date]
  (java.sql.Timestamp. (c/to-long date)))
(defn convert-date-groove [groove]
  (assoc groove :date (convert-date (:date groove))))

(defn parseLong [s]
  (if (instance? Long s)
    s
  (if (re-matches #"\d+" s)
    (Integer/parseInt s)
    0)))
