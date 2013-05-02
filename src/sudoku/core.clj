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

(join-lines [lines]
  lines)

(defn eliminate-lines [candidates]
  (join-lines 
    (let [candidates (break-lines candidates)]
      (map clear-line candidates))))

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
5. Caso ainda existam células com mais de um candidato, aplique a função novamente."
  []
  (let [board [0 0 7   0 0 0   4 0 6
               8 0 0   4 0 0   1 7 0
               0 0 0   3 0 0   9 0 5
              
               0 0 0   7 0 5   0 0 8
               0 0 0   0 0 0   0 0 0
               4 0 0   2 0 8   0 0 0

               7 0 4   0 0 3   0 0 0
               0 5 2   0 0 1   0 0 9
               1 0 8   0 0 0   6 0 0]
        candidates (list->candidates board)]
    (pretty-print board)
    (eliminate-lines candidates)))
