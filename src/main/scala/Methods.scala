object Methods extends App {
  implicit class Radian(d: Double) {
    def r: Double = d * 180 / Math.PI
  }
  val root = 0.0711164136609064
  val eps = 0.5 * Math.pow(10, -5)

  def f(x: Double): Double = 1 + Math.sin(x) - 1.15 * Math.pow(Math.E, -x)
  def `f'`(x: Double): Double = Math.cos(x) + 1.15 * Math.pow(Math.E, -x)



  def methodNewton(x0: Double): (Double, Int) = {
    val m = `f'`(Math.PI / 6)
//    println(m, "0=")
    val M = -1.13925
//    println(-(Math.sin(0.151243042576346) + 1.15*Math.pow(Math.E, -0.151243042576346)))

    def iter(Xn: Double): Double = Xn - f(Xn)/`f'`(Xn)

    def loop(Xn: Double, n: Int = 1): (Double, Int) = {
      val `Xn+1` = iter(Xn)
//      if (Math.abs(M/(2*m) * Math.pow(`Xn+1` - Xn, 2)) < eps) (`Xn+1`, n)
      if (n > 1) (`Xn+1`, n)
      else loop(`Xn+1`, n + 1)
    }
    loop(x0)
  }

//  def g(x: Double): Double = Math.log(1.15 / (1 + Math.sin(x)))
//  def `g'`(x: Double): Double = - Math.cos(x) / (1 + Math.sin(x))

  def g(x: Double): Double = x - 0.5 * (1 + Math.sin(x) - 1.15 * Math.pow(Math.E, -x))
  def `g'`(x: Double): Double = 1 - 0.5 * (Math.cos(x) + 1.15 * Math.pow(Math.E, -x))

  def simpleIteration(x0: Double): (Double, Int) = {
    val q = Math.max(Math.abs(`g'`(x0)), Math.abs(`g'`(Math.PI / 6)))
    val x1 = g(x0)
    println(q, "q")

    def loop(Xn: Double, n: Int = 1, c: Double = Math.abs(x0 - x1) / (1 - q)): (Double, Int) = {
      val `Xn+1`  = g(Xn)
      println("==", Math.abs(f(`Xn+1`) - f(root)) < eps)
//      if (Math.pow(q, n) * Math.abs(x0 - x1) / (1 - q) <= eps) {
      if (c * q <= eps) {
        (`Xn+1`, n)
      }
//      if (n > 5) (`Xn+1`, n)
      else loop(`Xn+1`, n + 1, c * q)
    }

    println(scala.math.log(eps * (1 - q) / Math.abs(x0 - x1)) / scala.math.log(q))
    println(x1)
    loop(x0)
  }
  println(methodNewton(0))
  println(simpleIteration(0))
}
