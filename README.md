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
Compile with `make` and run in your shell with `scala Project.jar <file.txt> <search> <heuristic>`.
