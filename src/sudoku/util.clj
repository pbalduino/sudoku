(ns sudoku.util
  (:import [java.lang.Math]))

(defn flattenv 
  "Takes any nested combination of sequential things (lists, vectors,
  etc.) and returns their contents as a single, flat vector.
  (flattenv nil) returns an empty vector."
  [map]
  (vec (flatten map)))

(defn sqrti [x]
  (int (Math/sqrt x)))