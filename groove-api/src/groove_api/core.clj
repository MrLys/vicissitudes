(ns groove-api.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.api.sweet :refer [api context routes]]
            [groove-api.routes.groove :refer [groove-routes]]
            [groove-api.routes.user :refer [user-routes]]
            [groove-api.routes.habit :refer [habit-routes]]
            [toucan.db :as db]
            [toucan.models :as models]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [buddy.auth.accessrules :refer [restrict]]
            [buddy.auth :refer [authenticated?]]
            [environ.core :refer [env]]))

(def db-spec
  {:dbtype (env :dbtype)
   :dbname (env :dbname)
   :user (env :user) 
   :password (env :password)})
(def swagger-config
  {:ui "/api/v1/docs"
   :spec "/swagger.json"
   :data {:info {:version "1.0.0", :title "RESTful groove API"
                 :securityDefinitions {:api_key {:type "apiKey" :name "Authorization" :in "header"}}}}})
(def app 
  (api
    {:swagger swagger-config}
  (context "/api" [] groove-routes user-routes habit-routes)))

(defn -main
  [& args]
  (run-jetty app {:port 3000}))

(defn -dev-main
  [& args]
  (db/set-default-db-connection! db-spec)
  (models/set-root-namespace! 'groove-api.models)
  (run-jetty (wrap-reload #'app) {:port (Integer/parseInt (env :port))}))
