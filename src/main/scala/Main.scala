/*==================================================
    CS:4420 Artificial Intelligence
    Spring 2017
    Main method
    Name: Pat Hawks, Ryan Larson, Rui Yang
  ==================================================*/

import project.heuristics._
import project.Nsquare._
import project.PatternDatabase._
import project.disjointPatternDatabase._
import project.Search._
import project.Utility._

/**
 * Entry point for our program
 * Takes three command line arguments:
 *   0 - Filename of state to load
 *   1 - Variant of A* to use
 *   2 - Heuristic to use
 */
object Main extends App {
  val inFile = args(0)
  val s: State = try {
    read_board_to_state(inFile)
  } catch {
    case _ : java.io.FileNotFoundException => {
      println("File '" + inFile + "' cannot be opened")
      System.exit(-1)
      goalState(0)
    }
  }

  val search_algo = args(1) match {
    case "astar" => (i: State, g: State, m: State=>List[(State, Operator)], h: State=>Int, c:(State, Operator)=>Int) => astar(i, g, m, h, c)
    case "id"    => (i: State, g: State, m: State=>List[(State, Operator)], h: State=>Int, c:(State, Operator)=>Int)=> astar(i, g, m, h, c)
    //case _       => throw "Unknown search"
  }

  val heuristic = args(2) match {
    case "Manhatta"             => (s: State) => manhattan(s)
    case "linearConflict"       => (s: State) => linearConflict(s)
    case "NMaxSwap"             => (s: State) => NMaxSwap(s)
    case "nonAdditiveFringe"    => (s: State) => nonAdditive(s, 0)
    case "nonAdditiveCorner"    => (s: State) => nonAdditive(s, 1)
    case "nonAdditiveMax"       => (s: State) => nonAdditive(s, 2)
      // disjointPDB(s:State, mode:Int, move:Int)
    case "disjointPDBVertical"  => (s: State) => disjointPDB(s, 0, 20)
    case "disjointPDBHorizontal"=> (s: State) => disjointPDB(s, 1, 20)
    case "disjointPDBMax"       => (s: State) => disjointPDB(s, 2, 20)
    //case _                => throw "Unknown Search"
  }

  val State(Board(size, _), _) = s

  search_algo(s, goalState(size), validMoves, heuristic, cost)

}
