/*==================================================
    CS:4420 Artificial Intelligence
    Spring 2017
    Search Algorithms
    Name: Pat Hawks, Ryan Larson, Rui Yang
  ==================================================*/
package project

import scala.collection.mutable.PriorityQueue

/**
 * A* search
 * @param initial state
 * @param goal state we are looking for
 * @param makeNodes function that returns a list of all children for a given
 *        node, as well as the step to get from current node to that child
 * @param heuristic function that analyzes the desirability of the current state
 * @param costs function that return the cost of moving a step from a state
 * @return list of steps required to find goal, or Nil if goal was not found
 */
class Search {
  def astar[T, S](initial: T, goal: T, makeNodes: T => List[(T, S)], heuristic: T => Int, costs: (T, S) => Int): List[S] = {
    /**
      * Our fringe will hold nodes and the list of steps to get to that node
      * We are keeping track of:
      *   - the score of a given state (g(x) + h(x))
      *   - the state itself
      *   - the actual cost to reach this state from the root node
      *   - the path to reach this node from the root node
      */
    val fringe = PriorityQueue.empty[(Int, T, Int, Int, List[S])](
      Ordering.by((_: (Int, T, Int, Int, List[S]))._1).reverse)
    var expandedNodes = 0
    var costOfSolution = 0
    var depth = 0
    var generatedNodes = 0

    /**
      * Inner function that actually does the searching
      *
      * @param a        state
      * @param solution steps required to get from initial to a
      * @return list of steps required to find goal, or Nil if goal was not found
      */
    def search(a: T, g: Int, d: Int, solution: List[S]): List[S] = {
      expandedNodes += 1
      // Find all valid moves from a state, and add them to the fringe
      makeNodes(a).foreach {
        case (a, s) => {
          generatedNodes += 1
          val hScore = heuristic(a)
          val score = g + hScore
          val cost = g + costs(a, s)
          val newSolution = s :: solution
          val node = (score, a, cost, d + 1, newSolution)
          fringe += node
        }
      }
      fringe.isEmpty match { // If the fringe is empty
        case true => Nil //   No solution found
        case _ => fringe.dequeue match { // Else dequeue a node
          case (_, a, g, d, solution) => {
            if (a == goal) { // If this node matches the goal
              costOfSolution = g
              depth = d
              solution //   we have found a solution
            } else { // Else
              search(a, g, d, solution) //   search its children
            }
          }
        }
      }
    }

    val startTime = System.currentTimeMillis()
    val r = search(initial, 0, 0, Nil).reverse
    val endTime = System.currentTimeMillis()
    // val eftBranchingFactor = EBF(generatedNodes, depth)
    val eftBranchingFactor = 0.0
    if (r != Nil)
      printf(
        "expanded nodes: %d\teffective branching factor: %.2f\tcost of solution: %d\trunning time: %d\n",
        expandedNodes,
        eftBranchingFactor,
        costOfSolution,
        endTime - startTime
      )

    r
  }
}
