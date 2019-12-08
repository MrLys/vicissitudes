(ns groove-api.routes.groove
  (:require [schema.core :as s]
            [toucan.db :as db]
            [groove-api.models.groove :refer [Groove]]
            [groove-api.handlers.groove :refer :all]
            [groove-api.middleware :refer [wrap-basic-auth wrap-token-auth auth-credentials-reponse]]
            [groove-api.util.utils :refer [convert-date]]
            [compojure.api.sweet :refer [GET POST PATCH]]
            [groove-api.util.validation :refer :all]))

(s/defschema GrooveRequestSchema 
  {:owner_id (s/constrained s/Int valid-user-id?)
   :state (s/constrained s/Str valid-groove?)
   :habit_id (s/constrained s/Int valid-habit-id?)
   :date (s/constrained java.util.Date valid-date?)})

(def groove-routes
  [(-> (GET "/grooves/:user_id/:habit_id"  [:as request]
            :header-params [authorization :- String]
            :tags ["grooves"]
            :middleware [wrap-token-auth]
            :path-params [user_id :- Long, habit_id :- Long]
            :query-params [start_date :- s/Str, end_date :- s/Str]
            (get-grooves-handler request user_id habit_id start_date end_date)))
   (-> (POST "/groove/" []
             :header-params [authorization :- String]
             :middleware [wrap-token-auth]
             :tags ["grooves"]
             :body [create-groove-req GrooveRequestSchema]
             (create-groove-handler (convert-date create-groove-req))))])
