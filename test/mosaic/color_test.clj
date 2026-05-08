(ns mosaic.color-test
  (:require [clojure.test :refer [deftest is testing]]
            [mosaic.color :as color]))

(deftest test-srgb->xyz
  (testing "Black conversion"
    (let [rgb (double-array [0.0 0.0 0.0]) ;; BGR
          xyz (color/srgb->xyz rgb)]
      (is (< (aget xyz 0) 1e-5))
      (is (< (aget xyz 1) 1e-5))
      (is (< (aget xyz 2) 1e-5))))

  (testing "White conversion (D65)"
    (let [rgb (double-array [255.0 255.0 255.0]) ;; BGR
          xyz (color/srgb->xyz rgb)]
      ;; D65 White point: X=95.047, Y=100.0, Z=108.883
      (is (< (Math/abs (- (aget xyz 0) 95.047)) 0.1))
      (is (< (Math/abs (- (aget xyz 1) 100.0)) 0.1))
      (is (< (Math/abs (- (aget xyz 2) 108.883)) 0.1))))

  (testing "XYZ to LAB conversion (White point D65)"
    (let [xyz (double-array [95.047 100.0 108.883])
          lab (color/xyz->lab xyz)]
      ;; LAB: L=100.0, a=0.0, b=0.0
      (is (< (Math/abs (- (aget lab 0) 100.0)) 0.1))
      (is (< (Math/abs (aget lab 1)) 0.1))
      (is (< (Math/abs (aget lab 2)) 0.1)))))
