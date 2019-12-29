(ns groove-api.util.utils
  (:require [clj-time.format :as f]
            [clj-time.core :as t]
            [ring.util.codec :as codec]
            [clj-time.coerce :as c]))





(defn format-groove [groove]
  (assoc groove :state (.toLowerCase (:state groove))))

(defn parseLong [s]
  (if (or (instance? Long s)
          (instance? Integer s))
    s
    (if (re-matches #"\d+" s)
      (Long/valueOf s)
      (throw (.IllegalArgumentException (str s " is not a valid Long"))))))


(defn sanitize [m & args]
  "Updates each value in a map with the url-encoded variation"
  (println m)
  (reduce (fn [r [k v]] (assoc r k (apply codec/url-encode v args))) {} m))


