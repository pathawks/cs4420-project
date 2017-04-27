/*==================================================
    CS:4420 Artificial Intelligence
    Spring 2017
    Project
    Name: Pat Hawks, Ryan Larson, Rui Yang
  ==================================================*/

import java.io._

import scala.collection.mutable.PriorityQueue
import scala.collection.mutable.Set

/**
 * A* search
 * @param initial state
 * @param goal state we are looking for
 * @param makeNodes function that returns a list of all children for a given
 *        node, as well as the step to get from current node to that child
 * @param heuristic function that analyzes the desirability of the current state
 * @returns list of steps required to find goal, or Nil if goal was not found
 */
def generate[T, S] (initial:T, makeNodes:T=>List[(T, S)]):Unit = {
  // Our fringe will hold nodes and the list of steps to get to that node
  // It will also keep track of if a node matches a goal state
  // It is sorted by g(x) + f(x)
  val fringe = PriorityQueue.empty[(Int, T)](
    Ordering.by((_: (Int, T))._1).reverse
  )

  val seen = Set[T]()

  var numSeen = 0
  var max = 0

  /**
   * Inner function that actually does the searching
   * @param a state
   * @param solution steps required to get from initial to a
   * @returns list of steps required to find goal, or Nil if goal was not found
   */
  def search(a:T, g:Int):Unit = {
    val pw = new PrintWriter(new File("3x3/" + g + "/" + numSeen + ".txt"))
    pw.write(a.toString)
    pw.close
    numSeen += 1

    // Find all valid moves from a state, and add them to the fringe
    makeNodes(a).foreach {
      case (a, _) => {
        if (!seen(a)) {
          val cost = g + 1
          val node = (cost, a)
          if (cost > max)
            max = cost
          fringe += node
          seen += a
        }
      }
    }
    fringe.isEmpty match {                // If the fringe is empty
      case true => ()                     //   No solution found; return Nil
      case _    => fringe.dequeue match { // Else dequeue a node
        case (g, a) => {
          search(a, g)                  //   search its children
        }
      }
    }
  }

  search(initial, 0)
  println("\n\n\nNumber seen: " + numSeen + "\nMax cost: " + max)
}
