(ns gpufinder.views
  (:use clostache.parser
        [hiccup core page])
  (:require [clojure.java.io]
            [noir.session :as session]))

(defn login-page []
  (render-resource "templates/login.html" {:error (session/get :session-message)}))

(defn index-page []
  (if (empty? (session/get :name))
    (render-resource "templates/login.html" {:error (session/get :session-message)})  
    (render-resource "templates/index.html" {:name (session/get :name) :validation-error (session/get :validation-error)})))

(defn register-page []
  (render-resource "templates/register.html" {:error (session/get :session-message)}))

(defn initialize-gpus-page []
  (render-resource "templates/register.html"))

(defn gpu-results-page [results]
  (render-resource "templates/results.html" results))

(defn wishlist-page [results]
  (render-resource "templates/wishlist.html" results))

(defn not-found [] 
  (html5
    [:div#not-found.red "Page Not Found!"]))