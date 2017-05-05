#### introduction

The sliding block puzzle family provide a fertile {area/oppurtunity} to develop heuristic functions for informed search strategies. This is by no means a new area in either puzzles or computer science, and contains a variety of strategies to use. This work takes a 10,000 foot perspective and evaluates several heuristics approaches using the traditional 8 and 15 puzzles. We are measuring the heuristic's performance on the number of nodes expanded, branching factor, time, and cost. Namely we characterize linear {manhattan, linear conflict} methods, tile based {nmaxswap}, and pattern databases with using a grid search of different algorithms to illustrate the trade-offs of each. 


#### Prior Work


- disjoint pattern database heuristics by Korf & Felner
- The pathology of heuristic search in the 8-puzzle

- design of algorithms(?, nice-to-have)

#### Methods

At a high level our methodology follows: first we generate puzzle boards and categorize according to the optimal cost of each board. We then run multiple experiments using three algorithms; A*, IDA, SMA, with each of our heuristics over two puzzle sizes (8 and 15). For each iteration we record the size board, heuristic and algorithm used along with measuring the board cost, time (milliseconds) elapsed, number of nodes expanded, and branching factor. Finally we aggregate our results, and conduct an multivariate analysis.

The 8 and 15 puzzles are small enough that we can store all the states. Thus for replication and uniformity we first generate all permutations and store them to text files. To ensure correctness in assigning cost to boards we start at our goal state, and permute the empty tile position by N-number of moves from the goal state. After each state is generated it is saved to a graphically representative file, to which our application has a parser function. As previously noted each board is categorized into a directory heirarchy according to the board's cost, N-number moves.

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