/*==================================================
    CS:4420 Artificial Intelligence
    Spring 2017
    Main method
    Name: Pat Hawks, Ryan Larson, Rui Yang
  ==================================================*/
import heuristics.heuristics
import astar.Search
import EBFsolver.EBFsolver
import utilities.Utility

class Project {
  /**
   * Entry point for our program
   * Takes three command line arguments:
   *   0 - Filename of state to load
   *   1 - Variant of A* to use
   *   2 - Heuristic to use
   */
  def main(args: Array[String]) = {
    /*val s = Utilities.read_board_to_state("data/test-board.txt")
    val search = args[1] match {
      case "astar" => astar
      case "id"    => astar
      case _       => throw "Unknown search"
    }
    val heuristic = args[2] match {
      case "Manhatta"       => Manhatta
      case "linearConflict" => linearConflict
      case "NMaxSwap"       => NMaxSwap
      case "nonAdditive"    => nonAdditive
      case _                => throw "Unknown Search"
    }*/

    //search(s, goalState(3), validMoves, heuristic, costs)
    println("Ran")
  }
}
