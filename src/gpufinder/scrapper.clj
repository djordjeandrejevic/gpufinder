(ns gpufinder.scrapper
  (require [clojure.zip :as zip]
           [clojure.data.xml :as xml]
           [clojure.data.zip.xml :as xz]
           [clojure.java.io :as io]
           [net.cgrand.enlive-html :as html]))

(def data-url "http://gpuboss.com/sitemap/gpus.xml")

(def data (-> data-url io/reader xml/parse))

(def z (zip/xml-zip data))

(defn all-gpus []
  (mapcat (comp :content zip/node)
          (xz/xml-> z
                    (xz/tag= :url)
                    (xz/tag= :loc))))

(count (mapcat (comp :content zip/node)
               (xz/xml-> z
                         (xz/tag= :url)
                         (xz/tag= :loc))))

(defn fetch-page [url]
  (html/html-resource (java.net.URL. url)))

(defn scrape-data [link]
  (conj (map html/text
             (html/select (fetch-page link) #{[:body :h1 :span.family-model]                                                  
                                              [:#prop_market :td]
                                              [:#side-buy-now :div :div :div :table :tr :td.link :div.price]
                                              [:#overview [:div (html/nth-of-type 2)] :table :tr [:td (html/nth-of-type 1)] :div :div :span.score-text]
                                              [:#prop_tdp :td.value :span :span :span]
                                              [:#prop_memory_size :td.value :span :span :span]})) link))


(def fields [:url :model :rating :type :vram :tdp :price])

(defn order-scraped-data [link] (zipmap fields (scrape-data link)))

(def testiranje (zipmap fields (scrape-data "http://gpuboss.com/graphics-card/GeForce-GTX-960")))

;(def heshmapa (zipmap fields (hn-headlines-three "http://gpuboss.com/graphics-card/GeForce-GTX-960")))

;(defn is-desktop-initial [link]
;  (let [gpu (order-scraped-data link)]
;    (if (= (gpu :type) "Desktop")
;      gpu
;      false)))

;(defn is-desktop [link]
;    (if (= ((order-scraped-data link) :type) "Desktop")
;       true
;       false))

;(defn insert-gpu-into-db [link]
;  (let [gpu (is-desktop-initial link)]
;    (if gpu
;      (mc/insert db "gpus" {:model (gpu :model)})
;      false)))
