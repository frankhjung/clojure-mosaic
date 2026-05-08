(ns mosaic.core
  (:require [mosaic.image :as image]
            [mosaic.math :as math]
            [mosaic.library :as library]
            [mosaic.grid :as grid]
            [clojure.java.io :as io])
  (:import [java.awt Graphics2D]
           [java.awt.image BufferedImage]
           [java.io Closeable]))

(set! *warn-on-reflection* true)

(defn generate-mosaic [{:keys [input directory output size tile metric]}]
  (let [^BufferedImage base-img (image/load-image (io/file input))
        grid-plan (grid/plan (.getWidth base-img) (.getHeight base-img) size tile)
        {:keys [nx ny out-w out-h cells]} grid-plan]

    (println "Loading tiles...")
    (with-open [lib ^Closeable (library/open directory tile)]
      (let [tiles (library/tile-metadata lib)
            _ (when (empty? tiles) (throw (ex-info "No valid tiles found." {})))

            _ (println (format "Grid: %dx%d, Output: %dx%d" nx ny out-w out-h))
            _ (println "Metric:" metric)

            metric-adapter (math/get-metric metric)

            ;; Prepare tile colors once
            tile-colors (mapv #(math/prepare metric-adapter (double-array (:avg %)))
                              tiles)

            ;; Resize input image to grid size to get target colors
            input-small (BufferedImage. nx ny BufferedImage/TYPE_INT_RGB)
            g-small ^Graphics2D (.createGraphics input-small)
            _ (doto g-small
                (.drawImage base-img 0 0 nx ny nil)
                (.dispose))

            target-bgrs (mapv (fn [{:keys [gx gy]}]
                                (image/get-average-color
                                 (.getSubimage input-small gx gy 1 1)))
                              cells)

            ;; Prepare target colors once
            target-colors (mapv #(math/prepare metric-adapter %) target-bgrs)

            ;; Match tiles
            _ (println "Matching tiles...")
            dist-fn (partial math/distance-sq metric-adapter)
            best-matches (pmap #(math/find-best-match % tile-colors dist-fn)
                               target-colors)

            ;; Assemble mosaic
            res (BufferedImage. out-w out-h BufferedImage/TYPE_INT_RGB)
            g ^Graphics2D (.createGraphics res)]

        (println "Assembling mosaic...")
        (doseq [[cell match-idx] (map vector cells best-matches)]
          (let [{:keys [px py]} cell
                ^BufferedImage tile-img (library/fetch-image lib match-idx)]
            (.drawImage g tile-img (int px) (int py) nil)))

        (.dispose g)
        (image/save-image res (io/file output) "jpg")
        (println "Mosaic saved to" output)))))
