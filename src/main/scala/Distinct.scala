object Distinct extends App {
  val l = List("a", "a", "b", "c", "c", "b")

  println(l.distinct)

  case class Foo(str: String)

  val ll = List(Foo("a"), Foo("a"), Foo("b"), Foo("c"), Foo("c"))

  println(ll.distinctBy(_.str))
}
