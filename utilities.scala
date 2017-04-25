/**

  */

import scala.io.Source

val filename = "data/test-board.txt"

def read_board_to_map(fName: String): Unit = {
  /*
  Function to take a .txt file depicting a n board, and output a mapping
  of the positions to their tiles.
  INPUTS: A str filename path
  OUTPUTS: a Map of (x,y)->tile #'s
   */

  var file_lines = Source.fromFile(fName).getLines.toList
  var tiles = Map[Tuple2, Int]()
  val n_size_board = file_lines.length

  for (i <- 0 to n_size_board-1) {
    var row = file_lines(i).split(" ")

    for (j <-0 to n_size_board-1) {
      println((i+1, j+1))
      println(row(j))
      //tiles += (i+1, j+1) -> row(j).toInt

    }
  }
}
read_board_to_map(filename)
