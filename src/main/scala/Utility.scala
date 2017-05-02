/**
UTILITIES script
  Functions:
    -read_board_to_state: function to read txt boards

  Authors:
    -Ryan Larson
  */
package project

import project.Nsquare._
import scala.io.Source

object Utility {

  def read_board_to_state(fName: String): State = {
    /*
    Function to take a .txt file depicting a n board, and output a mapping
    of the positions to their tiles.
    INPUTS: A str filename path
    OUTPUTS: a Tuple(
      Map of (x,y)->tile #'s,
      tuple of empty tile
      )
    */

    val file_lines = Source.fromFile(fName).getLines.toList
    val n_size_board = file_lines(0).trim.split(" ").length

    var tiles:Map[Pos, Tile] = Map()
    var emptyTile: (Int, Int) = (0,0)

    var i = 0;
    for (row <- file_lines) {
      i += 1;
      var j = 0;
      // The list is made up of strings
      val cols = row.trim.split(" ")
      // And for each 'column'
      for (col <- cols) {
        j += 1;
        val pos = (i, j)
        // Conditionally update
        col match {
          case "" => ()
          case "_" => emptyTile = pos
          case _ => tiles += (pos -> col.toInt)
        }
      }
    }

    val startBoard = Board(n_size_board, tiles)
    val start = State(startBoard, emptyTile) // start state for the 8-puzzle

    return start
  }

}
