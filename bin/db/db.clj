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

(defn initialize []
  connection
  (if (empty? (get-all "users"))
    (insert-admin)))

(defn auth [username password]
  (let [existing-username (mc/find-maps db "users" {:username username}) existing-password (mc/find-maps db "users" {:password password})]    
    (or (empty? existing-username) (empty? existing-password))))

(defn reg [username password]
  (if (empty? (mc/find-maps db "users" {:username username}))
    (do
      (mc/insert db "users" {:id (inc 1) :username username :password password})
      true)
    false))

