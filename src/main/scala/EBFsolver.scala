/*==================================================
    CS:4420 Artificial Intelligence
    Spring 2017
    Project
    Name: Pat Hawks, Ryan Larson, Rui Yang
  ==================================================*/

package EBFsolver
import scala.math._

class EBFsolver {
  def polyGen(N: Int, depth: Int): (Double => Double, List[Double]) = {
    var coeff = List(N * (-1.0))
    coeff = coeff ++ List.fill(depth)(1.0)

    def poly(x: Double): Double = {
      var sum = N * (-1.0)
      for (i <- 1 to depth) {
        sum = sum + pow(x, i);
      }
      return sum
    }

    return (poly, coeff)
  }

  def derivate(fnt: Double => Double, coeff: List[Double]): (Double => Double) = {
    var newCoeff: List[Double] = List.tabulate(coeff.length - 1)(n => (n + 1) * coeff(n + 1))

    def poly(x: Double): Double = {
      var sum = 0.0;
      for (i <- 0 to newCoeff.length - 1) {
        sum = sum + newCoeff(i) * pow(x, i)
      }
      return sum
    }

    return poly
  }

  def newtonMethod(N: Int, depth: Int, tole: Double): Double = {
    var x: Double = 1.0
    var (a, b) = polyGen(N: Int, depth: Int): (Double => Double, List[Double])
    var c = derivate(a, b)
    while (abs(a(x)) > tole) {
      x = x - a(x) / c(x)
    }
    return x
  }

  def EBF(N: Int, depth: Int): Double = {
    return newtonMethod(N, depth, 0.0001)
  }

}
