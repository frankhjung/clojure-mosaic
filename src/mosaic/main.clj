(ns mosaic.main
  (:require [clojure.tools.cli :refer [parse-opts]]
            [clojure.string :as str]
            [mosaic.core :as core])
  (:gen-class))

(set! *warn-on-reflection* true)

(def cli-options
  [["-i" "--input INPUT" "Path to the source image file."]
   ["-d" "--directory DIRECTORY" "Directory containing images to use as tiles."]
   ["-o" "--output OUTPUT" "Path where the resulting mosaic will be saved."]
   ["-s" "--size SIZE" "The desired size of the largest dimension of the mosaic."
    :parse-fn #(Integer/parseInt %)]
   ["-t" "--tile TILE" "The width and height of each square tile."
    :parse-fn #(Integer/parseInt %)]
   ["-h" "--help"]])

(defn -main [& args]
  (let [{:keys [options summary errors]} (parse-opts args cli-options)]
    (cond
      (:help options) (println summary)
      errors (binding [*out* *err*]
               (println (str/join "\n" errors))
               (System/exit 1))
      (not-every? #(contains? options %) [:input :directory :output :size :tile])
      (binding [*out* *err*]
        (println "Missing required options.")
        (println summary)
        (System/exit 1))
      :else (try
              (core/generate-mosaic options)
              (catch Exception e
                (binding [*out* *err*]
                  (println "Error:" (.getMessage e)))
                (System/exit 1))))))
