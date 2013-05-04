# sudoku

Sudoku solver written in Clojure

## Usage

* Install Leiningen

* Run in the first time:
lein deps

* Run always you want to run:
lein run

# Rules (pt_BR)

Sudoku Solver

Regras
- Tanto uma linha quanto uma coluna devem ser preenchida com valores de 1 a 9 sem repetição
- As células de 3x3 devem ser preenchidas por valores de 1 a 9 sem repetição

Passos para resolver um Sudoku:

1. Toda célula vazia começa com valores-candidatos de 1 a 9. Células já preenchidas tem apenas um candidato.
2. É feita uma varredura na linha eliminando os candidatos que já estiverem em células preenchidas
3. É feita uma varredura na coluna eliminando os candidatos que já estiverem em células preenchidas
4. É feita uma varredura na célula eliminando os candidatos que já estiverem em células preenchidas
5. Caso ainda existam células com mais de um candidato, aplique a função novamente.

## License

Copyright © 2013 FIXME

Distributed under the Eclipse Public License, the same as Clojure.
