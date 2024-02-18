object Sort extends App {
  trait Enum
  object One extends Enum
  object Two extends Enum
  object Three extends Enum

  case class Foo(en: Enum, age: Int)

  def getOrder(en: Enum): Int = en match {
      case One => 1
      case Two => 2
      case Three => 3
  }

  val list = List(Foo(One, 10), Foo(Two, 1), Foo(Three, 2), Foo(One, 4), Foo(Two, 1))

  def first(list: List[Foo]) = {
    list
    .groupBy(acc => getOrder(acc.en))
    .toList
    .sortBy { case (weight, _) => weight }
    .flatMap { case (_, list) =>
      list.sortBy(_.age)
    }
  }

  def second(list: List[Foo]) = {
    list.sortBy(acc => (getOrder(acc.en), acc.age))
  }

  val res1 = first(list)
  val res2 = second(list)

  println(res1)
  println(res2)


}
