(defproject groove-api "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 ; web
                 [prismatic/schema "1.1.9"]
                 [metosin/compojure-api "2.0.0-alpha26"]
                 [ring/ring-jetty-adapter "1.6.3"]
                 [jumblerg/ring-cors "2.0.0"]
                 [org.clojure/data.json "0.2.7"]
                 [ring/ring-json "0.5.0"]]
  :main ^:skip-aot groove-api.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
