(ns gpufinder.controller
  (:use [db.db :as db]
        [gpufinder.scrapper :as scrapper])
  (:require  [noir.response :refer [redirect]]
             [noir.response :refer [json]]
             [noir.session :as session]
             [ring.util.response :refer [response]]
             [clojure.string :as str]))

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

(defn logout []
  (do
    (session/remove! (session/get! :name))
    (redirect "/")))

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
    (if (and (>= psu 300) (< psu 400))
      120) 120
    (if (and (>= psu 400) (< psu 450))
      150) 150
    (if (and (>= psu 450) (< psu 500))
      180) 180
    (if (and (>= psu 500) (< psu 550))
      210) 210
    (if (>= psu 550) (< psu 600)
      230) 230
    (if (>= psu 600)
      java.lang.Integer/MAX_VALUE) java.lang.Integer/MAX_VALUE))

(defn validate-psu [psu-string]
  (let [psu (java.lang.Integer/parseInt psu-string)]
    (if (< psu 0)
      (do
        (session/put! :session-message "You must enter positive number of watts for your PSU!")
        (redirect "/index"))
      (let [tdp (return-tdp psu)]
        tdp))))

(defn get-prices-from-string [price_string]
  (let [splitted_array (str/split price_string #" ")]
    (let [price {:price_from (read-string (nth splitted_array 0)) :price_to (read-string (nth splitted_array 1))}]
      price)))

(defn get-gpus-from-string [gpu_string]
  (let [splitted_array (str/split gpu_string #" ,")]
    splitted_array))

(def mojAtom2 (atom {:tdp 250 :vram 1024 :price {:price_from 2000 :price_to 10000}}))

(defn create-clostache-friendly-response [results]
  {:results (into [] results) })

(defn find-gpu [price vram psu]  
  (let [myAtom (atom {:price "" :vram "" :tdp ""})]
    (if-not (= (read-string price) 0)
      (swap! myAtom assoc :price (get-prices-from-string price))
      (swap! myAtom assoc :price {:price_from 0 :price_to  java.lang.Integer/MAX_VALUE}))
    (if-not (= vram 0)
      (swap! myAtom assoc :vram (read-string vram))
      (swap! myAtom assoc :vram java.lang.Integer/MIN_VALUE))
    (swap! myAtom assoc :tdp (validate-psu psu))      
    (create-clostache-friendly-response (db/find-gpu-in-db myAtom))))

(defn get-wishlist []
  (create-clostache-friendly-response (db/read-wishlist (session/get! :name))))
;(defn add-to-wishlist [gpu_string]
;  (doseq [wishlist-gpu (get-gpus-from-string gpu_string)]
;    (db/save-to-wishlist wishlist-gpu)))
;(defn get-wishlist []
;  (let [user (session/get! :name)]
    

;(db/search-gpu-db query)))


;(for [x (scrapper/all-gpus)]
;    (if (= (x :type) "Desktop")
;      gpu
;      false)))
