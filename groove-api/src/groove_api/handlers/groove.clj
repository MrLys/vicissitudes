(ns groove-api.handlers.groove
  (:require [groove-api.util.validation :refer :all]
            [groove-api.util.utils :refer :all]
            [groove-api.auth.token :refer [create-token]]
            [groove-api.db :refer :all]
            [toucan.db :as db]
            [ring.util.http-response :refer [ok unauthorized not-found]]
            [groove-api.bulwark :refer [get-by-dates]]
            [groove-api.models.groove :refer [Groove]]))

(defn create-groove-handler [req]
  (println req)
  (id->created (db/insert! Groove (format-groove req))))

(defn get-grooves-handler [request user habit start end]
  (get-by-dates request user habit start end))

(defn update-groove-handler [id req]
   (if (db/update! Groove req)
     (ok)
     (create-groove-handler req)))

(defn groove->response [day]
  (if day
    (ok day)
    (not-found)))
