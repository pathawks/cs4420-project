/*==================================================
    CS:4420 Artificial Intelligence
    Spring 2017
    Code taken from HW1 for representing Boards
    Names: Pat Hawks, Ryan Larson, Rui Yang
  ==================================================*/
package project

object Nsquare {
  // A position on the puzzle boad is encoded just as a pair of integers
  // Each coordinate ranges from 1 to n, where n is the n of the puzzle
  // position (1,1) is the top-left position in the board.
  type Pos = (Byte, Byte)

  // a puzzle tile is encoded just as an integers
  type Tile = Byte

  // A board is encoded as a case class where the the cells are stored
  // in an immutable map from position to tiles
  case class Board(size: Byte, tiles: Map[Pos, Tile]) {

    // swap returns a new board idential to this except that
    // the values at positioin p1 and p2 are swapped
    def swap(p1: Pos, p2: Pos) = {
      val t1 = (tiles get p1, tiles get p2) match {
        case (Some(v1), Some(v2)) => tiles + (p1 -> v2) + (p2 -> v1)
        case (Some(v1), None) => (tiles - p1) + (p2 -> v1)
        case (None, Some(v2)) => (tiles - p2) + (p1 -> v2)
        case _ => tiles
      }
      new Board(size, t1)
    }
   // convert the board contents into byte array
   def toBytes():Array[Byte]= {
     var s:Array[Byte]=Array()
      for (i <- 1 to size) {
        for (j <- 1 to size){
          tiles get (i.toByte, j.toByte) match {
            case None    => s ++= Array(' '.toByte)
            case Some(v) => s ++= Array(v.toByte)}
       }
     }
    s
   }
  /* Boards are converted to strings that pring like this:
  +-------+
  | 1 3 2 |
  | 4   7 |
  | 6 8 5 |
  +-------+
  */
    override def toString = {
      var b = " +"
      for (i <- 1 to size)
        b = b + "--"
      b = b + "-+\n"
      var s = b
      for (i <- 1 to size) {
        s = s + " |"
        for (j <- 1 to size)
          tiles get(i.toByte, j.toByte) match {
            case None => s = s + "  "
            case Some(v) => s = s + " " + v.toInt
          }
        s = s + " |\n"
      }
      s + b
    }
  }

  // A state is encoded as a case class with a board and a the position
  // of the empty cell in the board
  case class State(board: Board, emptyPos: Pos) {
    /* States are converted to strings that pring like this:
    +-------+
    | 1 3 2 |
    | 4   7 |
    | 6 8 5 |
    +-------+
    (2,2)
   */
    override def toString = board.toString() + " " + emptyPos + "\n"

    // states are converted into bytes array of board without empty position, for non-addivtive pattern databases
    def toBytes() = board.toBytes()
    // states are converted into bytes array of board followed by the empty position, for disjoint pattern databases
    def toBytes2() = board.toBytes() ++ Array(emptyPos._1.toByte) ++ Array(emptyPos._2.toByte)
  }

  // given n > 0, goalState generates a state with a board whose
  // cells are ordered increasingly left-to-right and top to bottom
  def goalState(size: Int) = {
    val n = size.toByte
    var m: Map[Pos, Tile] = Map()
    for (i <- 1 to n; j <- 1 to n) {
      // println((i,j))
      m = m + ((i.toByte, j.toByte) -> (j + n * (i - 1)).toByte)
    }
    val lastPos: Pos = (n, n)
    State(Board(n, m - lastPos), lastPos)
  }

 /*
 Operators in for the n-puzzle problem are encoded as singleton subclasses (objects)
 of the abstract class Operator.
*/
  abstract class Operator {
    def apply(s: State): Option[State]
    /** moving tiles operation that used for generating disjoint pattern databases,
      * check if this moves a tile in specific partition.
      * @param s State to search
      * @param list1 tiles in 'up' area of solution puzzle in horizatal partition
      * @param list2 tiles in 'left' area of solution puzzle in vertical partition
      * retrun a list of new states, operator, if the tile moved in up area, if the tile moved in left area
      */
    def apply(s: State, list1:List[Tile],list2:List[Tile]): Option[(State,Boolean,Boolean)]
  }

  // Left operator
  case object Left extends Operator {
    // the apply method returns
    override def apply(s: State): Option[State] =
      s match {
        case State(_, (_, 1)) => None
        case State(b, (r, c)) => {
          val ep = (r, (c - 1).toByte)
          Some(State(b.swap((r, c), ep), ep))
        }
      }
    override def apply (s: State, list1:List[Tile],list2:List[Tile]): Option[(State,Boolean,Boolean)] =  None
  }

  case object Right extends Operator {
    // the apply method returns
    override def apply(s: State): Option[State] =
      s match {
        case State(_, (_, c)) if c == s.board.size => None
        case State(b, (r, c)) => {
          val ep = (r, (c + 1).toByte)
          Some(State(b.swap((r, c), ep), ep))
        }      }
    override def apply (s: State, list1:List[Tile],list2:List[Tile]): Option[(State,Boolean,Boolean)] =  None
  }

  // Up operator
  case object Up extends Operator {
    // the apply method returns
    override def apply(s: State): Option[State] =
      s match {
        case State(_, (1, _)) => None
        case State(b, (r, c)) => {
          val ep = ((r - 1).toByte, c)
          Some(State(b.swap((r, c), ep), ep))
        }
      }
    override def apply (s: State, list1:List[Tile],list2:List[Tile]): Option[(State,Boolean,Boolean)] =  None
  }

