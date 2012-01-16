(ns balrog.views.api
  "This namespace surfaces a RESTful API for map tiles."
  (:use [noir.core :only (defpage)]
        [noir.options :only (dev-mode?)]
        [clojure.data.json :only (json-str write-json read-json)]
        [hiccup.page-helpers :only (include-js javascript-tag)])
  (:require clojure.data.json
            [clj-redis.client :as redis]
            [noir.response :as response]
            [balrog.views.common :as common]))
 
(defpage "/api" []
  "The API landing page that provides some links and information about the API."
  (common/layout 
   (include-js "cljs/bootstrap.js")
   (when (dev-mode?)
     (javascript-tag "goog.require('forma-api.repl');"))
   (javascript-tag "goog.require('forma-api.mainview')")
   [:div.written
    [:p "Balrog API"]
    [:img {:src "http://goo.gl/YjU3W"}]]))

(def db
  "If REDISTOGO_URL isn't defined, redis will default to local mode."
  (redis/init {:url (System/getenv "REDISTOGO_URL")}))

(defn cache-get
  "Gets the value for a key from the cache or nil if the key doesn't exist."
  [key]
  (redis/get db key))

(defn cache-put
  "Puts a key/value into the cache."
  [key value]
  (redis/set db key value))

(defn get-tile
  "Return a PNG tile from the FS."
  []
  {:tile "TODO"})

(defpage [:get "/api/tiles/:x/:y/:z.png"]
"Returns a map tile."
  {:keys [x y z]}  
  (let [key (format "tile-%s-%s-%s" x y z)
        content (cache-get key)]
    (if content
      content
      (let [data (get-tile)
            content (json-str data)]
        (cache-put key content)
        content))))








