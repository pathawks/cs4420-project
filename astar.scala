/*==================================================
    CS:4420 Artificial Intelligence
    Spring 2017
    Project
    Name: Pat Hawks, Ryan Larson, Rui Yang
  ==================================================*/

import scala.collection.mutable.PriorityQueue

/**
 * A* search
 * @param initial state
 * @param goal state we are looking for
 * @param makeNodes function that returns a list of all children for a given
 *        node, as well as the step to get from current node to that child
 * @param heuristic function that analyzes the desirability of the current state
 * @returns list of steps required to find goal, or Nil if goal was not found
 */
def astar[T, S] (initial:T, goal:T, makeNodes:T=>List[(T, S)], heuristic:T=>Int):List[S] = {
  // Our fringe will hold nodes and the list of steps to get to that node
  // It will also keep track of if a node matches a goal state
  // It is sorted by g(x) + f(x)
  val fringe = PriorityQueue.empty[(Int, T, Int, List[S])](
    Ordering.by((_: (Int, T, Int, List[S]))._1).reverse
  )

  /**
   * Inner function that actually does the searching
   * @param a state
   * @param solution steps required to get from initial to a
   * @returns list of steps required to find goal, or Nil if goal was not found
   */
  def search(a:T, g:Int, solution:List[S]):List[S] = {
    // Find all valid moves from a state, and add them to the fringe
    makeNodes(a).foreach {
      case (a, s) => {
        val hScore = heuristic(a)
        val score = g + hScore
        val cost = g + 1
        val newSolution = s :: solution
        val node = (score, a, cost, newSolution)
        fringe += node
      }
    }
    fringe.isEmpty match {                  // If the fringe is empty
      case true => Nil                      //   No solution found; return Nil
      case _    => fringe.dequeue() match { // Else dequeue a node
        case (_, a, g, solution) => {
          if (a == goal)                    // If this node matches the goal
            solution                        //   we have found a solution
          else                              // Else
            search(a, g, solution)          //   search its children
        }
      }
    }
  }

  search(initial, 0, Nil).reverse
}
