(ns mosaic.math
  (:require [mosaic.color :as color]))

(set! *warn-on-reflection* true)

(defn redmean-distance-sq
  "Calculates the squared Redmean distance between two BGR colours.
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

(defn cielab-distance-sq
  "Calculates the squared Euclidean distance between two colours in CIELAB space.
   Expects two primitive double arrays of shape [B G R]."
  ^double [^doubles c1 ^doubles c2]
  (let [lab1 (color/xyz->lab (color/srgb->xyz c1))
        lab2 (color/xyz->lab (color/srgb->xyz c2))
        dl (- (aget lab1 0) (aget lab2 0))
        da (- (aget lab1 1) (aget lab2 1))
        db (- (aget lab1 2) (aget lab2 2))]
    (+ (* dl dl) (* da da) (* db db))))

(defn find-best-match
  "Finds the index of the tile with the minimum distance to the target colour.
   target-colour: [B G R] double array
   tile-colors: sequence of [B G R] double arrays
   dist-fn: function [^doubles ^doubles] -> double"
  [target-colour tile-colors dist-fn]
  (let [distances (map-indexed (fn [idx tc]
                                 [idx (dist-fn target-colour tc)])
                               tile-colors)]
    (first (apply min-key second distances))))
