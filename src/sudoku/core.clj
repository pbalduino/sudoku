(ns sudoku.core)

(defn- pretty-print [game]
	(let [a 1]))

(defn -main []
	(let [board [[0 0 7][0 0 0][4 0 6]
		           [8 0 0][4 0 0][1 7 0]
		           [0 0 0][3 0 0][9 0 5]
		          
		           [0 0 0][7 0 5][0 0 8]
		           [0 0 0][0 0 0][0 0 0]
		           [4 0 0][2 0 8][0 0 0]

		           [7 0 4][0 0 3][0 0 0]
		           [0 5 2][0 0 1][0 0 9]
		           [1 0 8][0 0 0][6 0 0]]
        numbers (vec (flatten board))]
	(assoc numbers 2 99)))