  // Down operator
  case object Down extends Operator {
    // the apply method returns
    override def apply(s: State): Option[State] =
      s match {
        case State(_, (r, _)) if r == s.board.size => None
        case State(b, (r, c)) => {
          val ep = ((r + 1).toByte, c)
          Some(State(b.swap((r, c), ep), ep))
        }
      }
    override def apply (s: State, list1:List[Tile],list2:List[Tile]): Option[(State,Boolean,Boolean)] =  None
  }

  // left move operator used in disjoint pattern database generation
  case object LeftDPDB extends Operator {
    override def apply(s: State): Option[State]=None
    override def apply (s: State, list1:List[Tile], list2:List[Tile]): Option[(State,Boolean,Boolean)] =
      s match {
        case State(_, (_, 1)) => None
        case State(b, (r, c)) => {
          val ep = (r, (c - 1).toByte)
          b match {
            case Board(size,tiles)=> {
              val tile= tiles(ep)
              val (inup,inleft)=(list1 contains tile, list2 contains tile)
              Some( State(b.swap((r, c), ep), ep),inup,inleft)
            }
          }
        }
      }
  }

// right move operator used in disjoint pattern database generation
case object RightDPDB extends Operator {
  override def apply(s: State): Option[State]=None
  // the apply method returns
  override def apply (s: State, list1:List[Tile], list2:List[Tile]): Option[(State,Boolean,Boolean)] =
    s match {
      case State(_, (_, c)) if c == s.board.size => None
      case State(b, (r, c)) => {
        val ep = (r, (c + 1).toByte)
        b match {
          case Board(size,tiles)=> {
            val tile= tiles(ep)
            val (inup,inleft)=(list1 contains tile, list2 contains tile)
            Some( State(b.swap((r, c), ep), ep),inup,inleft)
    }}
  }}
}

// up move operator used in disjoint pattern database generation
case object UpDPDB extends Operator {
  // the apply method returns
  override def apply(s: State): Option[State]=None
  override def apply (s: State, list1:List[Tile], list2:List[Tile]): Option[(State,Boolean,Boolean)] =
    s match {
      case State(_, (1, _)) => None
      case State(b, (r, c)) => {
        val ep = ((r - 1).toByte, c)
        b match {
          case Board(size,tiles)=> {
            val tile= tiles(ep)
            val (inup,inleft)=(list1 contains tile, list2 contains tile)
            Some( State(b.swap((r, c), ep), ep),inup,inleft)
          }
        }
      }
    }
  }

// down move operator used in disjoint pattern database generation
case object DownDPDB extends Operator {
  // the apply method returns
  override def apply(s: State): Option[State]=None
  override def apply (s: State, list1:List[Tile], list2:List[Tile]): Option[(State,Boolean,Boolean)] =
    s match {
      case State(_, (r, _)) if r == s.board.size => None
      case State(b, (r, c)) => {
        val ep = ((r + 1).toByte, c)
        b match {
          case Board(size,tiles)=> {
            val tile= tiles(ep)
            val (inup,inleft)=(list1 contains tile, list2 contains tile)
            Some( State(b.swap((r, c), ep), ep),inup,inleft)
          }
        }
      }
    }

}

  type Plan = List[Operator]

  // given a state s and a plan p, attempt to execute p starting with s.
  // Return the final state if each operator is applicable to the current state;
  // otherwise, raise an exception.
  def execute(s: State, p: Plan): State = {
    // println(s.board)
    p match {
      case Nil => s
      case op :: p1 => op(s) match {
        case None => throw new RuntimeException("Plan is infeasible")
        case Some(s1) => execute(s1, p1)
      }
    }
  }


  /**
    * Find a List of all States that are one Operator away from a given State s
    * This is used as our makeNodes function for A* and the generation of non-additive pattern databases
    *
    * @param s State to search
    * @return List of States reachable from current state, and the Operator that
    *         moves from s to that State
    */
  def validMoves(s: State): List[(State, Operator)] = {
    val moves = List(Up, Down, Left, Right)
    moves.foldLeft(Nil: List[(State, Operator)])((l, op) => op(s) match {
      case Some(s1) => (s1, op) :: l
      case None => l
    })
  }

  /**
    * Find a list of valid new states, corresponding operator from a given state s
    * This is used as our makeNodes function for generation of disjoint pattern databases
    *
    * @param s State to search
    * @param list1 tiles in 'up' area of solution puzzle in horizatal partition
    * @param list2 tiles in 'left' area of solution puzzle in vertical partition
    * retrun a list of new states, operator, if the tile moved in up area, if the tile moved in left area
    */
  def validMovesDPDB(s: State, list1:List[Tile],list2:List[Tile]): List[(State,Operator,Boolean,Boolean)] = {
    val moves = List(UpDPDB, DownDPDB, LeftDPDB, RightDPDB)
    moves.foldLeft(Nil:List[(State, Operator, Boolean,Boolean)])( (l, op) => op(s,list1,list2) match {
      case Some((s1,inup, inleft)) => (s1, op,inup,inleft) :: l
      case None => l
    })
  }

  def prepare_trial_board(): State = {
    val startMap: Map[Pos, Tile] = Map(
      (1.toByte, 1.toByte) -> 1.toByte, (1.toByte, 2.toByte) -> 2.toByte, (1.toByte, 3.toByte) -> 3.toByte,
      (2.toByte, 1.toByte) -> 4.toByte, (2.toByte, 3.toByte) -> 7.toByte,
      (3.toByte, 1.toByte) -> 6.toByte, (3.toByte, 2.toByte) -> 8.toByte, (3.toByte, 3.toByte) -> 5.toByte
    )
    val startBoard = new Board(3.toByte, startMap)
    val start = new State(startBoard, (2.toByte, 2.toByte)) // start state for the 8-puzzle
    return start
  }

  def cost(s: State, o: Operator) = {1};

}
