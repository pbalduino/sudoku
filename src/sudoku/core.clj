(ns sudoku.core
  (:use [sudoku.pprint]
        [sudoku.util]))

(def one-nine [1 2 3 4 5 6 7 8 9])

(defn- list->candidates [numbers]
  (mapv 
    (fn [x]
      (if (= x 0) 
          one-nine 
          [x]))
    numbers))

(defn- candidates->list [numbers]
  (mapv 
    (fn [x]
      (if (= (count x) 1) 
          (first x)
          0))
    numbers))


(defn- solved? [candidates]
  (nil? (some #(not= (count %) 1) candidates)))

(defn- get-line [candidates line]
  (subvec candidates (* line 9) (* (inc line) 9)))

(defn- remove-value [x list]
  (vec
    (if-not (= (count list) 1)
            (filter #(not= x %) list)
            list)))

(defn insert [part whole start]
  (let [part-size  (count part)
        whole-size (count whole)
        begin      (if (= start 0)
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
  (loop [solved (flatten (filter #(= (count %) 1) line))
         line   line]
    (if (empty? solved)
      line
      (recur (rest solved) (mapv #(remove-value (first solved) %) line)))))

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
                      (do
                         (recur (rest lines) 
                                (conj result (first lines))))))))))

(defn rotate-left [candidates]
  (loop [line (range 0 9) result []]
    (if (empty? line)
        result
        (recur (rest line)
               (loop [col (range 0 9)
                      result result]
                  (if (empty? col)
                      result
                      (recur (rest col)
                             (conj result (nth candidates (- (* 9 (inc (first col))) (inc (first line))))))))))))

(defn rotate-right [candidates]
  (rotate-left 
    (rotate-left
      (rotate-left candidates))))

(defn subvec-2 [candidates]
  #(subvec candidates % (+ % 3)))

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
  (join-lines 
    (map clear-line (break-lines candidates))))

(defn eliminate-cols [candidates]
  (rotate-right
    (eliminate-lines 
      (rotate-left candidates))))

(defn eliminate-cells [candidates]
  (group-lines-as-cells 
    (eliminate-lines
      (group-cells-as-lines candidates))))

(defn -main 
"Sudoku Solver

Regras
- Tanto uma linha quanto uma coluna devem ser preenchida com valores de 1 a 9 sem repetição
- As células de 3x3 devem ser preenchidas por valores de 1 a 9 sem repetição

Passos para resolver um Sudoku:

1. Toda célula vazia começa com valores-candidatos de 1 a 9. Células já preenchidas tem apenas um candidato.
2. É feita uma varredura na linha eliminando os candidatos que já estiverem em células preenchidas
3. É feita uma varredura na coluna eliminando os candidatos que já estiverem em células preenchidas
4. É feita uma varredura na célula eliminando os candidatos que já estiverem em células preenchidas
5. Caso ainda existam células com mais de um candidato, aplique a função novamente.


              [0 0 7   0 0 0   4 0 6
               8 0 0   4 0 0   1 7 0
               0 0 0   3 0 0   9 0 5
              
               0 0 0   7 0 5   0 0 8
               0 0 0   0 0 0   0 0 0
               4 0 0   2 0 8   0 0 0

               7 0 4   0 0 3   0 0 0
               0 5 2   0 0 1   0 0 9
               1 0 8   0 0 0   6 0 0]
"
  []
  (let [board [0 6 0   3 0 0   8 0 4
               5 3 7   0 9 0   0 0 0
               0 4 0   0 0 6   3 0 7
              
               0 9 0   0 5 1   2 3 8
               0 0 0   0 0 0   0 0 0
               7 1 3   6 2 0   0 4 0

               3 0 6   4 0 0   0 1 0
               0 0 0   0 6 0   5 2 3
               1 0 2   0 0 9   0 8 0]
        candidates (list->candidates board)
        solve      (comp eliminate-cells eliminate-cols eliminate-lines)]

  (loop [candidates candidates]
    (if (or (solved? candidates)
            (= (solve candidates) candidates))
        (do
          (println)
          (pretty-print (candidates->list candidates)))
        (recur 
          (do
            (solve candidates)))))))