(ns mosaic.math-test
  (:require [clojure.test :refer [deftest is testing]]
            [mosaic.math :refer [redmean-distance-sq cielab-distance-sq]]))

(deftest test-redmean-distance
  (testing "Identity distance is zero"
    (let [c (double-array [10.0 20.0 30.0])]
      (is (= 0.0 (redmean-distance-sq c c)))))

  (testing "Distance calculation matches expectation"
    (let [c1 (double-array [0.0 0.0 0.0])
          c2 (double-array [255.0 255.0 255.0])
          d (redmean-distance-sq c1 c2)]
      (is (> d 0.0)))))

(deftest test-cielab-distance
  (testing "Identity distance is zero"
    (let [c (double-array [10.0 20.0 30.0])]
      (is (= 0.0 (cielab-distance-sq c c)))))

  (testing "Distance calculation matches expectation"
    (let [c1 (double-array [0.0 0.0 0.0]) ;; Black
          c2 (double-array [255.0 255.0 255.0]) ;; White
          d (cielab-distance-sq c1 c2)]
      ;; Distance between black and white in LAB space should be 100
      (is (< (Math/abs (- d 10000.0)) 1e-3)))))

(deftest test-find-best-match
  (testing "Matching with Redmean"
    (let [target (double-array [100.0 100.0 100.0])
          tiles [(double-array [101.0 101.0 101.0])
                 (double-array [200.0 200.0 200.0])]
          idx (mosaic.math/find-best-match target tiles redmean-distance-sq)]
      (is (= 0 idx))))

  (testing "Matching with CIELAB"
    (let [target (double-array [100.0 100.0 100.0])
          tiles [(double-array [101.0 101.0 101.0])
                 (double-array [200.0 200.0 200.0])]
          idx (mosaic.math/find-best-match target tiles cielab-distance-sq)]
      (is (= 0 idx)))))

(deftest test-metric-protocol
  (testing "Redmean Metric"
    (let [m (mosaic.math/get-metric "redmean")
          c1 (double-array [0.0 0.0 0.0])
          c2 (double-array [255.0 255.0 255.0])
          p1 (mosaic.math/prepare m c1)
          p2 (mosaic.math/prepare m c2)]
      (is (instance? (Class/forName "[D") p1)) ;; Still a double array
      (is (> (mosaic.math/distance-sq m p1 p2) 0.0))))

  (testing "CIELAB Metric"
    (let [m (mosaic.math/get-metric "cielab")
          c1 (double-array [0.0 0.0 0.0])
          c2 (double-array [255.0 255.0 255.0])
          p1 (mosaic.math/prepare m c1)
          p2 (mosaic.math/prepare m c2)]
      (is (instance? (Class/forName "[D") p1))
      ;; Check that distance calculation on prepared values works
      (is (< (Math/abs (- (mosaic.math/distance-sq m p1 p2) 10000.0)) 1e-3)))))
