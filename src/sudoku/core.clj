(ns sudoku.core
  (:use [sudoku.pprint]
        [sudoku.util]))

(def one-nine [1 2 3 4 5 6 7 8 9])

(defn- list->candidates [numbers]
  (mapv #(if (zero? %) one-nine [%]) numbers))

(defn- candidates->list [numbers]
  (mapv #(if (single? %) (first %) 0) numbers))

(defn- solved? [candidates]
  (not-any? not-single? candidates))

(defn- get-line [candidates line]
  (subvec candidates (* line 9) (* (inc line) 9)))

(defn- remove-value [x list]
  (vec
    (if-not (single? list)
            (filter #(not= x %) list)
            list)))

(defn insert [part whole start]
  (let [part-size  (count part)
        whole-size (count whole)
        begin      (if (zero? start)
                       []
                       (subvec whole 0 start))
        end        (subvec whole (+ start part-size))]
    (flattenv (conj begin part end))))

(defn- break-lines [board]
  (loop [line-number 0
         result      []]
    (if (> line-number 8)
        result
        (recur (inc line-number) (conj result (get-line board line-number))))))

(defn clear-line [line]
  (loop [solved (flatten (filter single? line))
         line   line]
    (if (empty? solved)
      line
      (recur (rest solved) (mapv #(remove-value (first solved) %) line)))))

(defn eliminate-alone [line]
  (loop [alone (flattenv (filterv #(= (count %) 1) (vals (group-by identity (flattenv (filterv #(> (count %) 1) line))))))
         line  line]
    (if (empty? alone)
         line
         (recur (rest alone) 
                (map (fn[x] (if (some #(= (first alone) %) x) [(first alone)] x)) line)))))

(defn join-lines [lines]
  (loop [lines lines
         result []]
    (if (empty? lines)
        result
        (recur (rest lines) 
               (loop [lines (first lines)
                      result result]
                 (if (empty? lines)
                      result
                      (recur (rest lines) 
                             (conj result (first lines)))))))))

(defn rotate-left [candidates]
  (loop [line (range 0 9) 
         result []]
    (if (empty? line)
        result
        (recur (rest line)
               (loop [col (range 0 9)
                      result result]
                  (if (empty? col)
                      result
                      (recur (rest col)
                             (conj result 
                                   (nth candidates 
                                        (- (-> col 
                                               first 
                                               inc 
                                               (* 9)) 
                                            (-> line 
                                                first 
                                                inc)))))))))))

(defn rotate-right [candidates]
  (rotate-left 
    (rotate-left
      (rotate-left candidates))))

(defn subvec-2 [candidates]
  #(subvec candidates % (+ % 3)))

; ugh
(defn group-cells-as-lines [candidates]
  (let [part (subvec-2 candidates)]
    (concatv
      (part 0) (part 9) (part 18)
      (part 3) (part 12) (part 21)
      (part 6) (part 15) (part 24)

      (part 27) (part 36) (part 45) 
      (part 30) (part 39) (part 48) 
      (part 33) (part 42) (part 51) 

      (part 54) (part 63) (part 72)
      (part 57) (part 66) (part 75)
      (part 60) (part 69) (part 78))))

(defn group-lines-as-cells [candidates]
  (group-cells-as-lines candidates))

(defn eliminate-lines [candidates]
  (let [line-transformation (comp clear-line)]
    (join-lines
      (map line-transformation
        (break-lines candidates)))))

(defn eliminate-cols [candidates]
  (rotate-right
    (eliminate-lines 
      (rotate-left candidates))))

(defn eliminate-cells [candidates]
  (group-lines-as-cells 
    (eliminate-lines
      (group-cells-as-lines candidates))))

(defn -main []
  (let [board [6 4 0 7 1 0 0 0 0
               0 7 3 8 0 0 0 0 0
               0 0 2 5 0 9 7 4 0
               0 5 0 0 8 0 0 0 0
               0 0 1 0 0 0 8 0 0
               0 0 0 0 4 0 0 1 0
               0 1 9 6 0 2 5 0 0
               0 0 0 0 0 8 1 2 0
               0 0 0 0 9 1 0 6 7]
        candidates (list->candidates board)
        solve      (comp eliminate-cells eliminate-cols eliminate-lines)]

  (print "Begin")
  (pretty-print board)
  (println)
  (loop [candidates candidates]
    (if (or (solved? candidates)
            (= (solve candidates) candidates))
        (do
          (print "End")
          (pretty-print (candidates->list candidates))
          (println candidates))
        (recur 
          (solve candidates))))))