(ns sudoku.pprint)

(defn pretty-print [numbers]
  (loop [list numbers pos 0]
    (let [number (first list)]
      (if (zero? (mod pos 27))
          (do
            (when-not (zero? pos)
                      (print "|"))
            (print "\n+---------+---------+---------+")
            (when-not (= pos 81)
                      (print "\n|")))
          (if (zero? (mod pos 9))
              (print "|\n|         |         |         |\n|")
              (if (zero? (mod pos 3))
                (print "|"))))

      (print (str " " (if (= 0 number) " " number) " ")))

    (when (seq list)
          (recur (rest list) 
                 (inc pos))))

  (println "\n"))