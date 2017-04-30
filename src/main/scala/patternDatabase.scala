
import java.io._
import scala.collection.mutable.PriorityQueue
import scala.collection.mutable.Set

var patternCost:Map[State,Int]=Map()

def generatePDB(initial:State, makeNodes: State =>List[(State,Operator)],maxCost:Int):Unit = {
  var patternCost:Map[State,Int]=Map()
  val fringe = PriorityQueue.empty[(Int, State)](
    Ordering.by((_: (Int, State))._1).reverse)
  val seen = Set[State]()
  var max = 0

  def search(a:State, g:Int):Unit = { 
    println(a)
    val p=pattern(a)
    println(p)
    println(g)
    if (!(patternCost contains p))  patternCost= patternCost +(p->g)
    else { if (patternCost(p)>g) patternCost= patternCost +(p->g)}
    // Find all valid moves from a state, and add them to the fringe
    if (g<maxCost){
    makeNodes(a).foreach {
      case (a, _) => {
        if (!seen(a)) {
          val cost = g + 1
          val node = (cost, a)
          if (cost > max)
            max = cost
          fringe += node
          seen += a
        }
      }
    }}
    fringe.isEmpty match {                // If the fringe is empty
      case true => ()                     //   No solution found; return Nil
      case _    => fringe.dequeue match {  // Else dequeue a node
        case (g, a) => if (g<=maxCost) search(a, g) // search its children
      }
    }
  }
  search(initial, 0)
  for (i<- patternCost) {println(i._1) ;println(i._2)}
}

def pattern(s:State): State={
  var ptiles:Map[Pos,Tile]=Map()
  s match{
    case State(b,e)=> b match{
      case Board(size,t)=> { 
        val fringe=List.tabulate(2*size-1)(n=>{if (n<size) n+1 else (n-2)*size+1 })
        for (i<-1 to size){ for (j<-1 to size){
          t get (i,j) match {
           case None=> {}
           case Some(tile)=>  
            if (fringe contains tile) ptiles=ptiles+((i,j)->tile)}}}
            State(Board(size, ptiles), e)
     }
   }
  }
}

def fringePattern(s:State):Int={
    val p=pattern(s)
    patternCost get p
}
