(ns groove-api.groove
  (:require [compojure.api.sweet :refer [GET POST PUT]]
            [toucan.db :as db]
            [ring.util.http-response :refer [created]]
            [groove-api.models.groove :refer [Groove]]
            [groove-api.util.validation :refer :all]
            [groove-api.util.utils :refer :all]
            [ring.util.http-response :refer [ok not-found]]
            [ring.util.http-response :refer [created]]
            [schema.core :as s]))

(s/defschema GrooveRequestSchema 
  {:owner_id (s/constrained s/Int valid-user-id?)
   :state (s/constrained s/Str valid-groove?)
   :habit_id (s/constrained s/Int valid-habit-id?)
   :date (s/constrained java.util.Date valid-date?)})

(defn groove->response [day]
  (if day
    (ok day)
    (not-found)))

(defn get-grooves-handler []
  (ok (db/select Groove)))


(defn id->created [id]
  (created id))

(defn create-groove-handler [req]
  (id->created (db/insert! Groove (format-groove req))))

(defn update-groove-handler [id req]
   (if (db/update! Groove req)
     (ok)
     (create-groove-handler req)))

(def groove-routes
  [(GET "/grooves" []
        :summary "Get all grooves"
        (get-grooves-handler))
   (POST "/groove/" []
         :body [create-groove-req GrooveRequestSchema]
         (create-groove-handler (convert-date create-groove-req)))
   (PUT "/groove/:id" []
        :path-params [id :- s/Int]
        :body [update-groove-req GrooveRequestSchema]
        (update-groove-handler id update-groove-req))])

