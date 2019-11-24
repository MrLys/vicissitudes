(ns groove-api.choice
  (:require [compojure.api.sweet :refer [GET POST]]
            [ring.util.http-response :refer [ok not-found]]
            [schema.core :as s]))

(def states #{"none", "fail", "success", "pass"})
(def mock-days {:monday "none" :tuesday "fail" :wednesday "success" :thursday "none" :friday "none" :saturday "none" :sunday "none"})
(defn valid-choice? [state]
  (contains? states state))

(s/defschema ChoiceRequestSchema 
  {:state (s/constrained s/Str valid-choice?)})
(defn choice->response [day]
  (if day
    (ok day)
    (not-found)))

(defn get-choice-handler [day]
  (choice->response (get mock-days (keyword day))))

(defn get-choices-handler []
  (ok mock-days))

 (defn update-choice-handler [day state]
  (if (valid-choice? state)
    (choice->response (assoc mock-days (keyword day) state))
    (choice->response nil)))

(def choice-routes
  [(GET "/choice/:day" []
        :path-params [day :- s/Str]
        (get-choice-handler day))
   (GET "/choices" []
        (get-choices-handler))
   (POST "/choice/:day" []
         :path-params [day :- s/Str]
         :query-params [state :- s/Str]
         (update-choice-handler day state))])
