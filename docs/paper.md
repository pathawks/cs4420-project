#### Introduction

	The sliding block puzzle family provide a fertile area to develop heuristic functions for informed search strategies. This is by no means a new area in either puzzles or computer science, and puzzle problems contain a variety of strategies to use. This work takes a 10,000 foot perspective and evaluates several heuristics approaches using the traditional 8 and 15 puzzles. We are measuring the heuristic's performance on the number of nodes expanded, branching factor, time, and cost. Namely we characterize empty-tile-relaxed {manhattan, linear conflict} methods, adjacency-relaxed{nmaxswap}, and pattern databases {non-additive, disjoint} while using a grid search of different algorithms to illustrate the trade-offs of each. We hypothesize that in order of increasing performance, the empty-tile-relaxed and adjacency-relaxed will be similar with Linear Conflict out edging N-Max-Swap, which will be both be dominated by the pattern databases.


##### Prior Work

  **Heuristics**

	As mentioned this is by no means a cutting edge field. The N-Puzzle provides a neatly packaged problem space, one whose complexity provides challenge for new search strategies as the number of tiles rapidly increases. While more elaborate heuristics have been developed towards this problem we will constrain our experiments to three broad heuristic families: linear, tile, and pattern based. We will briefly discuss the background and attributes of each family.

	Empty-tile-relaxed: This aptly named family includes the Manhattan and Linear Conflict heuristic. The family is characterized as adhering to an adjacency constraint, meaning only tiles in adjacent rows or columns can be manipulated. The Manhattan heuristic regards each tile independently, taking the linear distance between two tiles at right angles. It is a relaxed solution to the N-puzzle, and is deterministic. In contrast the Linear Conflict calculates the Manhattan cost, and increments it’s value by counting pairs of tiles that are within their correct row or column, but not their correct position. At any value N the Linear Conflict is expected to expand less nodes than Manhattan, at the cost of spending more time at each. In addition as N increases the Manhattan heuristic will suffer from a steep cost to reverse any errors.

	Adjacency-relaxed: The adjacency-relaxed search strategies remove the adjacency constraint while enforcing empty tile involvement, resulting in a lower bound on the number of moves. The efficacy depends on the coding solution, randomly choosing a out-of-place tile yields better performance than an ordered solution. We are using the N-Max-swap heuristic, whose performance is similar to the empty-tile-relaxed heuristics. However the pattern databases will exceed N-Max-Swap’s performance.

	Pattern-based: The Pattern-based heuristics take advantage of pattern repetition within the N-Puzzle. The heuristic precomputes a lookup table for later use in the search. This means it will preform well in the nodes expanded at the cost of increased run time. Pattern-based heuristics can be further broken down into two further categories; non-additive and disjoint. The non-additive heuristics corner, fringe, and max use the entire board as it’s index. This is opposed to the disjointed strategies, which partitions and evaluates the board horizontally, vertically or a combination of the two. We note that the Manhattan heuristic is a special case of disjoint pattern database, calculating every tile grid into a group. In theory, the disjoint should preform better than it’s sibling, with a caveat: only on even number boards when it is able to partition equally. Otherwise the non-additive strategies will have similar performance.

##### Search Algorithms

###### A*

Our A* search algorithm is general purpose. It takes as arguments:

  1. An initial node (Can be any type that can be compared for equality)
  2. A goal node
  3. A `makeNodes` function which generates children of a node, and also returns
     the move from the current node to each child.
  4. A heuristic function, which takes a node and returns the estimated distance
     from that node to the goal as an `int`. This is the `h(x)` function.
  5. A cost function, which takes a child node and a step and returns the cost
     to that child by that step. This is the `g(x)` function.

A* will add generated nodes to a priority queue sorted by minimum value of a
score which is calculated by `∑g(x) + h(x)`. As nodes are dequeued, they are
compared to the goal node and, if they are not the goal, their children are
generated and added to the priority queue.

###### Iterative Deepening A*

This works similar to uninformed Iterative Deepening Depth-First search except
that the depth limit is determined by the heuristic function. Because the
heuristic function must underestimate the cost of reaching the goal, we are
guaranteed that any solution we find is the optimal solution.

The advantage of IDA* over A* is that the priority queue is no longer necessary,
thus the memory requirements are dramatically reduced. Branches that exceed the
cost limit are never explored.

The disadvantage of IDA* is that the tree may be traversed multiple times if the
cost was underestimated the first time. In practice however this is not an
issue, and IDA* runtime was usually lower than A*

#### Methods
At a high level our methodology follows: First we generate puzzle boards, then run multiple experiments over the same arbitrary subset of all possible puzzles using two algorithms (A* and IDA*) with each of our heuristics over two puzzle sizes (8 and 15). For each combination we record the board size, algorithm and heuristic used along with measuring the board cost (steps in optimal solution), time (milliseconds) elapsed, number of nodes expanded, and branching factor. Finally we aggregate our results, and conduct an multivariate analysis.

