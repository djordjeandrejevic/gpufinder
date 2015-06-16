(ns gpufinder.controller
  (:use [db.db :as db]
        [gpufinder.scrapper :as scrapper])
  (:require  [noir.response :refer [redirect]]
             [noir.session :as session]
             [ring.util.response :refer [response]]))

(defn to-numeric [value]
  (.replaceAll value "[^0-9]" ""))

(defn gpu-with-integer-fields [raw-gpu]
  (let [tdp (to-numeric (raw-gpu :tdp))
        vram (to-numeric (raw-gpu :vram))
        price (to-numeric (raw-gpu :price))]
    (dissoc raw-gpu :tdp :vram :price)
    (assoc raw-gpu :tdp (read-string tdp) :vram (read-string vram) :price (read-string price))))

(defn initialize-gpus []    
  (for [x (scrapper/all-gpus)] 
    (let [gpu (scrapper/order-scraped-data x)]
      (if (= (gpu :type) "Desktop")
        (if (= 7 (count gpu))          
          (db/insert-gpu (gpu-with-integer-fields gpu)))))))


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

(defn return-tdp [psu]
  (cond
    (if (and (> psu 0) (< psu 300))
      75) 75
    (if (and (> psu 300) (< psu 400))
      120) 120
    (if (and (> psu 400) (< psu 450))
      150) 150
    (if (and (> psu 450) (< psu 500))
      180) 180
    (if (and (> psu 500) (< psu 550))
      210) 210
    (if (> psu 550) (< psu 600)
      230) 230
    (if (> psu 600)
      java.lang.Integer/MAX_VALUE) java.lang.Integer/MAX_VALUE))


(defn validate-psu [psu]
  (if (number? psu)
    (if (< psu 0)
      (session/put! :session-message "You must enter positive number of watts for your PSU!") 
      (let [tdp (return-tdp psu)]
        tdp))
    (session/put! :session-message "You must enter number for PSU wattage, not characters!")))


(defn find-gpu [price vram psu]
  (let [myAtom (atom {:price "" :vram "" :tdp ""})]
    (if-not (= price 0)
      (swap! myAtom assoc :price price))
    (if-not (= vram 0)
      (swap! myAtom assoc :vram vram))
    (swap! myAtom assoc :tdp (validate-psu psu))
    (db/find-gpu-in-db myAtom)))

;(db/search-gpu-db query)))


;(for [x (scrapper/all-gpus)]
;    (if (= (x :type) "Desktop")
;      gpu
;      false)))
