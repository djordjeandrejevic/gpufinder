# GPUFinder

GPUFinder is Clojure app that helps you choose the best graphic card that fits your needs. It uses data from GPUBoss.com by parsing their [XML sitemap](http://gpuboss.com/sitemap/gpus.xml) and by scraping HTML data using Enlive library.

## Main features

Users can:

* Login/Register/Logout
* Initialize GPU database
* Search for best GPU using mutliparameter search
* Add/Remove GPU to/from Wishlist

etc.

## Setup

* Download and install Leiningen: http://leiningen.org/
* Download and install MongoDB: https://www.mongodb.org/
* Navigate to project folder and type: 
`lein ring server`

## Usage

After setup, you should be able to register/login and reach index page.

IMPORTANT - before using functionality you need to initialize database. You'll do that by pressing "Initialize GPU database" button in "About" section. Wait for few minutes and after a while you'll see something like [this](http://i.imgur.com/AeOcyCA.png). At this point, database is successfully initialized. This is known 'issue', for some reason Lib Noir redirect after this long POST doesn't work, so simply press Back button on your browser and use search functionality below.

## Libraries used

* [Monger 2.0.1](https://github.com/michaelklishin/monger)
* [Lib-noir 0.9.9](https://github.com/noir-clojure/lib-noir)
* [Ring 1.3.2](https://github.com/ring-clojure/ring)
* [Compojure 1.3.4](https://github.com/weavejester/compojure)
* [Enlive 1.1.5](https://github.com/cgrand/enlive)
* [Clostache 1.4.0](https://github.com/fhd/clostache)
* [Clojure/data.xml 0.0.8](https://github.com/clojure/data.xml)
* [Clojure/data.zip 0.1.1](https://github.com/clojure/data.zip/)
* [Lein-Ring 0.9.1](https://github.com/weavejester/lein-ring)

... with Eclipse Luna IDE and Counterclockwise plugin.

## Literature

Books:

* Practical Clojure
* Web Development with Clojure
* Clojure Programming

Videos:

* [PacktPub] Building Web Applications with Clojure
* [Pluralsight] Clojure Fundamentals
* [Pluralsight] Clojure Concurrency


## License

Copyright © 2015 Đorđe Andrejević

Distributed under the Eclipse Public License, the same as Clojure.