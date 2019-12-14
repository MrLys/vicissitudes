(ns groove-api.handlers.groove
  (:require [groove-api.util.validation :refer :all]
            [groove-api.util.utils :refer :all]
            [groove-api.auth.token :refer [create-token]]
            [ring.util.http-response :refer [ok unauthorized not-found]]
            [groove-api.bulwark :refer [get-by-dates update-groove]]
            [groove-api.models.groove :refer [Groove]]))

(defn update-groove-handler [req]
  (println req)
  (update-groove req))

(defn get-grooves-handler [request user habit start end]
  (get-by-dates request user habit start end))

(defn create-groove-handler [groove])

(defn groove->response [day]
  (if day
    (ok day)
    (not-found)))
