(ns gpufinder.controller
  (:use [db.db :as db])
  (:require  [noir.response :refer [redirect]]
             [noir.session :as session]
             [ring.util.response :refer [response]]))

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

