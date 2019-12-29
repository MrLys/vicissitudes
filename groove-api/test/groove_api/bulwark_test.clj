(ns groove-api.bulwark-test
  (:require [clojure.test :refer :all]
            [groove-api.util.utils :refer [parseLong]]
            [groove-api.core :refer :all]
            [groove-api.bulwark :as blwrk]
            [groove-api.db-test-utils :refer :all]
            [groove-api.db :as db]))


(defn- get-mock-request [user]
  {:identity (db/new-user! user)})

(defn- each-fixture-wrapper []
  )



(defn- generate-grooves [user habit start-date end-date]
  "Generates some grooves for a habit for a date range")

(defn- create-user
  ([] (get-mock-request {:password "password" :username "test@test.no" :email "test@test.no"}))
  ([offset] (get-mock-request {:password "password" :username (str "test" offset "@test.no") :email (str "test" offset "@test.no")})))

(deftest create-habit-test
  "Test creation of a habit"
  (let [req (create-user)
        user (:identity req)
        habit {:name "piano" :owner_id (parseLong (:id user))}
        response (blwrk/create-habit habit req)
        db-habit (db/get-habit-by-name (:name habit))]
    (and
      (is (not (nil? db-habit)))
      (is (not (nil? (db/get-user-habit (:id user) (:id db-habit)))))
      (is (= (str (:status response)) "200")) ;; http status ok
      (is (= (str (:habit_id (:body response))) (str (:id db-habit)))) ; correct habit id
      (is (= (str (:owner_id (:body response))) (str (:id user))))  ; correct user id
      (is (= (str (:habit_id (:body response))) (str (:habit_id (db/get-user-habit (:id user) (:id db-habit))))))
      (is (= (str (:owner_id (:body response))) (str (:owner_id (db/get-user-habit (:id user) (:id db-habit)))))))))

(deftest get-habit-test
  "Test creation of a habit"
  (let [req1 (create-user 1)
        user1 (:identity req1)
        req2 (create-user 2)
        user2 (:identity req2)
        habit1 {:name "piano" :owner_id (parseLong (:id user1))}
        habit2 {:name "exercise" :owner_id (parseLong (:id user2))}
        _ (blwrk/create-habit habit1 req1)
        _ (blwrk/create-habit habit2 req2)]
    (and
      ; User1 should not have access to habit2
      ; User2 should not have access to habit1
      (is (nil? (db/get-user-habit (:id user2) (:id (db/get-habit-by-name (:name habit1))))))
      (is (nil? (db/get-user-habit (:id user1) (:id (db/get-habit-by-name (:name habit2))))))
      (is (= (count (:body (blwrk/get-habits req1))) 1))
      (is (= (count (:body (blwrk/get-habits req2))) 1)))))

(deftest create-duplicate-habits
  "Create a duplicate habit"
  (let [req (get-mock-request {:password "password" :username "test@test.no" :email "test@test.no"})
        user (:identity req)
        habit1 {:name "exercise" :owner_id (parseLong (:id user))}
        habit2 {:name "piano" :owner_id (parseLong (:id user))}
        _ (blwrk/create-habit habit1 req)
        _ (blwrk/create-habit habit2 req)
        habits (blwrk/get-habits req)
        habit3 (blwrk/create-habit habit1 req)]
    (and
      (is (= (str (:status habits)) (str 200)))
      (is (= (count (:body habits)) 2))
      (is (= (:status (:body habit3) 409))))))


(defn- create-groove [req resp today offset state]
  "Create a groove based on request a user habit and date today"
  {
   :owner_id (parseLong (:id (:identity req)))
   :state state
   :user_habit_id (parseLong (:id (:body resp)))
   :date (.plusDays today offset)
   })

(deftest update-groove-test
  "Test updating/creating grooves.
  Creates two users.
  Creates two habits for user 1
  Updates three grooves per habit
  Verify
  Update again."
  (let [req1 (create-user)
        user1 (:identity req1)
        req2 (create-user 2)
        user2 (:identity req2)
        habit1 {:name "exercise" :owner_id (parseLong (:id user1))}
        habit2 {:name "piano" :owner_id (parseLong (:id user1))}
        resp1 (blwrk/create-habit habit1 req1)
        resp2 (blwrk/create-habit habit2 req1)
        today (java.time.LocalDate/now)
        g1 (create-groove req1 resp1 today -1 "success")
        _ (blwrk/update-groove g1 req1)
        g2 (create-groove req1 resp1 today -4 "success")
        _ (blwrk/update-groove g2 req1)
        g3 (create-groove req1 resp1 today -5 "fail")
        _ (blwrk/update-groove g3 req1)
        g4 (create-groove req1 resp2 today 0 "success")
        _ (blwrk/update-groove g4 req1)
        g5 (create-groove req1 resp2 today -2 "success")
        _ (blwrk/update-groove g5 req1)
        g6 (create-groove req1 resp2 today -3 "fail")
        _ (blwrk/update-groove g6 req1)
        user1-grooves-habit1 (blwrk/get-by-dates req1 (parseLong (:id user1)) (parseLong (:id (:body resp1))) (.plusDays today -6) today)
        user1-grooves-habit2 (blwrk/get-by-dates req1 (parseLong (:id user1)) (parseLong (:id (:body resp2))) (.plusDays today -6) today)
        user2-grooves-habit1 (blwrk/get-by-dates req2 (parseLong (:id user2)) (parseLong (:id (:body resp1))) (.plusDays today -6) today)]
    (and
      (is (= (count (:body user1-grooves-habit1)) 3))
      (is (= (count (:body user1-grooves-habit2)) 3))
      (is (= (count (:body user2-grooves-habit1)) 0))
      (is (= (:state (nth (:body user1-grooves-habit1) 2)) (:state g1)))
      (is (= (:state (nth (:body user1-grooves-habit1) 1)) (:state g2)))
      (is (= (:state (nth (:body user1-grooves-habit1) 0)) (:state g3)))
      (is (= (:state (nth (:body user1-grooves-habit2) 2)) (:state g4)))
      (is (= (:state (nth (:body user1-grooves-habit2) 1)) (:state g5)))
      (is (= (:state (nth (:body user1-grooves-habit2) 0)) (:state g6)))
      (do
        (blwrk/update-groove (create-groove req1 resp1 today -1 "fail") req1)
        (let [updated (blwrk/get-by-dates req1 (parseLong (:id user1)) (parseLong (:id (:body resp1))) (.plusDays today -6) today)]
          (is (= (count (:body updated)) 3))
          (is (= (:state (nth (:body updated) 2)) "fail"))
          (is (= (:state (nth (:body updated) 1)) (:state g2)))
          (is (= (:state (nth (:body updated) 0)) (:state g3))))))))

(deftest get-by-dates-test
  "Test getting grooves by date")

(use-fixtures :once once-fixture)
(use-fixtures :each each-fixture)
