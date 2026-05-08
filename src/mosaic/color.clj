(ns mosaic.color)

(set! *warn-on-reflection* true)

(defn- pivot-rgb ^double [^double n]
  (let [v (/ n 255.0)]
    (if (> v 0.04045)
      (Math/pow (/ (+ v 0.055) 1.055) 2.4)
      (/ v 12.92))))

(defn srgb->xyz
  "Converts BGR sRGB to XYZ (D65).
   Expects a double array [B G R]."
  ^doubles [^doubles bgr]
  (let [b (pivot-rgb (aget bgr 0))
        g (pivot-rgb (aget bgr 1))
        r (pivot-rgb (aget bgr 2))
        ;; D65 sRGB to XYZ matrix
        x (+ (* r 0.4124) (* g 0.3576) (* b 0.1805))
        y (+ (* r 0.2126) (* g 0.7152) (* b 0.0722))
        z (+ (* r 0.0193) (* g 0.1192) (* b 0.9505))]
    (double-array [(* x 100.0) (* y 100.0) (* z 100.0)])))

(defn- pivot-xyz ^double [^double n]
  (if (> n 0.008856)
    (Math/pow n 0.3333333333333333)
    (+ (* 7.787 n) 0.13793103448275862)))

(defn xyz->lab
  "Converts XYZ to CIELAB (D65).
   Expects a double array [X Y Z]."
  ^doubles [^doubles xyz]
  (let [x (pivot-xyz (/ (aget xyz 0) 95.047))
        y (pivot-xyz (/ (aget xyz 1) 100.0))
        z (pivot-xyz (/ (aget xyz 2) 108.883))
        l (- (* 116.0 y) 16.0)
        a (* 500.0 (- x y))
        b (* 200.0 (- y z))]
    (double-array [l a b])))