The 8 and 15 puzzles are small enough that we can store all the states which are
solvable in 20 or fewer moves. Thus for replication and uniformity we first generate all permutations and store them to text files. We did this by using breadth-first search plus memoization, starting from the goal state at the root and branching down via legal moves until we have generated all boards that are 20 moves or fewer away from the goal state. Because we used a breadth-first search, we could be certain that any board we saw was solvable in no fewer moves than the depth at which it was generated. After each state is generated it is saved to a text file, for which our application has a parser function. As previously noted each board is categorized into a directory heirarchy according to the board's cost, N-number moves.

All of our tests were run on the same machine for uniformity: A Mac Pro with a 2.8GHz Quad-Core Intel Xeon processor, 16 GB of RAM, and Scala version 2.12.2. To give our solver flexibility with memory, we configured the Java Virtual Machine to start with an initial heap size of 4 GB, and a max heap size of 10 GB. We only ran out of memory once, while running A* with NMaxSwap on a 15-puzzle board that was solvable in 20 moves. Due to time constraints we are only able to perform one set of experiments.

The 3x3 board only has 181,400 possible states. Because of it's relatively small size our first 8 puzzle experiment used A*. We use the same set of boards for each of our experiments, varying only the algorithm and heuristic. After setting the algorithm and heuristic we feed boards to our program in increasing cost. At lower N-cost boards we are able to exhuast the low number of possible states, and it isn't until {!!!stat, look for number of records by cost, 3x3} that we pick a limited number of state subsets.

 Our quasi grid-search used several heuristics; Manhattan, linear conflict, non-additive {methods?}, and disjoint pattern databases. The non-additive heuristic had three variations; horizontal, vertical, and maximum which uses the two previous variations. The disjoint pattern database also had fringe, corner and maximum variants. Each experiment is evaluated on four metrics, each is calculated by the formula in the table below:

| Metric | Formula |
|---|---|
| Cost | N-number permutations away from goal state, ground truth from generation. |
| Time | Elapsed time in milliseconds, taken from end time - start time. |
| Nodes Expanded | A counter kept within the search function. |
| Branching Factor | We use {!!!@RUI's method/Newton's method} to calculate the E.B.F. |

	After completing our 8-puzzle experiments we turn towards the 15 puzzle. A*'s effectiveness is limited by it's large fringe, so we use an iterative deepening A* instead. Our second set of experiments involve a much more constrained subset of attributes, we run the algorithms on both size boards with only one heuristic. Due to it’s performance in our 8-Puzzle experiments we choose the Linear Conflict as our control heuristic when comparing A* and IDA.
	Finally, after running both sets of experiments we perform a multivariate analysis on the metrics results. This is done using the data analysis package Pandas for Python, along with Seaborn for statistical plots, and D3.js to facilitate interaction.



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

##### Heuristic Patterns

While Linear Conflict was a good heuristic for puzzles that can be solved in 20
or fewer moves, it seems that it would not be good enough for more complicated
puzzles. Already in our test data, we noticed that the number of nodes expanded
started growing rapidly around 16 step solutions. We did try to solve a couple
puzzles with an 80 step optimal solution (the maximum for 15-Puzzles), and
neither A* nor IDA* was able to find a solution after 10 hours.

##### Algorithm Discussion

We only had one test case that ran our of memory, so IDA* did not solve any
puzzles that A* was unable to solve. However, IDA* was a bit faster than A* in
our tests. This was a suprise. Our intuition suggested that IDA* would be no
faster, and possibly slower than A*, on account of possibly restarting the
search multiple times. In practice, this was not an issue, and seems to be more
than offset by not having to keep sorted a priority queue of nodes.

Since memory did not seem to be a limiting factor for us, we would have liked to
explore a Bidirectional A* where one tree starts from the initial state and
branches toward the goal, while another tree starts from the goal and branches

##### Critique

If we had more time, we would have liked to calculate the amortized time of the
Pattern Database heuristics over a large number of test cases *with out*
regenerating the database each time. Once the database was generated, these
heuristics resulted in a lower branching factor and fewer nodes expanded than
the linear heuristics. It seems that if we could store this database instead of
regenerating it each time, it would have eventually outperformed the others. In
our tests, the time it took to generate this database was *far* greater than the
time it took other heuristics to find a solution that the overhead was never
worth it.

If we had more time, we would have liked to test *every* puzzle solvable in 20
or fewer moves, rather than just a subset of puzzles. It is _conceivable_
(though not particularly likely) that we just happened to find a subset that one
heuristic was especially well suited for, and testing more puzzles would have
eliminated this possibility.
