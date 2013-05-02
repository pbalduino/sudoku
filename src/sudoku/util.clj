(ns sudoku.util)

(defn flattenv 
  "Takes any nested combination of sequential things (lists, vectors,
  etc.) and returns their contents as a single, flat vector.
  (flattenv nil) returns an empty vector."
  [map]
  (vec (flatten map)))