(ns groove-api.handlers.groove
  (:require [groove-api.util.validation :refer :all]
            [groove-api.util.utils :refer :all]
            [groove-api.auth.token :refer [create-token]]
            [groove-api.handlers.response :refer [response-handler]]
            [ring.util.http-response :refer [ok unauthorized not-found]]
            [groove-api.bulwark :refer [get-by-dates update-groove get-all-by-dates]]
            [groove-api.models.groove :refer [Groove]]))

(defn update-groove-handler [groove request]
  (response-handler 
    :PATCH 
    #(let [date (:date groove)
            tomorrow (.plusDays (java.time.LocalDate/now) 1)]
        (if (.isAfter tomorrow date)
          (%1 %2 %3)
          {:error "Cannot update grooves into the future"})) update-groove groove request))


(defn get-grooves-handler [request habit start end]
  (get-by-dates request habit start end))

(defn get-all-grooves-handler [request user start end]
  (get-all-by-dates request start end))

(defn groove->response [day]
  (if day
    (ok day)
    (not-found)))
