import cats.data.Chain
import cats.syntax.all._

object ChainCats extends App {
  val ch: Chain[Int] = Chain(1, 2, 2) ++ Chain.one(2)

  List(1, 2, 3) ++ List(4, 5, 6)
//  def foo2[Bar >: T >: Foo](x: T) = println(x)
}
