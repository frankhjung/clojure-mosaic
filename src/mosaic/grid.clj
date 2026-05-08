(ns mosaic.grid)

(set! *warn-on-reflection* true)

(defn- create-cell [gx gy tile-size src-cell-size]
  {:gx gx
   :gy gy
   :px (* gx tile-size)
   :py (* gy tile-size)
   :sx (int (* gx src-cell-size))
   :sy (int (* gy src-cell-size))
   :sw (int src-cell-size)
   :sh (int src-cell-size)})

(defn plan
  "Calculates the geometry of a photo mosaic.
   Returns a map containing grid dimensions and a vector of Cell records."
  [src-w src-h requested-size tile-size]
  (let [scale (/ (double requested-size) (max src-w src-h))
        nx (int (Math/ceil (/ (* src-w scale) tile-size)))
        ny (int (Math/ceil (/ (* src-h scale) tile-size)))
        out-w (* nx tile-size)
        out-h (* ny tile-size)
        src-cell-size (/ (double tile-size) scale)]
    {:nx nx
     :ny ny
     :out-w out-w
     :out-h out-h
     :cells (vec (for [gy (range ny)
                       gx (range nx)]
                   (create-cell gx gy tile-size src-cell-size)))}))
