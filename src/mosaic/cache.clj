(ns mosaic.cache
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io])
  (:import [java.io File]))

(defn- get-dir-hash [^File dir]
  (let [files (sort (.listFiles dir))]
    (hash (map #(str (.getName ^File %) (.lastModified ^File %)) files))))

(defn load-cache [^File dir cache-file]
  (if (.exists (io/file cache-file))
    (try
      (let [data (edn/read-string (slurp cache-file))]
        (if (= (:hash data) (get-dir-hash dir))
          (:tiles data)
          nil))
      (catch Exception _ nil))
    nil))

(defn save-cache [^File dir cache-file tiles]
  (let [data {:hash (get-dir-hash dir)
              :tiles tiles}]
    (spit cache-file (pr-str data))))
