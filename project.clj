(defproject balrog "0.1.0-SNAPSHOT"
  :description "TODO: Make this pretty!"
  :source-path "src/clj"
  :dev-resources-path "dev"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [noir "1.2.1"]
                 [org.clojure/data.json "0.1.1"]
                 [org.clojure/data.codec "0.1.0"]
                 [clj-redis "0.0.12"]]
  :dev-dependencies [[lein-ring "0.4.6"]
                     [commons-codec "1.4"]]
  :ring {:handler balrog.server/handler}
  :main balrog.server)
