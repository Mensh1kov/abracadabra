object FlatMapAndOption extends App {
  trait A {
    val id: Int
  }
  case class B(id: Int, b: Int) extends A
  case class C(id: Int, c: Int) extends A

  val list = List(B(1, 10), C(2, 12))

  val a = list.find(_.id == 2).collect {case b: B => b.b}

 /* def f(a: Int): (T => T) forSome {type T} = o => {
    println(o)
    o
  }*/

//  val g = f(10)("123")




}
