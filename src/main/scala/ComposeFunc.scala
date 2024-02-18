object ComposeFunc extends App {
  def f(a: Int, b: Int): Int = a + b

  val g = (f _).tupled.andThen(_ + 7)

}
