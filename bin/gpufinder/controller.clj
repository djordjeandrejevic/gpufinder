(ns gpufinder.controller
  (:use [db.db :as db])
  (:require  [noir.response :refer [redirect]]   
             [ring.util.response :refer [response]]))

(defn login [username password]
  (let [red (db/auth username password)]
    (if red
      (redirect "http://www.sk.rs")
      (redirect "http://www.blic.rs"))
    ))