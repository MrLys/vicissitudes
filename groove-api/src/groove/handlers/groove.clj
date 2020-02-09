(ns groove.handlers.groove
  (:require [groove.handlers.response :refer [response-handler]]
            [groove.bulwark :as blwrk]))

(defn update-groove [groove request]
  (let [date (:date groove)
        tomorrow (.plusDays (java.time.LocalDate/now) 1)]
    (if (.isAfter tomorrow date)
      (blwrk/update-groove groove request)
      {:error "Cannot update grooves into the future"})))


(defn update-groove-handler [groove request]
  (response-handler :PATCH update-groove groove request))

(defn get-grooves-handler [request habit start end]
  (blwrk/get-by-dates request habit start end))

(defn get-all-grooves-handler [request user start end]
  (blwrk/get-all-by-dates request start end))
