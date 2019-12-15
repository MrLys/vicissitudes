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
                 [ring/ring-json "0.5.0"]
                 [ring "1.7.1"]
                 [ring/ring-defaults "0.3.2"]
                 [ring/ring-anti-forgery "1.2.0"]
                 [clj-http "3.10.0"]
                 [org.clojars.nakkaya.javax.mail/mail "1.4.3"]
                 [toucan "1.1.9"]
                 [clj-time "0.15.2"]
                 [metosin/schema-tools "0.12.0"]
                 [org.postgresql/postgresql "42.2.4"]
                 [buddy/buddy-hashers "1.4.0"]
                 [buddy/buddy-auth "2.2.0"]
                 [ring/ring-ssl "0.3.0"]
                 [environ "1.1.0"]]
  :plugins [[lein-environ "1.1.0"]]
  :main ^:skip-aot groove-api.core
  :target-path "target/%s"
  :profiles { :dev [:project/dev :profiles/dev]
	     :test [:project/test :profiles/test]
	     ;; only edit :profiles/* in profiles.clj
	     :profiles/dev  {}
	     :profiles/test {}
	     :project/dev {:source-paths ["src" "tool-src"]
			   :dependencies [[midje "1.6.3"]]
			   :plugins [[lein-auto "0.1.3"]]}
	     :project/test {}
	     :uberjar {:aot :all}})
