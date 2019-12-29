(ns groove-api.db-test-utils
  (:require [clojure.test :refer :all]
            [groove-api.core :refer :all]
            [toucan.db :as db]
            [groove-api.core :refer :all]
            [clojure.java.jdbc :as j]
            [environ.core :refer [env]]))


(def pg-db {:dbtype "postgresql"
            :dbname (env :dbname)
            :host "localhost"
            :user  (env :user)
            :password (env :password)
            :ssl false})

(defn clear [table]
  (j/execute! pg-db [(format "TRUNCATE public.%s CASCADE" (name table))]))
; Based on:
; https://gist.github.com/quux00/3606159#file-gistfile1-clj

(defn one-time-setup []
  (setup-db)
  (clear :user)
  (clear :habit))

(defn one-time-teardown []
  (clear :user)
  (clear :habit))

(defn once-fixture [f]
  (one-time-setup)
  (f)
  (one-time-teardown))

(defn setup []
  (clear :user)
  (clear :habit))

(defn teardown []
  (clear :user)
  (clear :habit))

(defn each-fixture [f]
  (setup)
  (f)
  (teardown))

