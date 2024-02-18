import shapeless._
import shapeless.syntax.typeable.typeableOps
import syntax.zipper._

object HList extends App {
  case class Foo()
  val list: String :: Int :: Foo :: HNil = "foo" :: 10 :: Foo() :: HNil

  println(list)

  list match {
    case a :: b :: c :: HNil => ()
  }

  import poly.identity
  list.map(identity)

  val l = 1 :: "foo" :: 3.0 :: HNil
  println(l.toZipper.right.put(("wibble", 45)).reify)

  trait A
  case class B() extends A
  case class C() extends A

  val a: A :: A :: HNil = B() :: C() :: HNil
  val b: Option[B :: C :: HNil] = a.cast[B :: C :: HNil]
}
