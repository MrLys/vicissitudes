(ns groove-api.routes.habit
  (:require [schema.core :as s]
            [groove-api.handlers.habit :refer :all]
            [groove-api.util.validation :refer :all]
            [compojure.api.sweet :refer [GET POST PATCH]]))

(s/defschema HabitRequestSchema
  {:owner_id (s/constrained s/Int valid-habit-id?) 
   :name (s/constrained s/Str valid-name?)})

(def habit-routes
  [(POST "/habits" []
         :tags ["Habits"]
         :body [create-habit-req HabitRequestSchema]
         (create-habit-handler create-habit-req))
   (GET "/habits/:id" []
         :tags ["Habits"]
        :path-params [id :- s/Int]
        (get-habit-handler id))
   (GET "/habits" []
         :tags ["Habits"]
        (get-habits-handler))])
