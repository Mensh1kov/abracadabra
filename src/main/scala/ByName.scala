object ByName {
  lazy val x: Int = {
    println("boba")
    10
  }

  println("---")

  def getX(): Int = {
    println("qwe")
    10
  }

  def f(a: => Int) = {
    println("-=-=")
    a
    a
  }

  f(getX())
  f(x)
}
