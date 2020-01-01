(ns groove-api.handlers.response
  (:require [ring.util.http-response :refer [ok not-found created unauthorized]]
            [compojure.api.sweet :refer [GET POST PATCH PUT]]))

(defn- valid-response? [value]
  (or (empty? value) (nil? value)))

(def ^:private handler-map {:GET (fn [v] (if (valid-response? v) (not-found "Not found") (ok v)))
                  :POST (fn [v] (if (valid-response? v) (unauthorized "Not authorized!") (created v)))
                  :PATCH (fn [v] (if (valid-response? v) (unauthorized "Not authorized!") (ok v)))
                  :PUT (fn [v] (if (valid-response? v) (unauthorized "Not authorized!")  (ok v)))})
; Get the function or deciding the response
; call the getter/handler function with the args and pass the result to
; handler function from the map
(defn response-handler [req-type f & args]
  ((req-type handler-map) (apply f args)))
