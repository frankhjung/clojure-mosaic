(ns mosaic.grid-test
  (:require [clojure.test :refer [deftest is testing]]
            [mosaic.grid :as grid]))

(deftest test-grid-plan
  (testing "Grid dimensions and snapping"
    (let [src-w 640
          src-h 512
          requested-size 1000
          tile-size 50
          plan (grid/plan src-w src-h requested-size tile-size)]
      ;; Aspect ratio is 5:4. Requested max dimension is 1000.
      ;; Scale is 1000 / 640 = 1.5625.
      ;; Theoretical height is 512 * 1.5625 = 800.
      ;; nx = ceil(1000 / 50) = 20
      ;; ny = ceil(800 / 50) = 16
      ;; out-w = 20 * 50 = 1000
      ;; out-h = 16 * 50 = 800
      (is (= 20 (:nx plan)))
      (is (= 16 (:ny plan)))
      (is (= 1000 (:out-w plan)))
      (is (= 800 (:out-h plan)))

      (testing "Cell sequence"
        (let [cells (:cells plan)]
          (is (= (* 20 16) (count cells)))
          (let [first-cell (first cells)
                last-cell (last cells)]
            ;; First cell
            (is (= 0 (:gx first-cell)))
            (is (= 0 (:gy first-cell)))
            (is (= 0 (:px first-cell)))
            (is (= 0 (:py first-cell)))

            ;; Last cell
            (is (= 19 (:gx last-cell)))
            (is (= 15 (:gy last-cell)))
            (is (= (* 19 50) (:px last-cell)))
            (is (= (* 15 50) (:py last-cell))))))))

  (testing "Source coordinates for sampling"
    (let [plan (grid/plan 640 512 1000 50)
          first-cell (first (:cells plan))]
      ;; Cell covers 50x50 in output. 
      ;; Scale is 1.5625 output pixels per source pixel.
      ;; Source cell size = 50 / 1.5625 = 32 pixels.
      (is (= 0 (:sx first-cell)))
      (is (= 0 (:sy first-cell)))
      (is (= 32 (:sw first-cell)))
      (is (= 32 (:sh first-cell))))))
