/*==================================================
    CS:4420 Artificial Intelligence
    Spring 2017
    Heuristics
    Name: Pat Hawks, Ryan Larson, Rui Yang
  ==================================================*/

def dummy(t:Any) = {0}

// Manhatta distance heuristc function
def Manhatta(s:Int, tiles:Map[Pos,Tile]):Int={
     var sum=0
     for (pair<-tiles){
         val ((row,col),tile)=pair
         val goalRow=ceil(1.0*tile/s)
         val goalCol=tile-(goalRow-1)*s
         sum+=abs(row-goalRow.toInt)
         sum+=abs(col-goalCol.toInt)}
     sum
}

// Linear-conflict heuristic function
def linearConflict(s:Int, tiles:Map[Pos,Tile]):Int={
    var sum= Manhatta(s,tiles)
    def pos(pair:((Int,Int),Int))={
        val ((r,c),t)=pair;
        var gr=ceil(1.0*t/s)
        (r,c,t,gr,t-(gr-1)*s)
    }
    for (pair<-tiles){
        val (row,col,tile,goalRow,goalCol)=pos(pair)
        if (row==goalRow){
            for (sameRow<-tiles.filterKeys(k=>(k._1==row & k._2<col))){
                val (row2,col2,tile2,goalRow2,goalCol2)=pos(sameRow)
                if (goalRow2==row & (col2-col)*(goalCol2-goalCol)<0) sum+=2}
        }
        if (col==goalCol){
            for (sameCol<-tiles.filterKeys(k=>(k._1<row & k._2==col))){
                val (row2,col2,tile2,goalRow2,goalCol2)=pos(sameCol)
                if (goalCol2==col & (row2-row)*(goalRow2-goalRow)<0) sum+=2}
        }
    }
    sum
}
