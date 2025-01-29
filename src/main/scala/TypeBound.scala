object TypeBound extends App {
  trait Foo
  class Bar extends Foo
  class Baz extends Bar

  type A >: Foo


  implicit val strToInt: String => Int = _.length


  def f[A <% Int](a: A): Int = a * 2
  def f2[A](a: A)(implicit aToInt: A => Int): Int = a * 2

  def foo2[T >: Int](x: T) = println(x)

  println(f("123"))
  println(f2("123"))


  trait AA
  trait BB extends AA

  def g[T <: Option[AA]](t: T): Option[AA] = t

  g(Option.empty[BB])

}
