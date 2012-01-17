(ns balrog.views.api
  "This namespace surfaces a RESTful API for map tiles."
  (:use [noir.core :only (defpage)]
        [noir.options :only (dev-mode?)]
        [clojure.data.json :only (json-str write-json read-json)]
        [hiccup.page-helpers :only (include-js javascript-tag)])
  (:require [clojure.data.json]
            [clojure.java.io :as io]
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

(def example-tile
  "Returns raw tile bytes from an  example tile located in dev/tile.png."
  (let [ret (byte-array 100000)]
    (with-open [rdr (io/input-stream (io/resource "tile.png"))]
      (.read rdr ret)
      ret)))

(defn edb-get
  "Returns a tile from EDB given a key vector."
  [x y z]
  ;; TODO
  example-tile)

(defn get-tile-bytes
  "Returns raw bytes for a tile given a key vector."
  [x y z]
  (let [tile-bytes (edb-get x y z)]
    tile-bytes))

(defn bytes->string
  "Encodes bytes to Base64 and returns them as a UTF-8 encoded string."
  [bytes]
  (let [barr (Base64/encodeBase64 bytes)]
    (String. barr "utf-8")))

(defn string->bytes
  "Decodes a UTF-8 encoded string and returns its bytes encoded in Base64."
  [string]
  (Base64/decodeBase64 (.getBytes string "utf-8")))

(defn get-tile-string
  "Gets a UTF-8 encoded string representing the Base64 encoded bytes of a tile."
  [x y z]
  (bytes->string (get-tile-bytes x y z)))

(defn png-wrapper
  "Wraps a tile stream into a image/png response."
  [tile-stream]
  (response/content-type "image/png" tile-stream))

(defn get-tile-stream
  "Returns a ByteArrayInputStream that wraps some tile bytes."
  [tile-bytes]
  (-> tile-bytes (java.io.ByteArrayInputStream.)))

(defpage "/api/tiles/:x/:y/:z.png"
  {:keys [x y z]}
  "API for getting a tile at position x/y at zoom level z."
  (let [key (format "tile-%s-%s-%s" x y z)
        tile-string (cache-get key)]
    (if tile-string
      (png-wrapper (get-tile-stream (string->bytes tile-string)))
      (let [tile-string (get-tile-string x y z)]
        (cache-put key tile-string)
        (png-wrapper (get-tile-stream (string->bytes tile-string)))))))
