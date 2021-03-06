(ns gpufinder.core  
  (:use compojure.core
        gpufinder.views
        gpufinder.controller
        [hiccup.middleware :only (wrap-base-url)]
        [noir.util.middleware])
  (:require [compojure.route :as route]
            [compojure.handler :as handler]            
            [compojure.response :as response]
            [noir.session :as session]
            [noir.response :refer [redirect]]))

(defroutes main-routes  
  (GET "/" [] (login-page))
  (GET "/index" [] (index-page))
  (GET "/register" [] (register-page))
  (POST "/register" [username password repeated-password] (register username password repeated-password))
  (POST "/login" [username password] (login username password))
  (GET "/logout" [] (logout))
  (POST "/initialize-gpus" [] (initialize-gpus))
  (POST "/find-gpu" [price vram psu] (let [results (find-gpu price vram psu)] (gpu-results-page results)))
  (GET "/wishlist" [] (let [results (get-wishlist)] (wishlist-page results)))
  (POST "/add-to-wishlist" [wishlist] (add-to-wishlist wishlist))
  (POST "/remove-from-wishlist" [gpu-id] (delete-from-wishlist gpu-id))
  (route/resources "/")
  (route/not-found (not-found)))

(def app
  (-> (handler/site main-routes)
    (session/wrap-noir-session)    
    (wrap-base-url)))