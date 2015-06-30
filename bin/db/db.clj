(ns db.db
  (:require [monger.core :as mg]
            [monger.collection :as mc]
            [monger.conversion :as mconv]
            [monger.operators :refer :all])
  (:import (org.bson.types ObjectId)))

(def connection (mg/connect))

(def db (mg/get-db connection "gpufinder_db"))

(defn get-all [table]
  (mc/find-maps db table))

(defn insert-admin []
  (mc/insert db "users" {:id (inc 1) :username "admin" :password "admin" }))

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

(defn insert-gpu [gpu]
  (mc/insert db "gpus" {:url (gpu :url) :model (gpu :model) :rating (gpu :rating) :type (gpu :type) :vram (gpu :vram) :tdp (gpu :tdp) :price (gpu :price)}))

(defn find-gpu-in-db [myAtom]
  (let [tdp (@myAtom :tdp)
        vram (@myAtom :vram)
        price (@myAtom :price)]
    (mc/find-maps db "gpus" {:tdp {$lt tdp} :vram {$gt vram} :price {$lt (price :price_to) $gt (price :price_from)}})))

(defn find-gpu-by-name [gpu-name]
  (mc/find-one-as-map db "gpus" {:model gpu-name}))

(defn read-wishlist [username]
    (mc/find-maps db "users" {:username username} ["gpus"]))

(defn save-to-wishlist [gpu username]
    (mc/update db "users" {:username username} {$addToSet {:gpus (find-gpu-by-name gpu)}}))

(defn remove-from-wishlist [gpu-id username]
    (mc/update db "users" {:username username} {$pull {:gpus {:_id (ObjectId. gpu-id)}}}))