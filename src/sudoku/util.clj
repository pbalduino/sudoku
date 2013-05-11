(ns sudoku.util
  (:import [java.lang.Math]))

(defn flattenv 
  "Takes any nested combination of sequential things (lists, vectors,
  etc.) and returns their contents as a single, flat vector.
  (flattenv nil) returns an empty vector."
  [map]
  (vec (flatten map)))

(defn f-flattenv [pred l]
  (flattenv
    (filter pred l)))

(defn concatv
  "Returns a vector representing the concatenation of the elements in the supplied colls."
  [& x]
    (let [vec-cat (comp vec concat)]
      (apply vec-cat x)))
  
(defn single? [v]
  (= (count v) 1))

(defn not-single? [v]
  (not (single? v)))