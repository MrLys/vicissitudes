(ns groove-api.routes.groove
  (:require [schema.core :as s]
            [toucan.db :as db]
            [groove-api.models.groove :refer [Groove]]
            [groove-api.handlers.groove :refer :all]
            [groove-api.middleware :refer [wrap-basic-auth wrap-token-auth]]
            [groove-api.util.utils :refer [convert-date]]
            [compojure.api.sweet :refer [GET POST PATCH]]
            [groove-api.util.validation :refer :all]))

(s/defschema GrooveRequestSchema 
  {:owner_id (s/constrained s/Int valid-user-id?)
   :state (s/constrained s/Str valid-groove?)
   :habit_id (s/constrained s/Int valid-habit-id?)
   :date (s/constrained java.util.Date valid-date?)})

(def groove-routes
  [(-> (GET "/login" [:as request]
            :header-params [authorization :- String]
            :middleware [wrap-basic-auth]
            (auth-credentials-reponse request)))
   (-> (GET "/grooves" []
            :header-params [authorization :- String]
            :tags ["grooves"]
            :summary "get all grooves"
            :middleware [wrap-token-auth]
            (get-grooves-handler)))
   (-> (POST "/groove/" []
             :header-params [authorization :- String]
             :middleware [wrap-token-auth]
             :tags ["grooves"]
             :body [create-groove-req GrooveRequestSchema]
             (create-groove-handler (convert-date create-groove-req))))
   (-> (PATCH "/groove/:id" []
              :header-params [authorization :- String]
              :middleware [wrap-token-auth]
              :tags ["grooves"]
              :path-params [id :- s/Int]
              :body [update-groove-req GrooveRequestSchema]
              (update-groove-handler id update-groove-req)))])
