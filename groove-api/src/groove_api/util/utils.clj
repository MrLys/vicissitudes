(ns groove-api.util.utils
  (:require [clj-time.format :as f]
            [clj-time.core :as t]
            [clj-time.coerce :as c]))


(defn convert-date [date]
  (if (instance? Long date)
    (java.sql.Timestamp. date)
  (java.sql.Timestamp. (c/to-long date))))

(defn convert-date-groove [groove]
  (assoc groove :date (convert-date (:date groove))))

(defn format-groove [groove]
  (assoc groove :state (.toLowerCase (:state groove)) :date (convert-date (:date groove))))
(defn parseLong [s]
  (println (str "this cannot be nil " s))
  (if (instance? Long s)
    s
  (if (re-matches #"\d+" s)
    (Long/valueOf s)
    0)))
