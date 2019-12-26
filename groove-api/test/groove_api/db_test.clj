(ns groove-api.db-test
  (:require [clojure.test :refer :all]
            [groove-api.core :refer :all]
            [groove-api.db :as db]
            [groove-api.db-test-utils :refer [clear]]
            [groove-api.test-utils :refer [assertEqual]]))


(deftest new-user-test
  "Create a new user, check that it \"persist\""
  (let [_ (setup-db)
        user {:password "password" :username "test@test.no" :email "test@test.no"}
        dbUser (db/new-user! user)
        inDb (db/get-registered-user-by-username "test@test.no")
        _ (clear :user)] ;; clear database
    (testing "Is the user created, and does the user have the correct values?"
      (is
        (and (assertEqual (:username dbUser) (:username inDb))
             (assertEqual (:email dbUser) (:email inDb))
             (assertEqual (:id dbUser) (:id inDb)))))))

(deftest new-existing-user-test
  "Create a new user that already exists"
  (let [_ (setup-db)
        user {:password "password" :username "test@test.no" :email "test@test.no"}
        dbUser (db/new-user! user)]
      (is (thrown? org.postgresql.util.PSQLException (db/new-user! user))))
  (clear :user))
