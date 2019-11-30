(ns groove-api.util.validation
  (:require [buddy.core.hash :as hash]
            [buddy.core.codecs :refer :all]
            [clj-http.client :as client]))

(def breach_limit 1)
(def lower_limit 8)
(def upper_limit 128)


(defn get-digest [^String pwd]
  (-> (hash/sha1 pwd)
      (bytes->hex)))

(defn- find-matches [suffix digests] (filter (fn [digest] (= suffix (.toLowerCase (:hash digest)))) digests))

(defn- get-match [suffix digests] (:freq (first (find-matches suffix digests))))

(defn- digest-in-digests? [suffix digests] ;-- only one match
  (let [match (get-match  suffix digests)]
    (if (empty? match)
      false
      (> (Integer/parseInt match) breach_limit))))
  
(defn- get-prefix [^String digest]
  (subs digest 0 5))

(defn- get-suffix [^String digest]
  (subs digest 5))

(defn- structure [body]
  (re-seq #"(\w+):(\d+)\r\n" body))

(defn build-map [body]
  (let [body-map (structure body)]
    (if body-map 
      (map (fn [x] {:hash (nth x 1) :freq (nth x 2)}) body-map)
      {})))

(defn- get-db [prefix] 
  (client/get (str "https://api.pwnedpasswords.com/range/" prefix)))

(defn- breached? [^String pwd]
  (let [digest (get-digest pwd)]
  (digest-in-digests? 
    (get-suffix digest) 
    (build-map 
      (:body (get-db (get-prefix digest)))))))
                            
(defn valid-password? [^String pwd]
  (and 
    (> (.length pwd) lower_limit)
    (< (.length pwd) upper_limit)
    (not (breached? pwd))))
