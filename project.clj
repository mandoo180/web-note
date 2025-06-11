(defproject web-note "1.0.0"
  :description "Web Based Note Repository"
  :url "http://kyeongsoo.org/"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [ring/ring-core "1.11.0"]
                 [ring/ring-jetty-adapter "1.11.0"]
                 [compojure "1.7.1"]
                 [hiccup "1.0.5"]]
  :main ^:skip-aot web-note.core
  :plugins [[lein-ring "0.12.5"]]
  :ring {:handler web-note.core/app}
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}})
