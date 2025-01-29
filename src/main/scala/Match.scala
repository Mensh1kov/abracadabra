object Match extends App {
  case class Foo(a: Int, b: Int, c: Int, d: Int, e: Int)

  Foo(1, 2, 3, 4, 5)

  trait T
  case class A() extends T
  case class B() extends T

  val a: T = new A

  Match.a match {
    case A() | B() => println("A or B")
    case _ => println("other")
  }
}
