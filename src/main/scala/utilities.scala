/**
  UTILITIES script
  Functions:
    -read_board_to_map: function to read txt boards

  Authors:
    -Ryan Larson
  */

import scala.io.Source

val filename = "data/test-board.txt"

def read_board_to_map(fName: String): Unit = {
  /*
  Function to take a .txt file depicting a n board, and output a mapping
  of the positions to their tiles.
  INPUTS: A str filename path
  OUTPUTS: a Tuple(
    Map of (x,y)->tile #'s,
    tuple of empty tile
    )
  */

  var file_lines = Source.fromFile(fName).getLines.toList
  val n_size_board = file_lines.length
  // Init vars
  var tiles = scala.collection.mutable.HashMap[(Int, Int), Int]()
  var emptyTile = Tuple2;
  // For each row
  for (i <- 0 to n_size_board-1) {
    // The list is made up of strings
    var row = file_lines(i).split(" ")
    // And for each 'column'
    for (j <-0 to n_size_board-1) {
      // Conditionally update
      row(j) match {
        case "_" => emptyTile -> (i+1, j+1)
        case _ => tiles += ((i+1, j+1) -> row(j).toInt)
      }
    }
  }
  (tiles, emptyTile)
}
read_board_to_map(filename)
