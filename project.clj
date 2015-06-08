(defproject gpufinder "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]                 
                 [com.novemberain/monger "2.0.1"]
                 [lib-noir "0.9.9"]
                 [ring "1.3.2"]
                 [compojure "1.3.4"]
                 [enlive "1.1.5"]
                 [de.ubercode.clostache/clostache "1.4.0"]
                 [org.clojure/data.xml "0.0.8"]
                 [org.clojure/data.zip "0.1.1"]]
  :plugins [[lein-ring "0.9.1"]]
  :ring {:init gpufinder.controller/initialize-gpus                  
         :handler gpufinder.core/app})