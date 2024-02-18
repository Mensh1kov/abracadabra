object Extends extends App {

  trait Spam {
    def f: Unit
  }
  trait Foo extends Spam {
    override def f = println("Foo")
  }
  trait Bar extends Spam {
    override def f = println("Bar")
  }

  trait Baz extends Foo with Bar

  val a = new Baz {}
  a.f
}
