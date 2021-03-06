
/*==================================================
    CS:4420 Artificial Intelligence
    Spring 2017
    Heuristics
    Name: Pat Hawks, Ryan Larson, Rui Yang
  ==================================================*/

package project

import project.Nsquare._

import util.control.Breaks._
import scala.math._

object heuristics {
  def dummy(t: Any) = {0}

  // manhattan distance heuristcs
  def manhattan(s: State): Int = {
    val State(Board(size, tiles), _) = s
    var sum = 0
    for (pair <- tiles) {
      val ((row, col), tile) = pair
      val goalRow = ceil(1.0 * tile / size)
      val goalCol = tile - (goalRow - 1) * size
      sum += abs(row - goalRow.toInt) + abs(col - goalCol.toInt)
    }

    sum
  }

  // Linear-conflict heuristics

  def linearConflict(s: State): Int = {
    val State(Board(size, tiles), _) = s
    var sum = manhattan(s)

    def goal(pair: ((Tile, Tile), Byte)) = {
      val ((r, c), t) = pair;
      var gr = ceil(1.0 * t / size)
      (gr, t - (gr - 1) * size)
    }

    for (pair <- tiles) {
      val ((row, col), tile) = pair
      val (goalRow, goalCol) = goal(pair)
      if (row == goalRow) {
        for (sameRow <- tiles.filterKeys(k => (k._1 == row & k._2 < col))) {
          val ((_, col2), _) = sameRow
          val (goalRow2, goalCol2) = goal(sameRow)
          if (goalRow2 == row & (col2 - col) * (goalCol2 - goalCol) < 0) sum += 2
        }
      }
      if (col == goalCol) {
        for (sameCol <- tiles.filterKeys(k => (k._1 < row & k._2 == col))) {
          val ((row2, _), _) = sameCol
          val (goalRow2, goalCol2) = goal(sameCol)
          if (goalCol2 == col & (row2 - row) * (goalRow2 - goalRow) < 0) sum += 2
        }
      }
    }
    sum
  }

  // N-MaxSwap heuristics
  def NMaxSwap(s: State): Int = {
    val State(Board(length, tiles), _) = s
    var iter = 0
    var buffer = 0
    val totalSize = length * length
    var P = Array.tabulate(totalSize)(n => totalSize)
    var B = Array.tabulate(totalSize)(n => 0)
    val solution = Array.tabulate(totalSize)(_ + 1)

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
      val index = (pair._1._1 - 1) * length + pair._1._2 - 1
      val n = pair._2
      P(index) = n; B(n - 1) = index
    }
    B(totalSize - 1) = P.indexOf(totalSize)
    while (P(totalSize - 1) != totalSize) {
      swap(B(totalSize - 1), B(B(totalSize - 1)))
    }
    val unsort = P.filter(n => n != P.apply(n - 1))
    breakable {
      for (e <- unsort) {
        if (solution.sameElements(P)) break
        swap(e - 1, B(totalSize - 1)); swap(B(e - 1), e - 1)
      }
    }
    iter
  }
}
