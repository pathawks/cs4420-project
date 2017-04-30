/**
UTILITIES script
  Functions:
    -read_board_to_state: function to read txt boards

  Authors:
    -Ryan Larson
  */
package utilities
import nsquare.Nsquare
import scala.io.Source

class Utility {

  def read_board_to_state(fName: String): Unit = {
    /*
    Function to take a .txt file depicting a n board, and output a mapping
    of the positions to their tiles.
    INPUTS: A str filename path
    OUTPUTS: a Tuple(
      Map of (x,y)->tile #'s,
      tuple of empty tile
      )
    */
    val puzz = Nsquare

    var file_lines = Source.fromFile(fName).getLines.toList
    val n_size_board = file_lines.length

    //var tiles = scala.collection.mutable.HashMap[(Int, Int), Int]()
    var tiles:Map[puzz.Pos, puzz.Tile] = Map()
    val emptyTile: (Int, Int) = (0,0)

    for (i <- 0 to n_size_board-1) {
      // The list is made up of strings
      var row = file_lines(i).split(" ")
      // And for each 'column'
      for (j <-0 to n_size_board-1) {
        // Conditionally update
        row(j) match {
          case "_" => emptyTile -> (i+1, j+1)
          case _ => tiles += ((i+1, j+1) -> row(j).toInt)
          //case _ => tiles(List((i+1, j+1) -> row(j).toInt))
        }
      }
    }

    val startBoard = new puzz.Board(n_size_board, tiles)
    val start = new puzz.State(startBoard, emptyTile) // start state for the 8-puzzle

//  return start
  }

}
