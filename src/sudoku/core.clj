(ns sudoku.core)

(def one-nine [1 2 3 4 5 6 7 8 9])

(defn- pretty-print [numbers]
  (loop [list numbers pos 0]
    (let [number (first list)]
      (if (= (mod pos 27) 0)
        (do
           (when-not (= pos 0)
             (print "|"))
          (print "\n+---------+---------+---------+")
           (when-not (= pos 81)
                    (print "\n|")))
        (:else (if (= (mod pos 9) 0)
               (print "|\n|         |         |         |\n|")
               (:else (if (= (mod pos 3) 0)
                      (print "|"))))))

      (print (str " " (if (= number 0) " " number) " ")))

    (when-not (empty? list)
              (recur (rest list) (inc pos))))
  (println "\n"))

(defn list->candidates [numbers]
  (map 
  	(fn [x]
	 		(if (= x 0) 
 	    	one-nine 
   	    [x]))
		 numbers))

(defn solved? [candidates]
	(nil? (some #(not= (count %) 1) candidates)))

(defn eliminate-lines [candidates]
	(for [line (range 0 9)]
		(let [vector (subvec candidates (* line 9) (* (inc line) 9))]
			(loop [head (first vector) vector vector])
				vector)))

(defn insert [part whole start]
	(let [part-size  (count part)
		    whole-size (count whole)
		    begin      (if (= start 0)
		    	             []
		    	             (subvec whole 0 start))
		    end        (subvec whole (+ start part-size))]
    (vec (flatten (conj begin part end)))))

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
  (let [board [[0 0 7][0 0 0][4 0 6]
               [8 0 0][4 0 0][1 7 0]
               [0 0 0][3 0 0][9 0 5]
              
               [0 0 0][7 0 5][0 0 8]
               [0 0 0][0 0 0][0 0 0]
               [4 0 0][2 0 8][0 0 0]

               [7 0 4][0 0 3][0 0 0]
               [0 5 2][0 0 1][0 0 9]
               [1 0 8][0 0 0][6 0 0]]
        numbers    (vec (flatten board))
        candidates (list->candidates numbers)]
    (pretty-print numbers)
    (loop [candidates candidates]
    	(println (eliminate-lines candidates))
    	(println (solved? candidates))
      (println candidates))))