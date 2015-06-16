(ns db.db
  (:require [monger.core :as mg]
            [monger.collection :as mc])
  (:import (org.bson.types ObjectId)))

(def connection (mg/connect))

(def db (mg/get-db connection "gpufinder_db"))

(defn get-all [table]
  (mc/find-maps db table))

(defn insert-admin []
  (mc/insert db "users" {:id (inc 1) :username "admin" :password "admin" }))

(defn test-insert []
  (mc/insert db "gpus" {:id (inc 1) :model "GTX 960"}))

(defn test-user []
  (mc/insert db "users" {:id (inc 1) :username "test" :password "test" }))



(defn initialize-users []
  connection
  (do
    (if (empty? (get-all "users"))
      (insert-admin)
      false)))

(defn auth [username password]
  (let [existing-username (mc/find-maps db "users" {:username username}) existing-password (mc/find-maps db "users" {:password password})]    
    (or (empty? existing-username) (empty? existing-password))))

(defn reg [username password]
  (if (empty? (mc/find-maps db "users" {:username username}))
    (do
      (mc/insert db "users" {:id (inc 1) :username username :password password})
      true)
    false))

;(defn insert-gpu [gpu]
;  (mc/insert db "gpus" {:url (gpu :url) :model (gpu :model) :rating (gpu :rating) :type (gpu :type) :vram (gpu :vram) :tdp (gpu :tdp) :price (gpu :price)}))


(defn insert-gpu [gpu]
  (mc/insert db "gpus" {:url (gpu :url) :model (gpu :model) :rating (gpu :rating) :type (gpu :type) :vram (gpu :vram) :tdp (gpu :tdp) :price (gpu :price)}))

;(defn search-gpu-db [myAtom])

(defn find-gpu-in-db [myAtom]
  (mc/find-maps db "gpus" {:tdp {(@myAtom :tdp)} :vram {"$lt" (@myAtom :vram)} :price {(@myAtom :price)}}))