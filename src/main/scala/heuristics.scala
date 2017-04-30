
/*==================================================
    CS:4420 Artificial Intelligence
    Spring 2017
    Heuristics
    Name: Pat Hawks, Ryan Larson, Rui Yang
  ==================================================*/

package heuristics

import nsquare.Nsquare
import util.control.Breaks._
import scala.math._

class heuristics {

  def dummy(t: Any) = {
    0
  }

  // Manhatta distance heuristcs
  def Manhatta(s: Int, tiles: Map[nsquare.Nsquare.Pos, nsquare.Nsquare.Tile]): Int = {
    var sum = 0
    for (pair <- tiles) {
      val ((row, col), tile) = pair
      val goalRow = ceil(1.0 * tile / s)
      val goalCol = tile - (goalRow - 1) * s
      sum += abs(row - goalRow.toInt)
      sum += abs(col - goalCol.toInt)
    }

    sum
  }

  // Linear-conflict heuristics
  def linearConflict(s: Int, tiles: Map[nsquare.Nsquare.Pos, nsquare.Nsquare.Tile]): Int = {
    var sum = Manhatta(s, tiles)

    def pos(pair: ((Int, Int), Int)) = {
      val ((r, c), t) = pair;
      var gr = ceil(1.0 * t / s)
      (r, c, t, gr, t - (gr - 1) * s)
    }

    for (pair <- tiles) {
      val (row, col, tile, goalRow, goalCol) = pos(pair)
      if (row == goalRow) {
        for (sameRow <- tiles.filterKeys(k => (k._1 == row & k._2 < col))) {
          val (row2, col2, tile2, goalRow2, goalCol2) = pos(sameRow)
          if (goalRow2 == row & (col2 - col) * (goalCol2 - goalCol) < 0) sum += 2
        }
      }
      if (col == goalCol) {
        for (sameCol <- tiles.filterKeys(k => (k._1 < row & k._2 == col))) {
          val (row2, col2, tile2, goalRow2, goalCol2) = pos(sameCol)
          if (goalCol2 == col & (row2 - row) * (goalRow2 - goalRow) < 0) sum += 2
        }
      }
    }
    sum
  }

  // N-MaxSwap heuristics
  def NMaxSwap(s: Int, tiles: Map[nsquare.Nsquare.Pos, nsquare.Nsquare.Tile]): (Int, Array[Int], Array[Int]) = {
    var iter = 0
    var buffer = 0
    var P = Array.tabulate(9)(n => 9)
    var B = Array.tabulate(9)(n => 0)

    def swap(a: Int, b: Int) = {
      if (a != b) {
        iter += 1
        buffer = P(a);
        P(a) = P(b);
        P(b) = buffer
        buffer = B(P(a) - 1);
        B(P(a) - 1) = B(P(b) - 1);
        B(P(b) - 1) = buffer
      }
    }

    for (pair <- tiles) {
      val index = (pair._1._1 - 1) * 3 + pair._1._2 - 1
      val n = pair._2
      P(index) = n; B(n - 1) = index
    }
    B(8) = P.indexOf(9)
    while (P(8) != 9) {
      swap(B(8), B(B(8)))
    }
    val unsort = P.filter(n => n != P.apply(n - 1))
    for (e <- unsort) {
      swap(e - 1, B(8)); swap(B(e - 1), e - 1)
    }
    iter
  }
}