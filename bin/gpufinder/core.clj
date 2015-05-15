(ns gpufinder.core  
  (:use compojure.core
        gpufinder.views
        gpufinder.controller
        [hiccup.middleware :only (wrap-base-url)])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]
            [compojure.response :as response]))

(defroutes main-routes
;  (GET "/" request "Welcome!"))
(GET "/" [] (login-page))
(POST "/login" [username password] (login username password))
(route/resources "/")
(route/not-found (not-found)))

(def app
  (-> (handler/site main-routes)))