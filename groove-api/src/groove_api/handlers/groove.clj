(ns groove-api.handlers.groove
  (:require [groove-api.util.validation :refer :all]
            [groove-api.util.utils :refer :all]
            [groove-api.auth.token :refer [create-token]]
            [ring.util.http-response :refer [ok unauthorized not-found]]
            [groove-api.bulwark :refer [get-by-dates update-groove get-all-by-dates]]
            [groove-api.models.groove :refer [Groove]]))

(defn update-groove-handler [groove request]
  (update-groove groove request))

(defn get-grooves-handler [request habit start end]
  (get-by-dates request habit start end))

(defn get-all-grooves-handler [request user start end]
  (get-all-by-dates request start end))

(defn groove->response [day]
  (if day
    (ok day)
    (not-found)))
