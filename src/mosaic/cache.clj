(ns mosaic.cache
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io])
  (:import [java.io File]))

(set! *warn-on-reflection* true)

(defn- get-dir-hash [^File dir]
  (let [files (sort (.listFiles dir))]
    (hash (map #(str (.getName ^File %) (.lastModified ^File %)) files))))

(defn load-cache [^File dir cache-file]
  (when (.exists (io/file cache-file))
    (try
      (let [data (edn/read-string (slurp cache-file))]
        (when (= (:hash data) (get-dir-hash dir))
          (:tiles data)))
      (catch Exception _ nil))))

(defn save-cache [^File dir cache-file tiles]
  (let [data {:hash (get-dir-hash dir)
              :tiles tiles}]
    (spit cache-file (pr-str data))))
