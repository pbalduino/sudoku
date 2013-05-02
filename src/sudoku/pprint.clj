(ns sudoku.pprint)

(defn pretty-print [numbers]
  (loop [list numbers pos 0]
    (let [number (first list)]
      (if (= (mod pos 27) 0)
          (do
            (when-not (= pos 0)
                      (print "|"))
            (print "\n+---------+---------+---------+")
            (when-not (= pos 81)
                      (print "\n|")))
          (if (= (mod pos 9) 0)
              (print "|\n|         |         |         |\n|")
              (if (= (mod pos 3) 0)
                (print "|"))))

      (print (str " " (if (= number 0) " " number) " ")))

    (when-not (empty? list)
              (recur (rest list) (inc pos))))
  (println "\n"))