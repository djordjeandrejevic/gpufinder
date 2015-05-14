(ns gpufinder.core  
  (:use compojure.core)
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :as response]))

(defroutes main-routes
  (GET "/" request "Welcome!"))

(def app
  (-> (handler/site main-routes)))