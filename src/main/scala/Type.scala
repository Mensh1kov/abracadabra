import scala.util.Using

object Type extends App {
  class A {
    def foo = println("foo")
  }

  val a = new A
  val a2 = new A
  val aType: a.type = a
  // val aType: a.type = a2 // error

  val Aclass = a.getClass.getConstructor().newInstance()

  a.foo
  Aclass.foo

//  val cl = a.getClass[A]
//  class B[T <: A](inst: T) extends

//  val b = new B
//  b.foo



}
