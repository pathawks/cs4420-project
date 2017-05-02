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

/**
 * Non-additive Pattern Database Heuristics
 * @param initial state
 * @param makeNodes function that returns a list of all children for a given
 *        node, as well as the step to get from current node to that child
 * @param maxCost maximium number of moves from initial state to obtain pattern databases
 * @param mode subtypes of heuristics 0:fringe 1.corner 2.max(fringe,corner)
 */

object PatternDatabase {
  var fpdb:Map[IndexedSeq[Any],Int]=Map()  // Fringe Pattern Database
  var cpdb:Map[IndexedSeq[Any],Int]=Map()  // Corner Pattern Database

  // Generate above Pattern Databases
  def getNAPDB(initial: State, makeNodes: State => List[(State, Operator)], maxCost: Int, mode:Int): Unit = {
    val fringe = PriorityQueue.empty[(Int, State)](
      Ordering.by((_: (Int, State))._1).reverse)
    val seen = Set[State]()

    def search(a: State, g: Int, mode:Int): Unit = {
      // update fringe pattern databse
      if (mode==0 |mode==2){
        val fp = pattern(a, true)
        // if this pattern never seen before, update it into fringe database map
        if (!(fpdb contains fp))
          fpdb = fpdb + (fp -> g)
        else {
          // if this state has less number of moves than previous states having the same fringe pattern,
          // update the cost into fringe database map 
          if (fpdb(fp) > g)
            fpdb = fpdb + (fp -> g)
        }
      // generate corner pattern database
      }
      if (mode==0 |mode==2){
        val cp = pattern(a, false)
        // if this pattern never seen before, update it into corner database map
        if (!(cpdb contains cp))
          cpdb = cpdb + (cp -> g)
        else {
           // if this state has less number of moves than previous states having the same corner pattern,
          // update the cost into corner database map
          if (cpdb(cp) > g)
            cpdb = cpdb + (cp -> g)
        }
      }

      // Find all valid moves from a state, and add them to the state fringe queue, 
      // if its cost is less than maximium assigned cost. 
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
      fringe.isEmpty match { // If the fringe queue is empty
        case true => () //   No solution found; return Nil
        case _ => fringe.dequeue match { // Else dequeue a node
          case (g, a) => if (g <= maxCost) search(a, g, mode) // search its children
        }
      }
    }

    search(initial, 0, mode)

    /*println( “Fringe pattern database”)
  for (i<- fpdb) {
    println(i._1) ;println(i._2) }
  println( “Corner pattern database”)
  for (i<- cpdb) {
    println(i._1) ;println(i._2) }*/
  }

  // Generate either fringe or corner pattern for input state;
  // a pattern is a state with only partial tiles' position recorded, for 15-puzzle:
  //   fringe pattern          corner pattern
  //   1   2   3   4           1   2   3   4 
  //   5                           6   7   8
  //   9
  //   13        Empty                   Empty
  def pattern(s: State, fringe: Boolean): IndexedSeq[Any] = {
    var ptiles: Map[Pos, Tile] = Map()
    var sub: List[Int] = List()
    s match {
      case State(b, e) => b match {
        case Board(size, t) => {
          // obtain tiles to retain in fringe pattern  
          if (fringe) {
            sub = List.tabulate(2 * size - 1)(n => {
              if (n < size) n + 1 else (n - size + 1) * size + 1
            })
          }
          // obtain tiles to retain in corner pattern 
          else {
            sub = List.tabulate(2 * size - 1)(n => {
              if (n < size) n + 1 else n + 2
            })
          }
          // obtain the positions in input state of tiles among previous selected ones
          for (i <- 1 to size) {
            for (j <- 1 to size) {
              t get(i, j) match {
                case None => {}
                case Some(tile) => if (sub contains tile)
                  ptiles = ptiles + ((i, j) -> tile)
              }
            }
          }
          // return the byte represenation of this pattern 
          State(Board(size, ptiles), e).toBytes2().deep
        }
      }
    }
  }

  // Non-additive pattern database heuristics
  // Three modes 1:fringe 2.corner 3.max(fringe,corner)
  def nonAdditive(s: State, mode: Int): Int = {
    var heuristics = 0;
    val State(Board(size,tiles),_)=s
    // if fringe or corner database is empty, then generate corresponding one.
    if ((mode==0 & fpdb.size==0) | (mode==1 & cpdb.size==0)) { 
      getNAPDB(goalState(size),validMoves,30,mode) }
    // to calculate max heuristics between fringe and corner pettern database, generate both of them.
    if (mode==2 & (fpdb.size==0 | cpdb.size==0)) {
      getNAPDB(goalState(size),validMoves, 30, mode) }
    if (mode == 0 | mode == 2) {
      // get fringe pattern and its heuristic cost from the fpdb map
      val p = pattern(s, true);
      heuristics += fpdb(p);
    }
    if (mode == 1 | mode == 2) {
      // get corner pattern and its heuristic cost from the cpdb map
      val p2 = pattern(s, false);
      heuristics += cpdb(p2);
      if (heuristics < cpdb(p2)) heuristics= cpdb(p2)
    }
    heuristics
  }

}
