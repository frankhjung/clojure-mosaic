(ns mosaic.math)

(set! *warn-on-reflection* true)

(defn redmean-distance-sq
  "Calculates the squared Redmean distance between two BGR colors.
   Expects two primitive double arrays of shape [B G R]."
  ^double [^doubles c1 ^doubles c2]
  (let [b1 (aget c1 0) g1 (aget c1 1) r1 (aget c1 2)
        b2 (aget c2 0) g2 (aget c2 1) r2 (aget c2 2)
        rmean (/ (+ r1 r2) 2.0)
        dr (- r1 r2)
        dg (- g1 g2)
        db (- b1 b2)
        wr (+ 2.0 (/ rmean 256.0))
        wg 4.0
        wb (+ 2.0 (/ (- 255.0 rmean) 256.0))]
    (+ (* wr dr dr)
       (* wg dg dg)
       (* wb db db))))

(defn find-best-match
  "Finds the index of the tile with the minimum Redmean distance to the target color.
   target-color: [B G R] double array
   tile-colors: sequence of [B G R] double arrays"
  [target-color tile-colors]
  (let [distances (map-indexed (fn [idx tc]
                                 [idx (redmean-distance-sq target-color tc)])
                               tile-colors)]
    (first (apply min-key second distances))))
