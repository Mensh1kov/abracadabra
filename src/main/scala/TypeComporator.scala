import scala.reflect.runtime.universe._

object TypeComporator extends App {


  trait Animal
  trait Name
  trait Age

//  def isType[T: TypeTag, G: TypeTag](t: G): Boolean =
//   typeOf[T]




  def typeComp[A, B](a: A, b: B)(implicit at: TypeTag[A], bt: TypeTag[B]): Boolean = at == bt

  trait A
  case class Foo() extends A
  case class Bar() extends A


  val a1: A = Foo()
  val a2: A = Bar()
  val res = typeComp(a1, a2)
  println(res)
}
