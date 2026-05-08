(ns mosaic.library
  (:require [mosaic.image :as image]
            [clojure.java.io :as io]
            [clojure.edn :as edn])
  (:import [java.awt.image BufferedImage]
           [java.io File Closeable]))

(set! *warn-on-reflection* true)

(defprotocol TileLibrary
  (tile-metadata [this] "Returns vector of tile metadata.")
  (fetch-image ^BufferedImage [this index] "Returns pre-processed BufferedImage."))

(defn- get-combined-hash [^File dir tile-size]
  (let [files (sort (.listFiles dir))]
    (hash [(map #(str (.getName ^File %) (.lastModified ^File %)) files)
           tile-size])))

(defn- process-tile [^File file tile-size]
  (try
    (let [img (image/load-image file)
          processed (image/resize-and-pad img tile-size)
          avg (image/get-average-color processed)]
      {:path (.getAbsolutePath file)
       :avg (vec avg)})
    (catch Exception e
      (println "Error processing" (.getName file) ":" (.getMessage e))
      nil)))

(defrecord ManagedLibrary [dir tile-size tiles image-cache closed? max-cache-size]
  TileLibrary
  (tile-metadata [_this]
    (when @closed? (throw (IllegalStateException. "Library is closed")))
    tiles)

  (fetch-image [_this index]
    (when @closed? (throw (IllegalStateException. "Library is closed")))
    (let [path (:path (nth tiles index))]
      (if-let [img (get @image-cache path)]
        img
        (let [img (image/resize-and-pad (image/load-image (io/file path)) tile-size)]
          (swap! image-cache (fn [c]
                               (let [new-c (assoc c path img)]
                                 (if (> (count new-c) max-cache-size)
                                   ;; Simple strategy: clear oldest entry (not really LRU, but bounded)
                                   (dissoc new-c (first (keys new-c)))
                                   new-c))))
          img))))

  Closeable
  (close [{:keys [closed? image-cache]}]
    (reset! closed? true)
    (reset! image-cache {})))

(defn open
  "Opens a TileLibrary for the given directory and tile-size.
   Manages persistent caching internally."
  ([dir-path tile-size] (open dir-path tile-size {}))
  ([dir-path tile-size opts]
   (let [{:keys [cache-file cache-size]
          :or {cache-file ".mosaic-cache"
               cache-size 500}} opts
         dir (io/file dir-path)
         expected-hash (get-combined-hash dir tile-size)
         cache-f (io/file cache-file)
         data (when (.exists cache-f)
                (try (edn/read-string (slurp cache-f)) (catch Exception _ nil)))
         tiles (if (= (:hash data) expected-hash)
                 (:tiles data)
                 (let [files (filter #(.isFile ^File %) (.listFiles dir))
                       processed (vec (keep identity (pmap #(process-tile % tile-size) files)))]
                   (spit cache-file (pr-str {:hash expected-hash :tiles processed}))
                   processed))]
     (->ManagedLibrary dir tile-size tiles (atom {}) (atom false) cache-size))))

