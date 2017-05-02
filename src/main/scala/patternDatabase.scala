/*==================================================
    CS:4420 Artificial Intelligence
    Spring 2017
    Project
    Name: Pat Hawks, Ryan Larson, Rui Yang
  ==================================================*/
package project

import project.Nsquare._
import java.io._
import scala.collection.mutable.PriorityQueue
import scala.collection.mutable.Set

class PatternDatabase {
  var fpdb:Map[IndexedSeq[Any],Int]=Map()  // Fringe Pattern Database
  var cpdb:Map[IndexedSeq[Any],Int]=Map()  // Corner Pattern Database

  // Generate above Pattern Databases
  def getNAPDB(initial: State, makeNodes: State => List[(State, Operator)], maxCost: Int, mode:Int): Unit = {
    val fringe = PriorityQueue.empty[(Int, State)](
      Ordering.by((_: (Int, State))._1).reverse)
    val seen = Set[State]()

    def search(a: State, g: Int, mode:Int): Unit = {
      if (mode==0 |mode==2){
        val fp = pattern(a, true)
        if (!(fpdb contains fp))
          fpdb = fpdb + (fp -> g)
        else {
          if (fpdb(fp) > g)
            fpdb = fpdb + (fp -> g)
        }
      }
      if (mode==0 |mode==2){
        val cp = pattern(a, false)
        if (!(cpdb contains cp))
          cpdb = cpdb + (cp -> g)
        else {
          if (cpdb(cp) > g)
            cpdb = cpdb + (cp -> g)
        }
      }

      // Find all valid moves from a state, and add them to the fringe
      if (g < maxCost) {
        makeNodes(a).foreach {
          case (a, _) => {
            if (!seen(a)) {
              val cost = g + 1
              val node = (cost, a)
              fringe += node
              seen += a
            }
          }
        }
      }
      fringe.isEmpty match { // If the fringe is empty
        case true => () //   No solution found; return Nil
        case _ => fringe.dequeue match { // Else dequeue a node
          case (g, a) => if (g <= maxCost) search(a, g) // search its children
        }
      }
    }

    search(initial, 0)

    /*println( “Fringe pattern database”)
  for (i<- fpdb) {
    println(i._1) ;println(i._2) }
  println( “Corner pattern database”)
  for (i<- cpdb) {
    println(i._1) ;println(i._2) }*/
  }

  // Generate either fringe or corner pattern for input state
  def pattern(s: State, fringe: Boolean): IndexedSeq[Any] = {
    var ptiles: Map[Pos, Tile] = Map()
    var sub: List[Int] = List()
    s match {
      case State(b, e) => b match {
        case Board(size, t) => {
          if (fringe) {
            sub = List.tabulate(2 * size - 1)(n => {
              if (n < size) n + 1 else (n - size + 1) * size + 1
            })
          }
          else {
            sub = List.tabulate(2 * size - 1)(n => {
              if (n < size) n + 1 else n + 2
            })
          }
          for (i <- 1 to size) {
            for (j <- 1 to size) {
              t get(i, j) match {
                case None => {}
                case Some(tile) => if (sub contains tile)
                  ptiles = ptiles + ((i, j) -> tile)
              }
            }
          }
          State(Board(size, ptiles), e).toBytes2(),deep
        }
      }
    }
  }

  // Non-additive pattern database heuristics
  // Three modes 1:fringe 2.corner 3.max(fringe,corner)
  def nonAdditive(s: State, mode: Int): Int = {
    var heuristics = 0;
    val (size,_)=s.toBoard()
    if ((mode==0 & fpdb.size==0) | (mode==1 & cpdb.size==0)) { 
      getNAPDB(goalState(size),validMoves,30,mode) }
    if (mode==2 & (fpdb.size==0 | cpdb.size==0)) {
      getNAPDB(goalState(size), validMoves, 30, mode) }
    if (mode == 0 | mode == 2) {
      val p = pattern(s, true);
      heuristics += fpdb(p);
    }
    if (mode == 1 | mode == 2) {
      val p2 = pattern(s, false);
      heuristics += cpdb(p2);
      if (heuristics < cpdb(p2)) heuristics= cpdb(p2)
    }
    heuristics
  }

}
