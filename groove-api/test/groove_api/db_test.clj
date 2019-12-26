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
      (and
        (is (:username dbUser) (:username inDb))
        (is (assertEqual (:email dbUser) (:email inDb)))
        (is (assertEqual (:id dbUser) (:id inDb)))))))

(deftest new-existing-user-test
  "Create a new user that already exists"
  (let [_ (setup-db)
        user {:password "password" :username "test@test.no" :email "test@test.no"}
        dbUser (db/new-user! user)]
      (is (thrown? org.postgresql.util.PSQLException (db/new-user! user))))
  (clear :user))

(deftest get-all-users-empty
  "Get all users in empty db and assert the number is correct"
  (setup-db)
  (is (= 0 (count (db/get-all-users)))))

(deftest get-all-users
  "Get all users in empty db and assert the number is correct"
  (let [_ (setup-db)
        _ (db/new-user! {:password "password" :username "test@test.no" :email "test@test.no"})
        _ (db/new-user! {:password "password" :username "test2@test.no" :email "test2@test.no"})
        _ (db/new-user! {:password "password" :username "test3@test.no" :email "test3@test.no"})]
  (is (= 3 (count (db/get-all-users)))))
  (clear :user))

(deftest create-habit-test
  "Create a habit, verify it in db"
  (let [_ (setup-db)
        dbHabit (db/create-habit "exercising")
        inDb (db/get-habit-by-name "exercising")
        _ (clear :habit)]
  (and
    (is (= (:name dbHabit) (:name inDb)))
       (is (= (:id dbHabit) (:id inDb))))))

(deftest create-existing-habit-test
  "Create a habit, verify it in db"
  (let [_ (setup-db)
        dbHabit (db/create-habit "exercising")]
    (is (thrown? org.postgresql.util.PSQLException (db/create-habit "exercising"))))
  (clear :habit))

(deftest activate-user-test
  "Create a user and activate it in the db"
  (let [_ (setup-db)
        user (db/new-user! {:password "password" :username "test@test.no" :email "test@test.no"})
        updated? (db/activate-user (:id user))
        dbUser (db/get-user (:id user))
        _ (clear :user)]
    (and (is updated?)
         (is (:activated dbUser)))))

