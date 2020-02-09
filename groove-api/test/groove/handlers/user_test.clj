(ns groove.handlers.user-test
  (:require [clojure.test :refer :all]
            [groove.util.utils :refer [parseLong create-activation-token]]
            [groove.util.validation :refer :all]
            [groove.bulwark :as blwrk]
            [groove.db-test-utils :refer :all]
            [groove.handlers.user :as handler]
            [groove.test-utils :refer :all]
            [groove.db :as db]))



(deftest create-user-and-password-token
  "Test creation of a user, activation,
   forgot password password, then update password. With handlers"
  (let [request {:password "password" :username "test@test.no" :email "test@test.no"}
        user (handler/create-user request)
        activation-token (db/get-token-by-user-id (:id user))
        token (handler/forgot-password-handler (:email request))]
    (and
      (is (nil? (:error user)))
      (is (> (:id user) 0))
      (is (nil? (:error activation-token)))
      (is (valid-token? activation-token))
      (is (blwrk/activate-user activation-token))
      (is (nil? (:error token)))
      (is (valid-token? token))
      (is (= (:user_id token) (:id user)))
      (is (not (:user token)))
      (is (handler/update-user-password {:user_id (:id user) :password (:password "e8ms2cu1fese7h8k6fcog")})))))


        
(use-fixtures :once once-fixture)
(use-fixtures :each each-fixture)
