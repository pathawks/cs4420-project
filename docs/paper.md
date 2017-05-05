#### introduction

The sliding block puzzle family provide a fertile {area/oppurtunity} to develop heuristic functions for informed search strategies. This is by no means a new area in either puzzles or computer science, and contains a variety of strategies to use. This work takes a 10,000 foot perspective and evaluates several heuristics approaches using the traditional 8 and 15 puzzles. We are measuring the heuristic's performance on the number of nodes expanded, branching factor, time, and cost. Namely we characterize linear {manhattan, linear conflict} methods, tile based {nmaxswap}, and pattern databases with using a grid search of different algorithms to illustrate the trade-offs of each. 


#### Prior Work


disjoint pattern database heuristics by Korf & Felner

The pathology of heuristic search in the 8-puzzle

design of algorithms(?)

#### Methods

At a high level our methodology follows: first we generate puzzle boards and categorize according to the optimal cost of each board. We then run multiple experiments using three algorithms; A*, IDA, SMA, with each of our heuristics over two puzzle sizes (8 and 15). For each iteration we record the size board, heuristic and algorithm used along with measuring the board cost, time (milliseconds) elapsed, number of nodes expanded, and branching factor. Finally we aggregate our results, and conduct an multivariate analysis.

The 8 and 15 puzzles are small enough that we can store all the states. Thus for replication and uniformity we first generate all permutations and store them to text files. To ensure correctness in assigning cost to boards we start at our goal state, and permute the empty tile position by N-number of moves from the goal state. After each state is generated it is saved to a graphically representative file, to which our application has a parser function. As previously noted each board is categorized into a directory heirarchy according to the board's cost, N-number moves.

The 3x3 board only has 181,400 possible states. Because of it's relatively small size our first 8 puzzle experiment used A*. After setting the algorithm and heuristic we feed boards to our program in increasing cost. At lower N-cost boards we have a low number of possible states, 