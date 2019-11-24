(ns groove-api.core
  (:require [ring.adapter.jetty :refer [run-jetty]]
            [compojure.api.sweet :refer [api context routes]]
            [jumblerg.middleware.cors :refer [wrap-cors]]
            [groove-api.choice :refer [choice-routes]]))

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
