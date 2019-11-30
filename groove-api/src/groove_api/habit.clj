(ns groove-api.habit
  (:require [schema.core :as s]
            [clojure.string :as str]
            [toucan.db :as db]
            [groove-api.models.habit :refer [Habit]]
            [groove-api.util.validation :refer :all]
            [compojure.api.sweet :refer [GET POST]]
            [ring.util.http-response :refer [ok not-found]]
            [ring.util.http-response :refer [created]]))


(defn id->created [id]
  (created (str "/habits/" id) {:id id}))

(s/defschema HabitRequestSchema
  {:owner_id (s/constrained s/Int valid-habit-id?) 
   :name (s/constrained s/Str valid-name?)})

(defn create-habit-handler [create-habit-req]
  (id->created (get (db/insert! Habit create-habit-req) :id)))

(defn habit->response [habit]
  (if habit
    (ok habit)
    (not-found)))

(defn get-habit-handler [id]
  (-> (Habit id)
      habit->response))

(defn get-habits-handler []
  (->> (db/select Habit)
       ok))

(def habit-routes
  [(POST "/habits" []
         :body [create-habit-req HabitRequestSchema]
         (create-habit-handler create-habit-req))
   (GET "/habits/:id" []
        :path-params [id :- s/Int]
        (get-habit-handler id))
   (GET "/habits" []
        (get-habits-handler))])
