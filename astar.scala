/*==================================================
    CS:4420 Artificial Intelligence
    Spring 2017
    Project
    Name: Pat Hawks, Ryan Larson, Rui Yang
  ==================================================*/

import scala.collection.mutable.PriorityQueue

/**
 * A* search
 * @param a some initial state
 * @param children function that returns a list of all children for a given
 *        node, as well as the step to get from current node to that child
 * @param heuristic function that analyzes the desirability of the current state
 */
def astar[T, S] (a:T, goal:T, makeNodes:T=>List[(T, S)], heuristic:T=>Int):List[S] = {
  val fringe = PriorityQueue.empty[(Int, Boolean, T, List[S])](
    Ordering.by((_: (Int, Boolean, T, List[S]))._1).reverse
  )

  def search(a:T, g:Int, solution:List[S]):List[S] = {
    makeNodes(a).foreach {
      case (a, s) => {
        val hScore = heuristic(a)
        val score = g + hScore
        val newSolution = s :: solution
        val node = (score, a == goal, a, newSolution)
        fringe += node
      }
    }
    fringe.isEmpty match {
      case true => Nil
      case _    => fringe.dequeue() match {
        case (_, true, _, solution) => solution
        case (_, _,    a, solution) => search(a, g + 1, solution)
      }
    }
  }

  search(a, 0, Nil).reverse
}
