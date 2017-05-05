# Artificial Intelligence Project

**Authors:** Patrick Hawk, Ryan Larson, Rui Yang.


#### Abstract


#### Introduction


#### Prior Work

*Rui*: Where did you read the heuristic algorithms?
> Papers provided + disjoint pattern database heuristics by Korf & Felner



#### Methods

Introduction
Generation
Algorithms
heuristics
Experiments -> technical details & recipe

- Generation:
  - Started with goal state, permuted to non-goal states according to number of moves
  - Generated all permutations of n-board states.
  - Enables a ground truth for a board state search
- heuristics
  1. Manhattan
  2. Linear conflict
  3. nonAdditive
    * fringe
    * corner
    * max
  4. Disjoint
    * Vertical
    * Horizontal
    * Max
- Algorithms:
  1. A*
  2. Iterative Deepening
  3. SMA

#### Results

- 3x3 Board
  - time
  - branching factor
  - nodes expanded
- 4x4 board


#### Discussion

- Questions:
  * Which heuristic performed the best?
  * How do the heuristic metrics change over the solution complexity?
  * What, if any, trade offs are there within the heuristics?

#### Conclusion


#### References
- disjoint pattern database heuristics by Korf & Felner
- by The pathology of heuristic search in the 8-puzzle by Piltaver, Rok ; Luštrek, Mitja ; Gams, Matjaž