object Override extends App {
  trait Foo
  class Baz extends Foo

  trait Bar {
    def f(foo: Foo): Unit
  }

//  class BarImpl extends Bar {
//    override def f(foo: Baz): Unit = println(foo)
//  }
}
