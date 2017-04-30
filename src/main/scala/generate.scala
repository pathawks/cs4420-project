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
 * Generate all possible puzzles using depth-first iteration
 * @param initial state
 * @param makeNodes function that returns a list of all children for a given
 *        node, as well as the step to get from current node to that child
 */
def generate[T, S] (initial:T, makeNodes:T=>List[(T, S)]):Unit = {
  // Our fringe will hold nodes and the list of steps to get to that node
  // It will also keep track of if a node matches a goal state
  // It is sorted by g(x) + f(x)
  val fringe = PriorityQueue.empty[(Int, T)](
    Ordering.by((_: (Int, T))._1).reverse
  )

  // seen keeps track of all states we have already seen, so we do not create duplicates
  val seen = Set[T]()

  var numSeen = 0
  var max = 0

  /**
   * Inner function that actually does the searching
   * @param a state
   * @param g cost of a state
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
      case true => ()                     //   we have found all states
      case _    => fringe.dequeue match { // Else dequeue a node
        case (g, a) => {
          search(a, g)                    //   search its children
        }
      }
    }
  }

  search(initial, 0)
  println("\n\n\nNumber seen: " + numSeen + "\nMax cost: " + max)
}
