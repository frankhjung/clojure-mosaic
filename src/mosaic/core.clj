(ns mosaic.core
  (:require [mosaic.image :as image]
            [mosaic.math :as math]
            [mosaic.cache :as cache]
            [clojure.java.io :as io])
  (:import [java.awt Graphics2D]
           [java.awt.image BufferedImage]
           [java.io File]))

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

(defn load-tiles [dir-path tile-size cache-file]
  (let [dir (io/file dir-path)]
    (or (cache/load-cache dir cache-file)
        (let [files (filter #(.isFile ^File %) (.listFiles dir))
              tiles (remove nil? (pmap #(process-tile % tile-size) files))]
          (cache/save-cache dir cache-file (vec tiles))
          tiles))))

(defn- get-tile-from-cache [cache-atom path tile-size]
  (if-let [img (get @cache-atom path)]
    img
    (let [img (image/resize-and-pad (image/load-image (io/file path)) tile-size)]
      (swap! cache-atom assoc path img)
      img)))

(defn generate-mosaic [{:keys [input directory output size tile]}]
  (let [base-img (image/load-image (io/file input))
        _ (println "Loading tiles...")
        tiles (load-tiles directory tile ".mosaic-cache")
        _ (when (empty? tiles) (throw (Exception. "No valid tiles found.")))

        tile-colors (map #(double-array (:avg %)) tiles)

        input-w (.getWidth base-img)
        input-h (.getHeight base-img)
        scale (/ (double size) (max input-w input-h))

        nx (int (Math/ceil (/ (* input-w scale) tile)))
        ny (int (Math/ceil (/ (* input-h scale) tile)))

        out-w (* nx tile)
        out-h (* ny tile)

        _ (println (format "Grid: %dx%d, Output: %dx%d" nx ny out-w out-h))

        ;; Resize input image to grid size to get target colors
        input-small (BufferedImage. nx ny BufferedImage/TYPE_INT_RGB)
        g-small ^Graphics2D (.createGraphics input-small)
        _ (doto g-small
            (.drawImage base-img 0 0 nx ny nil)
            (.dispose))

        target-colors (for [y (range ny) x (range nx)]
                        (image/get-average-color
                         (.getSubimage input-small x y 1 1)))

        ;; Match tiles
        best-matches (pmap #(math/find-best-match % tile-colors) target-colors)

        ;; Assemble mosaic
        res (BufferedImage. out-w out-h BufferedImage/TYPE_INT_RGB)
        g ^Graphics2D (.createGraphics res)
        tile-cache (atom {})]

    (println "Assembling mosaic...")
    (doseq [[idx match-idx] (map-indexed vector best-matches)]
      (let [x (mod idx nx)
            y (quot idx nx)
            tile-path (:path (nth tiles match-idx))
            tile-img (get-tile-from-cache tile-cache tile-path tile)]
        (.drawImage g tile-img (* x tile) (* y tile) nil)))

    (.dispose g)
    (image/save-image res (io/file output) "jpg")
    (println "Mosaic saved to" output)))
