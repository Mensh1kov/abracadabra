object FunctionsCompose extends App {
  def f(a: Int, b: Int): Int = a + b
  def h(a: Int): Int = a * 2

  val g = (f _).tupled andThen h _

  val ff = f _

  println(g(1, 2))
}
