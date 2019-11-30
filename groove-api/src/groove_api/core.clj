(ns groove-api.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.api.sweet :refer [api context routes]]
            [groove-api.groove :refer [groove-routes]]
            [groove-api.user :refer [user-routes]]
            [groove-api.habit :refer [habit-routes]]
            [groove-api.middleware :refer [wrap-session-auth]]
            [toucan.db :as db]
            [toucan.models :as models]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [buddy.auth.accessrules :refer [restrict]]
            [buddy.auth :refer [authenticated?]]))

(def db-spec
  {:dbtype "postgres"
   :dbname "groove-api"
   :user "lyseggen"
   :password "groove-api"})
(def swagger-config
  {:ui "/api/swagger"
   :spec "/api/swagger.json"
   :options {:ui {:validatorUrl nil}
             :data {:info {:version "1.0.0", :title "RESTful groove API"}}}})
(def app 
  (api
    {:swagger swagger-config}
  (context "/api" [] groove-routes user-routes habit-routes)))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (run-jetty app {:port 3000}))

(defn -dev-main
  [& args]
  (db/set-default-db-connection! db-spec)
  (models/set-root-namespace! 'groove-api.models)
  (run-jetty (wrap-reload (wrap-defaults #'app site-defaults)) {:port 3000}))
