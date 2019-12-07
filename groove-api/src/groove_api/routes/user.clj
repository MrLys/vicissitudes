(ns groove-api.routes.user
  (:require [groove-api.handlers.user :refer :all]
            [groove-api.util.validation :refer :all]
            [compojure.api.sweet :refer [GET POST PATCH]]
            [schema.core :as s]))

(s/defschema UserRequestSchema
  {:username (s/constrained s/Str valid-username?)
   :email (s/constrained s/Str valid-email?)
   :password (s/constrained s/Str valid-password?)})

(def user-routes
  [(POST "/users" []
         :tags ["User"]
         :body [create-user-req UserRequestSchema]
         (create-user-handler2 create-user-req))
    (POST "/user" []
         :tags ["User"]
         :body [user UserRequestSchema]
         (create-user-handler2 user))
   (GET "/users/:id" []
         :tags ["User"]
        :path-params [id :- s/Int]
        (get-user-handler id))
   (GET "/users" []
         :tags ["User"]
        (get-users-handler))])
