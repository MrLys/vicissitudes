(ns groove-api.core
  (:require [compojure.api.sweet :refer [api context routes]]
            [groove-api.routes.groove :refer [groove-routes]]
            [groove-api.routes.user :refer [user-routes]]
            [groove-api.routes.habit :refer [habit-routes]]
            [toucan.db :as db]
            [toucan.models :as models]
            [groove-api.util.utils :refer [parseLong]]
            [ring.middleware.reload :refer [wrap-reload]]
            [environ.core :refer [env]]
            [org.httpkit.server :refer :all]))

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
  (db/set-default-db-connection! db-spec)
  (models/set-root-namespace! 'groove-api.models)
  (run-server app {:port (parseLong (env :port))})) ;; http-kit complains that it cannot parse from string to Number?

(defn -dev-main
  [& args]
  (db/set-default-db-connection! db-spec)
  (models/set-root-namespace! 'groove-api.models)
  (run-server (wrap-reload #'app) {:port (env :port)}))
