object Lab_NM extends App {

  def r(n: Int): Double  = (1 to n).foldLeft(0.0) { case (acc, n) =>
    acc + 1 / (Math.pow(n, 2) + 1.7 * n + 0.8)
  }

  def r2(n: Int): Double = (1 to n).foldLeft(0.0) { case (acc, n) =>
    acc + (1 / (Math.pow(n, 2) + 1.7 * n + 0.8) - 1 / Math.pow(n, 2))
  }

  def r3(n: Int): Double = (1 to n).foldLeft(0.0) { case (acc, n) =>
    acc + (1.7 / Math.pow(n, 3) + (1 / (Math.pow(n, 2) + 1.7 * n + 0.8) - 1 / Math.pow(n, 2)))
  }

  def r4(n: Int): Double = (1 to n).foldLeft(0.0) { case (acc, n) =>
    acc + ((1.7 / Math.pow(n, 3) + (1 / (Math.pow(n, 2) + 1.7 * n + 0.8) - 1 / Math.pow(
      n,
      2
    ))) - 2.09 / Math.pow(n, 4))
  }

//  println(r(10000000))
//  println(r(40000))
//  println(Math.pow(Math.PI, 2) / 6 + r2(185))
//  println(Math.pow(Math.PI, 2) / 6 - 1.7 * 1.2020569032 + r3(141))
//  println(Math.pow(Math.PI, 2) / 6 - 1.7 * 1.2020569032 + 2.09 * Math.pow(Math.PI, 4) / 90 + r4(13))
//  println(1.36 - 2.09 * 1.7)

  def p(n: Int) = {
    println("исходный", r(n))
    println("a)", Math.pow(Math.PI, 2) / 6 + r2(n))
    println("b)", Math.pow(Math.PI, 2) / 6 - 1.7 * 1.2020569032 + r3(n))
    println(
      "c)",
      Math.pow(Math.PI, 2) / 6 - 1.7 * 1.2020569032 + 2.09 * Math.pow(Math.PI, 4) / 90 + r4(n)
    )
  }

  p(20)

  sealed trait A {
    val a: String
    def b: Int
  }

  case class AA(
    override val a: String,
    override val b: Int
  ) extends A

}
