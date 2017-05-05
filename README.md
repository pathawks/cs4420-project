# A* Puzzle Solver
Using A* to solve 8 and 15 tile puzzles

##### Authors
  * Pat Hawks
  * Ryan Larson
  * Rui Yang

### Directory Structure

```
.
├── data
│   ├── 3x3.tgz           # Boards archive
│   ├── 4x4.tgz           # Boards archive
│   ├── results           # Experimental results
|   |   └── canonical     # Processed data
│   └── test-board.txt
├── docs
│   ├── analysis          # Chart outputs
│   ├── notes.md
│   ├── outline.md
│   └── paper.md
├── index.html            # Interactive analysis
├── launch.sh
├── Makefile
├── README.md
└── src
    ├── analysis          # Results analysis
    └── main              # Application
```

### How to run
Compile with `make` and run in your shell with `scala Project.jar <file.txt> <search> <heuristic>` where:

  * `<file.txt>` File containing a text representation of the puzzle to solve
    (See `test-board.txt` for an example)
  * `<search>` Search algorithm to use. Can be one of
    - `astar` A*
    - `ida`   Iterative Deepening A*
  * `<heuristic>` Heuristic to use. Can be one of
    - `dummy`  Always returns `0`
    - `manhattan` Sum of Manhattan distance of all tiles to correct position
    - `linearConflict` Linear Conflict
    - `NMaxSwap` Number of swaps between incorrect tiles and empty space
      necessary to arrive at solution
    - `nonAdditiveFringe` Nonadditive Pattern Database
    - `nonAdditiveCorner` Nonadditive Pattern Database
    - `nonAdditiveMax` Maximum of nonAdditiveFringe & nonAdditiveCorner
    - `disjointPDBVertical` Disjoint Pattern Database
    - `disjointPDBHorizontal` Disjoint Pattern Database
    - `disjointPDBMax` Maximum of disjointPDBVertical & disjointPDBHorizontal

The program will return results as a row of Comma Seperated Values in the
format:

```csv
Board Size, <file.txt>, <search>, <heuristic>, Expanded Nodes, Effective Branching Factor, Number of moves in optimal solution, run time
```
