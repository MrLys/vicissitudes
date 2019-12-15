(ns groove-api.routes.habit
  (:require [schema.core :as s]
            [groove-api.handlers.habit :refer :all]
            [groove-api.util.validation :refer :all]
            [groove-api.middleware :refer [wrap-token-auth]]
            [compojure.api.sweet :refer [GET POST PATCH]]))

(s/defschema HabitRequestSchema
  {:owner_id (s/constrained s/Int valid-habit-id?) 
   :name (s/constrained s/Str valid-name?)})

(def habit-routes
  [(POST "/habit" [:as request]
         :tags ["Habits"]
         :header-params [authorization :- String]
         :middleware [wrap-token-auth]
         :body [create-habit-req HabitRequestSchema]
         (create-habit-handler create-habit-req request))
   (GET "/habits/:id" [:as request]
        :tags ["Habits"]
        :header-params [authorization :- String]
        :middleware [wrap-token-auth]
        :path-params [id :- s/Int]
        (get-habit-handler id request))
   (GET "/habits" []
         :tags ["Habits"]
        (get-habits-handler))])
