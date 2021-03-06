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
  if (args.length != 3) {
    println("Please provide a filename, a search algorithm, and a heuristic")
    System.exit(-4)
  }

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

  val search_algo :(State, State, State=>List[(State, Operator)], State=>Int, (State, Operator)=>Int)=>Option[List[Operator]]  = args(1) match {
    case "astar" => astar
    case "ida"   => ida
    case _ => {
      println("Unknown search algorithm. Must be one of: astar ida")
      System.exit(-2)
      astar
    }
  }

  val heuristic : State=>Int = args(2) match {
    case "dummy"                => dummy
    case "manhattan"            => manhattan
    case "linearConflict"       => linearConflict
    case "NMaxSwap"             => NMaxSwap
    case "nonAdditiveFringe"    => nonAdditive(_, 0)
    case "nonAdditiveCorner"    => nonAdditive(_, 1)
    case "nonAdditiveMax"       => nonAdditive(_, 2)
    case "disjointPDBVertical"  => disjointPDB(_, 0)
    case "disjointPDBHorizontal"=> disjointPDB(_, 1)
    case "disjointPDBMax"       => disjointPDB(_, 2)
    case _ => {
      println("Unknown heuristic. Must be one of: dummy manhattan linearConflict NMaxSwap")
      println("                                   nonAdditiveFringe nonAdditiveCorner nonAdditiveMax")
      println("                                   disjointPDBVertical disjointPDFHorizontal disjointPDBMax")
      System.exit(-3)
      dummy
    }
  }

  val State(Board(size, _), _) = s

  printf(
    "%dx%d, %9s, %s, %s, ",
    size,
    size,
    inFile,
    args(1),
    args(2)
  )
  search_algo(s, goalState(size), validMoves, heuristic, cost)

}
