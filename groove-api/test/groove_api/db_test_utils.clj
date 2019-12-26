(ns groove-api.db-test-utils
  (:require [clojure.test :refer :all]
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


