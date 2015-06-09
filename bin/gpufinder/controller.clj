(ns gpufinder.controller
  (:use [db.db :as db]
        [gpufinder.scrapper :as scrapper])
  (:require  [noir.response :refer [redirect]]
             [noir.session :as session]
             [ring.util.response :refer [response]]))

(defn to-numeric [value]
  (.replaceAll value "[^0-9]" ""))

(defn gpu-without-letters [raw-gpu]
  (let [tdp (to-numeric (raw-gpu :tdp))
        vram (to-numeric (raw-gpu :vram))
        price (to-numeric (raw-gpu :price))]
   (dissoc raw-gpu :tdp :vram :price)
   (assoc raw-gpu :tdp tdp :vram vram :price price)))
    
(defn initialize-gpus []    
  (for [x (scrapper/all-gpus)] 
    (let [gpu (scrapper/order-scraped-data x)]
      (if (= (gpu :type) "Desktop")
        (if (= 7 (count gpu))          
        (db/insert-gpu (gpu-without-letters gpu)))))))


(defn login [username password]
  (let [user-doesnt-exist (db/auth username password)]
    (if user-doesnt-exist
      (do
        (session/put! :session-message "Incorrect username or password!")
        (redirect "/"))
      (do
        (session/put! :name username)       
        (redirect "index")))))

(defn register [username password repeated-password]
  (if (= password repeated-password)
    (if (db/reg username password)
      (do
        (session/put! :session-message "You have successfully registered, now you can login!")
        (redirect "/"))
      (do
        (session/put! :session-message "Username already exists! Pick another one please.")
        (redirect "/register")))
    (do
      (session/put! :session-message "Passwords didn't match!")
      (redirect "/register"))))



(defn initialize-data []
  (db/initialize-users))

;(for [x (scrapper/all-gpus)]
;    (if (= (x :type) "Desktop")
;      gpu
;      false)))
