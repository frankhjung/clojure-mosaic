(ns mosaic.image
  (:import [java.awt Graphics2D Color RenderingHints]
           [java.awt.image BufferedImage]
           [javax.imageio ImageIO]
           [java.io File]))

(set! *warn-on-reflection* true)

(defn load-image [^File file]
  (ImageIO/read file))

(defn save-image [^BufferedImage image ^File file ^String format]
  (ImageIO/write image format file))

(defn get-dominant-color
  "Calculates dominant color using RMS, matching the Python implementation.
   Returns a double-array [B G R]."
  [^BufferedImage image]
  (let [w (.getWidth image)
        h (.getHeight image)
        pixels ^ints (.getRGB image 0 0 w h nil 0 w)
        len (alength pixels)]
    (loop [idx 0
           sum-r-sq 0.0
           sum-g-sq 0.0
           sum-b-sq 0.0]
      (if (< idx len)
        (let [p (aget pixels idx)
              r (bit-and (bit-shift-right p 16) 0xff)
              g (bit-and (bit-shift-right p 8) 0xff)
              b (bit-and p 0xff)]
          (recur (inc idx)
                 (+ sum-r-sq (* r r))
                 (+ sum-g-sq (* g g))
                 (+ sum-b-sq (* b b))))
        (double-array [(Math/sqrt (/ sum-b-sq len))
                       (Math/sqrt (/ sum-g-sq len))
                       (Math/sqrt (/ sum-r-sq len))])))))

(defn get-average-color
  "Calculates simple average BGR color.
   Returns a double-array [B G R]."
  [^BufferedImage image]
  (let [w (.getWidth image)
        h (.getHeight image)
        pixels ^ints (.getRGB image 0 0 w h nil 0 w)
        len (alength pixels)]
    (loop [idx 0
           sum-r 0.0
           sum-g 0.0
           sum-b 0.0]
      (if (< idx len)
        (let [p (aget pixels idx)
              r (bit-and (bit-shift-right p 16) 0xff)
              g (bit-and (bit-shift-right p 8) 0xff)
              b (bit-and p 0xff)]
          (recur (inc idx)
                 (+ sum-r r)
                 (+ sum-g g)
                 (+ sum-b b)))
        (double-array [(/ sum-b len)
                       (/ sum-g len)
                       (/ sum-r len)])))))

(defn resize-and-pad
  "Resizes image to fit target-size while maintaining aspect ratio,
   and pads with dominant color."
  [^BufferedImage image target-size]
  (let [ts (int target-size)
        w (.getWidth image)
        h (.getHeight image)
        scale (/ (double ts) (max w h))
        new-w (int (* w scale))
        new-h (int (* h scale))
        dominant ^doubles (get-dominant-color image)
        bg-color (Color. (int (aget dominant 2))
                         (int (aget dominant 1))
                         (int (aget dominant 0)))
        res (BufferedImage. ts ts BufferedImage/TYPE_INT_RGB)
        ^Graphics2D g (.createGraphics res)]
    (doto g
      (.setColor bg-color)
      (.fillRect 0 0 ts ts)
      (.setRenderingHint RenderingHints/KEY_INTERPOLATION
                         RenderingHints/VALUE_INTERPOLATION_BICUBIC)
      (.drawImage image (int (/ (- ts new-w) 2))
                  (int (/ (- ts new-h) 2))
                  new-w new-h nil)
      (.dispose))
    res))
