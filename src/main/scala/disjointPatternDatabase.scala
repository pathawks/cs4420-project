
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

class disjointPatternDatabase {
  
  var updb:Map[IndexedSeq[Any],Int]=Map()
  var dpdb:Map[IndexedSeq[Any],Int]=Map()
  var lpdb:Map[IndexedSeq[Any],Int]=Map()
  var rpdb:Map[IndexedSeq[Any],Int]=Map()

  def patternDPDB(s:State, vertical: Boolean): (IndexedSeq[Any], IndexedSeq[Any])={
    var tiles:Map[Pos,Tile]= Map()
    var tiles2:Map[Pos,Tile]= Map()  
    var upLeft:List[Int]= List()
      s match{
        case State(b,e)=> b match{
          case Board(size,t)=> {
            if (vertical){ 
              if (size==3) {upLeft= List(1,4,7,8) }
              else { upLeft=List(1,2,5,6,9,10,13,14)} }
            else { 
              if (size==3) {upLeft=List(1,2,3,4) }
              else {upLeft=List(1,2,3,4,5,6,7,8)}}
            for (i<-1 to size){ 
              for (j<-1 to size){
                t get (i,j) match {
                  case None=> {}
                  case Some(tile)=>  
                    if (upLeft contains tile)  tiles=tiles+((i,j)->tile)
                    else { tiles2=tiles2+((i,j)->tile) } 
                }
              }
            } 
            (State(Board(size,tiles), e).toBytes().deep, State(Board(size,tiles2), e).toBytes().deep)
        }
      }
    }
  }

  def getDPDB(initial:State, makeNodes: (State,List[Int],List[Int]) =>List[(State,Operator,Boolean,Boolean)],maxCost:(Int,Int,Int,Int), mode:Int):Unit = {
    
    def order(p:((Int,Int,Int,Int), State)):Int=p._1._1+ p._1._2+p._1._3+p._1._4
    val fringe = PriorityQueue.empty[((Int,Int,Int,Int), State)](
      Ordering.by(order).reverse)
    val seen = Set[State]()
    val (s,_)=initial.toBoard()
    var up:List[Int]=List()
    var left:List[Int]=List()
    if (mode==0|mode==2){
      if (s==3) 
        left=List(1,4,7,8)
      else {
        left= List(1,2,5,6,9,10,13,14)
      }
    }
    if (mode==1|mode==2){
      if (s==3) {
        up=List(1,2,3,4)}
      else { 
        up= List(1,2,3,4,5,6,7,8)
      }
    }
      
  def search(a:State, g:(Int,Int,Int,Int),mode:Int):Unit = { 
    if (mode==0 |mode==2){
      val (left,right)=patternDPDB(a, true)
      if (!( lpdb contains left))  
        lpdb= lpdb +(left->g._3)
      else { 
        if (lpdb(left)>g._1) 
          lpdb= lpdb +(left->g._3)
      }
      if (!( rpdb contains right))  
        rpdb= rpdb +(right->g._4)
      else { 
        if (rpdb(right)>g._2) 
          rpdb= rpdb +(right->g._4)
      }
    }

    if (mode==1 | mode==2){
      val (up,down)=patternDPDB(a, false)
      if (!( updb contains up))  
        updb= updb +(up->g._1)
      else { 
        if (updb(up)>g._1) 
          updb= updb +(up->g._1)}
      if (!( dpdb contains down))
        dpdb= dpdb +(down->g._2)
      else { 
        if (dpdb(down)>g._2) 
          dpdb= dpdb +(down->g._2)
      }
    }
    
    // Find all valid moves from a state, and add them to the fringe
    if (g._1<maxCost._1 | g._2<maxCost._2 | g._3<maxCost._3 | g._4<maxCost._4){
      makeNodes(a,up,left).foreach {
        case (a, _, inup,inleft) => {
          if (!seen(a)) {
            var (u,d,l,r)=g
            if (mode==0 |mode==2){
              if (inleft) l=l+1
              else {r=r+1}
            }
            if (mode==1 |mode==2){
              if (inup) u=u+1
              else {d=d+1}
            }
            val node = ((u,d,l,r),a)
            fringe += node
            seen += a
          }
        }
      }
    }
      
    fringe.isEmpty match {                // If the fringe is empty
      case true => ()                     //   No solution found; return Nil
      case _    => fringe.dequeue match {  // Else dequeue a node
        case (g, a) => {
          if (g._1<=maxCost._1 & g._2<=maxCost._2 & g._3<=maxCost._3 & g._4<=maxCost._4) 
            search(a, g,mode) //   search its children
        }
      }
    }
  }
  search(initial, (0,0,0,0), mode)
  //println("up"+updb.size)
  //println("down"+dpdb.size)
  //println("left"+lpdb.size)
  //println("right"+rpdb.size)
  }

  def disjointPDB(s:State, mode:Int, move:Int):Int={
    var heuristics=0; 
    val (size,_)=s.toBoard()
    if ((mode==0 & (lpdb.size==0|rpdb.size==0)) | (mode==1 & (updb.size==0|dpdb.size==0))) {
      println("Constuct disjoint pattern database")
      getDPDB(goalState(size),validMovesDPDB,(move,move,move,move),mode)
    }
    if (mode==2 & (lpdb.size==0 | rpdb.size==0| updb.size==0| dpdb.size==0)) { 
      println("Constuct disjoint pattern database")
      getDPDB(goalState(size),validMovesDPDB,(move,move,move,move),mode)
    }
    if (mode==0 | mode==2){
      val (l,r)=patternDPDB(s,true)
      heuristics += lpdb(l)+rpdb(r)
    }
    if (mode==1 | mode==2){
      val (u,d)=patternDPDB(s,false)
      val sum=updb(u)+dpdb(d)
      if (heuristics < sum) heuristics= sum
    }
    heuristics
  }  

}

