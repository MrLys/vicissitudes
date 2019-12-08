(ns groove-api.bulwark
  (:require [toucan.db :as db]
            [groove-api.db :refer [get-grooves-by-date-range]]
            [ring.util.http-response :refer [ok unauthorized]]
            [groove-api.util.utils :refer [parseLong convert-date]]))

(defn- validate-user-permission [user id]
  (println (str "Userid: " user " accessing data for user " id " access allowed? " (= user id)))
  (= (parseLong user) (parseLong id)))

(defn get-by-dates [request user_id habit startDate endDate]
  (if (validate-user-permission (:id (:identity request)) user_id)
    (ok (get-grooves-by-date-range user_id habit (convert-date startDate) (convert-date endDate)))
    (unauthorized {:error "Not authorized"})))


