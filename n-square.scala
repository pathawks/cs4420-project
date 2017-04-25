/*==================================================
    CS:4420 Artificial Intelligence
    Spring 2017
    Code taken from HW1 for representing Boards
    Names: Pat Hawks, Ryan Larson, Rui Yang
  ==================================================*/

// A position on the puzzle boad is encoded just as a pair of integers
// Each coordinate ranges from 1 to n, where n is the n of the puzzle
// position (1,1) is the top-left position in the board.
type Pos = (Int, Int)

// a puzzle tile is encoded just as an integers
type Tile = Int

// A board is encoded as a case class where the the cells are stored
// in an immutable map from position to tiles
case class Board( size: Int, tiles: Map[Pos,Tile] ) {

  // swap returns a new board idential to this except that
  // the values at positioin p1 and p2 are swapped
  def swap(p1: Pos, p2: Pos) = {
    val t1 = (tiles get p1, tiles get p2) match {
      case (Some(v1), Some(v2)) => tiles + (p1 -> v2) + (p2 -> v1)
      case (Some(v1), None    ) => (tiles - p1) + (p2 -> v1)
      case (None,     Some(v2)) => (tiles - p2) + (p1 -> v2)
      case _                    => tiles
    }
    new Board(size, t1)
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
        tiles get (i,j) match {
          case None    => s = s + "  "
          case Some(v) => s = s + " " + v
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
  override def toString = board.toString + " " + emptyPos + "\n"
}

// given n > 0, goalState generates a state with a board whose
// cells are ordered increasingly left-to-right and top to bottom
def goalState(n: Int) = {
  var m: Map[Pos,Tile] = Map()
  for (i <- 1 to n; j <- 1 to n) {
    println((i,j))
    m = m + ((i,j) -> (j + n * (i - 1)))
  }
  val lastPos = (n,n)
  State(Board(n, m - lastPos), lastPos)
}

/*
Operators in for the n-puzzle problem are encoded as singleton subclasses (objects)
of the abstract class Operator.
*/
abstract class Operator {
  def apply(s: State): Option[State]
}

// Left operator
case object Left extends Operator {
  // the apply method returns
  override def apply (s: State): Option[State] =
  s match {
    case State(_, (_, 1)) => None
    case State(b, (r, c)) => {
      val ep = (r, c - 1)
      Some( State(b.swap((r, c), ep), ep) )
    }
  }
}

case object Right extends Operator {
  // the apply method returns
  override def apply (s: State): Option[State] =
  s match {
    case State(_, (_, c)) if c == s.board.size => None
    case State(b, (r, c)) => {
      val ep = (r, c + 1)
      Some( State(b.swap((r, c), ep), ep) )
    }
  }
}

// Up operator
case object Up extends Operator {
  // the apply method returns
  override def apply (s: State): Option[State] =
  s match {
    case State(_, (1, _)) => None
    case State(b, (r, c)) => {
      val ep = (r - 1, c)
      Some( State(b.swap((r, c), ep), ep) )
    }
  }
}

// Down operator
case object Down extends Operator {
  // the apply method returns
  override def apply (s: State): Option[State] =
  s match {
    case State(_, (r, _)) if r == s.board.size => None
    case State(b, (r, c)) => {
      val ep = (r + 1, c)
      Some( State(b.swap((r, c), ep), ep) )
    }
  }
}

type Plan = List[Operator]

// given a state s and a plan p, attempt to execute p starting with s.
// Return the final state if each operator is applicable to the current state;
// otherwise, raise an exception.
def execute(s: State, p: Plan): State = {
  println(s.board)
  p match {
    case Nil      => s
    case op :: p1 => op(s) match {
      case None => throw new RuntimeException("Plan is infeasible")
      case Some(s1) => execute(s1, p1)
    }
  }
}

def validMoves(s: State): List[(State, Operator)] = {
  val moves = List(Up, Down, Left, Right)
  moves.foldLeft(Nil:List[(State, Operator)])( (l, op) => op(s) match {
    case Some(s1) => (s1, op) :: l
    case None => l
  })
}


def prepare_trial_board(): State = {
  val startMap = Map(
    (1,1) -> 1, (1,2) -> 2, (1,3) -> 3,
    (2,1) -> 4,             (2,3) -> 7,
    (3,1) -> 6, (3,2) -> 8, (3,3) -> 5
  )
  val startBoard = new Board(3, startMap)
  val start = new State(startBoard, (2, 2)) // start state for the 8-puzzle
  return start
}
