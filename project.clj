(defproject balrog "0.1.0-SNAPSHOT"
  :description "TODO: Make this pretty!"
  :source-path "src/clj"
  :dependencies [[org.clojure/clojure "1.3.0"]
                 [noir "1.2.1"]
                 [org.clojure/data.json "0.1.1"]
                 [clj-redis "0.0.12"]]
  :dev-dependencies [[lein-ring "0.4.6"]]
  :ring {:handler balrog.server/handler}
  :main balrog.server)
