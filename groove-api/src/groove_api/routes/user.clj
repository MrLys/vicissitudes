(ns groove-api.routes.user
  (:require [groove-api.handlers.user :refer :all]
            [groove-api.util.validation :refer :all]
            [compojure.api.sweet :refer [GET POST PATCH]]
            [groove-api.middleware :refer [auth-credentials-reponse wrap-basic-auth]]
            [ring.util.http-response :refer [created ok]]
            [schema.core :as s]))

(s/defschema UserRequestSchema
  {:username (s/constrained s/Str valid-username?)
   :email (s/constrained s/Str valid-email?)
   :password (s/constrained s/Str valid-password?)})

(def user-routes
  [(-> (POST "/login" [:as request]
            :header-params [authorization :- String]
            :middleware [wrap-basic-auth]
            (auth-credentials-reponse request)))
   (POST "/register" []
         :tags ["User"]
         :body [user UserRequestSchema]
         (create-user-handler2 user))
   (POST "/activate" [:as request]
         :tags ["User"]
         :query-params [activation_token :- String]
         (activate-user-handler activation_token request))])

