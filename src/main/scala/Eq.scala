import cats.Eq
import cats.syntax.all._

object Eqqq extends App {
  class Foo
  class Bar

  type @@[A, B] = A with B
  type Id[Tag] = String @@ Tag


  implicit def eq[A] = Eq.fromUniversalEquals[A]

  val a: Id[Foo] = "qwe".asInstanceOf[Id[Foo]]
  val b: Id[Bar] = "qwe".asInstanceOf[Id[Bar]]
  println(a === a)
  println(List[Int]().forall(_ => !true))
}
