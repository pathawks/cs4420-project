#### introduction

The sliding block puzzle family provide a fertile {area/oppurtunity} to develop heuristic functions for informed search strategies. This is by no means a new area in either puzzles or computer science, and contains a variety of strategies to use. This work takes a 10,000 foot perspective and evaluates several heuristics approaches using the traditional 8 and 15 puzzles. We are measuring the heuristic's performance on the number of nodes expanded, branching factor, time, and cost. Namely we characterize linear {manhattan, linear conflict} methods, tile based {nmaxswap}, and pattern databases with using a grid search of different algorithms to illustrate the trade-offs of each.


#### Prior Work


- disjoint pattern database heuristics by Korf & Felner
- The pathology of heuristic search in the 8-puzzle

- design of algorithms(?, nice-to-have)

#### Methods

At a high level our methodology follows:
First we generate puzzle boards, then run multiple experiments over the same
arbitrary subset of all possible puzzles using two algorithms (A* and IDA*) with
each of our heuristics over two puzzle sizes (8 and 15).
For each combination we record the board size, algorithm and heuristic used
along with measuring the board cost (steps in optimal solution),
time (milliseconds) elapsed, number of nodes expanded, and branching factor.
Finally we aggregate our results, and conduct an multivariate analysis.

The 8 and 15 puzzles are small enough that we can store all the states which are
solvable in 20 or fewer moves. Thus for replication and uniformity we first
generate all permutations and store them to text files. We did this by using
breadth-first search plus memoization, starting from the goal state at the root
and branching down via legal moves until we have generated all boards that are
20 moves or fewer away from the goal state. Because we used a breadth-first
search, we could be certain that any board we saw was solvable in no fewer moves
than the depth at which it was generated.
After each state is generated it is saved to a text file, for which our
application has a parser function. As previously noted each board is categorized
into a directory heirarchy according to the board's cost, N-number moves.

All of our tests were run on the same machine for uniformity: A Mac Pro with a
2.8GHz Quad-Core Intel Xeon processor, 16 GB of RAM, and Scala version 2.12.2.
To give our solver flexibility with memory, we configured the Java Virtual
Machine to start with an initial heap size of 4 GB, and a max heap size of 10
GB. We only ran out of memory once, while running A* with NMaxSwap on a
15-puzzle board that was solvable in 20 moves.

The 3x3 board only has 181,400 possible states. Because of it's relatively small size our first 8 puzzle experiment used A*. We use the same set of boards for each of our experiments, varying only the algorithm and heuristic. After setting the algorithm and heuristic we feed boards to our program in increasing cost. At lower N-cost boards we are able to exhuast the low number of possible states, and it isn't until {!!!stat, look for number of records by cost, 3x3} that we pick a limited number of state subsets.

Our quasi grid-search used several heuristics; Manhattan, linear conflict, non-additive {methods?}, and disjoint pattern databases. The non-additive heuristic had three variations; horizontal, vertical, and maximum which uses the two previous variations. The disjoint pattern database also had fringe, corner and maximum variants. Each experiment is evaluated on four metrics, each is calculated by the formula in the table below:

| Metric | Formula |
|---|---|
| Cost | N-number permutations away from goal state, ground truth from generation. |
| Time | Elapsed time in milliseconds, taken from end time - start time. |
| Nodes Expanded | A counter kept within the search function. |
| Branching Factor | We use {!!!@RUI's method/Newton's method} to calculate the E.B.F. |

After completing our 8-puzzle experiments we turn towards the 15 puzzle. A*'s effectiveness is limited by it's large fringe, so we use an iterative deepening A* instead. ***To be continued***


#### Results

1. **8-Puzzle**
	- Descriptive statistics
		* Number of experiments for each algo, heuristic
		* number of experiments that didn't complete within their time frame
		* Distributions of each metric
	- nodes expanded
		* Which heuristics have the highest node expansions? Lowest?
		*
	- branching factor
	- time
		* which algorithms took the longest?
		*
	- cost
		* How does increasing the state's cost effect the nodes expanded?


**15-Puzzle: **

#### Discussion

**Heuristic patterns:**
