(defproject mosaic "0.1.0"
  :description "Clojure photo mosaic generator"
  :url "https://gitlab.com/frankhjung1/clojure-mosaic"
  :license {:name "MIT License"
            :url "https://spdx.org/licenses/MIT.html"}
  :dependencies [[org.clojure/clojure "1.12.4"]
                 [org.clojure/tools.cli "1.3.250"]]
  :plugins [[lein-eftest "0.6.0"]
            [lein-ancient "0.7.0"]]
  :main ^:skip-aot mosaic.main
  :target-path "target/%s"
  :profiles {:dev {:aliases {"build" ["do" "check," "eftest," "run"]}
                   :plugins [[lein-cljfmt "0.6.8"]
                             [com.github.clj-kondo/lein-clj-kondo "2026.04.15"]]}
             :cicd {:local-repo ".m2/repository"}
             :uberjar {:aot :all
                       :jvm-opts ["-Dclojure.compiler.direct-linking=true"]}}
  :clean-targets [:target-path]
  :aliases {"build" ["do" "check," "eftest," "run"]})
