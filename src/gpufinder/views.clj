(ns gpufinder.views
  (:use clostache.parser
        [hiccup core page])
  (:require clojure.java.io))

;(defn default-partials 
;  []
;  {:navbar-menu (render-resource "templates/navbar-menu.html") 
;        :header (render-resource "templates/header.html")
;        :footer (render-resource "templates/footer.html")})
;
(defn login-page []
  (render-resource "templates/login.html" ))



;(defn work-page []
;  (render-resource "templates/work.html" {:name "Nikola"} (default-partials)))
;
;
;(defn about-page []
;  (render-resource "templates/about.html" {:name "Nikola"} (default-partials)))
;
;
;(defn contact-page []
;  (render-resource "templates/contact.html" {:name "Nikola"} (default-partials)))


(defn not-found [] 
  (html5
    [:div#not-found.red "Page Not Found!"]))