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

   (GET "/habit/:user_id" [:as request]
        :header-params [authorization :- String]
        :tags ["habits"]
        :middleware [wrap-token-auth]
        :path-params [user_id :- Long]
        :query-params [start_date :- java.time.LocalDate, end_date :- java.time.LocalDate]
        (get-all-grooves-by-habit request start_date end_date))
   ;     :tags ["Habits"]
   ;     :header-params [authorization :- String]
   ;     :middleware [wrap-token-auth]
   ;     :path-params [id :- s/Int]
   ;     (get-habit-handler id request))
   (GET "/habits" [:as request]
         :tags ["Habits"]
         :header-params [authorization :- String]
         :middleware [wrap-token-auth]
        (get-habits-handler request))])
