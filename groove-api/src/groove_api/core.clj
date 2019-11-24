(ns groove-api.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.api.sweet :refer [api context routes]]
            [jumblerg.middleware.cors :refer [wrap-cors]]
            [groove-api.choice :refer [choice-routes]]
            [ring.middleware.reload :refer [wrap-reload]]))

(def swagger-config
  {:ui "/swagger"
   :spec "/swagger.json"
   :options {:ui {:validatorUrl nil}
             :data {:info {:version "1.0.0", :title "RESTful groove API"}}}})
(def app 
  (api
    {:swagger swagger-config}
  (context "/api" [] choice-routes) #".*"))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (run-jetty app {:port 3000}))

(defn -dev-main
  [& args]
  (run-jetty (wrap-reload #'app) {:port 3000}))
