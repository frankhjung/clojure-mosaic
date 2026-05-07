(ns mosaic.math-test
  (:require [clojure.test :refer [deftest is testing]]
            [mosaic.math :refer [redmean-distance-sq]]))

(deftest test-redmean-distance
  (testing "Identity distance is zero"
    (let [c (double-array [10.0 20.0 30.0])]
      (is (= 0.0 (redmean-distance-sq c c)))))

  (testing "Distance calculation matches expectation"
    (let [c1 (double-array [0.0 0.0 0.0])
          c2 (double-array [255.0 255.0 255.0])
          d (redmean-distance-sq c1 c2)]
      (is (> d 0.0)))))
