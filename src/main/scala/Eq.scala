import cats.Eq
import cats.syntax.all._

object Eqqq extends App {


  class A
  case class B()

  val a = new A
  val b = new A

  val aa = new B
  val bb = new B

  Map

  println(a == b)
  println(aa == bb)

//  class Foo
//  class Bar
//
//  type @@[A, B] = A with B
//  type Id[Tag] = String @@ Tag
//
//
//  implicit def eq[A] = Eq.fromUniversalEquals[A]
//
//  val a: Id[Foo] = "qwe".asInstanceOf[Id[Foo]]
//  val b: Id[Bar] = "qwe".asInstanceOf[Id[Bar]]
//  println(a === a)
//  println(List[Int]().forall(_ => !true))
}
