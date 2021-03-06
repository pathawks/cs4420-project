/*==================================================
    CS:4420 Artificial Intelligence
    Spring 2017
    Search Algorithms
    Name: Pat Hawks, Ryan Larson, Rui Yang
  ==================================================*/
package project

import EBFsolver.EBF

import scala.collection.mutable.PriorityQueue

/**
  * A* search
  *
  * @param initial   state
  * @param goal      state we are looking for
  * @param makeNodes function that returns a list of all children for a given
  *                  node, as well as the step to get from current node to that child
  * @param heuristic function that analyzes the desirability of the current state
  * @param costs     function that return the cost of moving a step from a state
  * @return list of steps required to find goal, or Nil if goal was not found
  */
object Search {
  def astar[T, S](initial: T, goal: T, makeNodes: T => List[(T, S)], heuristic: T => Int, costs: (T, S) => Int): Option[List[S]] = {
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
      * @param g        actual cost to reach this state from initial
      * @param d        depth of this node
      * @param solution steps required to get from initial to a
      * @return list of steps required to find goal, or Nil if goal was not found
      */
    def search(a: T, g: Int, d: Int, solution: List[S]): Option[List[S]] = {
      if (a == goal) {
        costOfSolution = g
        depth = d
        Some(solution)
      } else {
        expandedNodes += 1
        // Find all valid moves from a state, and add them to the fringe
        makeNodes(a).foreach {
          case (a, s) => {
            generatedNodes += 1
            val hScore = heuristic(a)  //        h(x)
            val cost = g + costs(a, s) // g(x)
            val score = cost + hScore  // g(x) + h(x)
            val newSolution = s :: solution
            val node = (score, a, cost, d + 1, newSolution)
            fringe += node
          }
        }
        fringe.isEmpty match {             // If the fringe is empty
          case true => None                //   No solution found
          case _ => fringe.dequeue match { // Else dequeue a node
            case (_, a, g, d, solution) => {
              search(a, g, d, solution)    //   search its children
            }
          }
        }
      }
    }

    val startTime = System.currentTimeMillis
    val result = search(initial, 0, 0, Nil)
    val endTime = System.currentTimeMillis
    val eftBranchingFactor = EBF(generatedNodes, depth)
    result match {
      case None    => {
        printf("-, -, -, -\n"); None
      }
      case Some(r) => {
        printf(
          "%5d, %.2f, %5d, %5d\n",
          expandedNodes,
          eftBranchingFactor,
          costOfSolution,
          endTime - startTime
        )
        Some(r.reverse)
      }
    }
  }

  def ida[T, S](initial: T, goal: T, makeNodes: T => List[(T, S)], heuristic: T => Int, costs: (T, S) => Int): Option[List[S]] = {
    var expandedNodes = 0
    var costOfSolution = 0
    var idaLimit = heuristic(initial)
    val defaultMin = 32767
    var depth = 0
    var min = defaultMin

    /**
      * Inner function that actually does the searching
      *
      * @param a            state
      * @param currentCost  sum of all costs to get to this state
      * @param currentDepth incremented at each depth
      * @return Some List of solution steps if found, otherwise None
      */
    def search(a: T, currentCost: Int, currentDepth: Int): Option[List[S]] = {
      if (a == goal) {
        costOfSolution = currentCost
        depth = currentDepth
        Some(Nil: List[S])
      } else {
        expandedNodes += 1
        var children: List[(T, S)]    = makeNodes(a)
        var solution: Option[List[S]] = None

        /* While there are child nodes to search
         * and no solution has been found:
         */
        while (children != Nil && solution == None) {
          val (node, move) :: t = children // Pick a child
          children = t                     // Remove it from the list
          val stepCost = costs(node, move) // Calculate its cost
          val h = heuristic(node)          // Estimate its distance from goal
          val newCost = currentCost + stepCost
          val score = newCost + h          // score = g(x) + h(x)
          if (score <= idaLimit) {         // if this *might* be an optimal path
            val newDepth = currentDepth + 1
            search(node, newCost, newDepth) match { // recursively search it
              case Some(l) => solution = Some(move :: l)
              case None => {} // solution is already None, no need to set
            }
          } else if (score <= min) { // if this is *not* an optimal path
            min = score // possibly update minimum seen estimated cost
          }
        }
        solution
      }
    }

    val startTime = System.currentTimeMillis
    var result: Option[List[S]] = None
    while (result == None) {
      result = search(initial, 0, 0)
      idaLimit = min
      min = defaultMin
    }
    val endTime = System.currentTimeMillis
    val eftBranchingFactor = EBF(expandedNodes, depth)
    result match {
      case None    => {
        printf("-, -, -, -\n")
      }
      case Some(r) => {
        printf(
          "%5d, %.2f, %5d, %5d\n",
          expandedNodes,
          eftBranchingFactor,
          costOfSolution,
          endTime - startTime
        )
      }
    }
    result
  }
}
