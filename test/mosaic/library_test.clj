(ns mosaic.library-test
  (:require [clojure.test :refer [deftest is testing]]
            [mosaic.library :as library])
  (:import [java.awt.image BufferedImage]))

(deftest test-library-lifecycle
  (testing "Library provides metadata and images"
    (let [lib (library/open "test-data/tiles" 50)]
      (try
        (let [meta (library/tile-metadata lib)]
          (is (vector? meta))
          (is (pos? (count meta)))
          (is (map? (first meta)))
          (is (contains? (first meta) :avg))

          (let [img (library/fetch-image lib 0)]
            (is (instance? BufferedImage img))
            (is (= 50 (.getWidth img)))
            (is (= 50 (.getHeight img)))))
        (finally
          (.close ^java.io.Closeable lib)))))

  (testing "Access after close throws IllegalStateException"
    (let [lib (library/open "test-data/tiles" 50)]
      (.close ^java.io.Closeable lib)
      (is (thrown? IllegalStateException (library/tile-metadata lib)))
      (is (thrown? IllegalStateException (library/fetch-image lib 0))))))
