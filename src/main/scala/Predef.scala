

object Predef extends App {
  class A
  class B extends A
  class C extends B

  def f[T](t: T)(implicit a: B <:< T) = ()

  f(new A)(implicitly[B <:< B])

  def g[T](t: T)(implicit a: =:=[B, T]) = ()

  g(new B)

  val b: Int = 10
  val u: Null = null

  // Пример применения

  class Foo[T] {

    def foo(x: T)(
      implicit
      evidence: T <:< String
    ): String = evidence(x)

  }


  1 :: 2 :: 3 :: Nil

  def |+|(a: Int, b: Int) = ()

//  case class Foo[A, B](a: A, b: B)
//
//  type IntFoo = Int Foo Int // инфиксная запись для типов

//  val intFoo: IntFoo = 1 Foo 2 // не работает

//  Foo(1, 2) match {
//    case a Foo b => () // инфиксной записи в PM
//  }


}

