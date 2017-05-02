/*==================================================
    CS:4420 Artificial Intelligence
    Spring 2017
    Main method
    Name: Pat Hawks, Ryan Larson, Rui Yang
  ==================================================*/
package project

import project.Nsquare._
import project.Utility._

object Project {
  /**
   * Entry point for our program
   * Takes three command line arguments:
   *   0 - Filename of state to load
   *   1 - Variant of A* to use
   *   2 - Heuristic to use
   */
  def main(args: Array[String]) = {
    val s = read_board_to_state("data/test-board.txt")
    val search = args(1) match {
      case "astar" => (i: State, g: State, m: State=>List[(State, Operator)], h: State=>Int, c:(State, Operator)=>Int) => Search.astar(i, g, m, h, c)
      case "id"    => (i: State, g: State, m: State=>List[(State, Operator)], h: State=>Int, c:(State, Operator)=>Int)=> Search.astar(i, g, m, h, c)
      //case _       => throw "Unknown search"
    }
    val heuristic = args(2) match {
      case "Manhatta"       => (s: State) => heuristics.manhattan(s)
      case "linearConflict" => (s: State) => heuristics.linearConflict(s)
      case "NMaxSwap"       => (s: State) => heuristics.NMaxSwap(s)
      case "nonAdditiveFringe"    => (s:State) => patternDatabase.nonAdditivePDB(s,0)
      case "nonAdditiveCorner"    => (s:State) => patternDatabase.nonAdditivePDB(s,1)
      case "nonAdditiveMax"    => (s:State) => patternDatabase.nonAdditivePDB(s,2)
      case "disjointVertical"    => (s:State) => disjointPatternDatabase.disjointPDB(s,0)
      case "disjointHorizontal"    => (s:State) => disjointPatternDatabase.disjointPDB(s,1)
      case "disjointMax"    => (s:State) => disjointPatternDatabase.disjointPDB(s,2)
      //case _                => throw "Unknown Search"
    }

    //search(s, Nsquare.goalState(3), Nsquare.validMoves(s), heuristic, costs)
    println("Ran")
  }
}
