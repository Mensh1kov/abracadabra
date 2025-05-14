import scala.annotation.tailrec

object Tailrec extends App {

  def f(a: Int): Int = a + 1



  List(1, 2, 3).map(f)
  @tailrec
  def foo(acc: Int, n: Int): Int = if (n <= 0) acc else foo(acc + n, n - 1)

  foo(0, 10)
}
