object Comprehension extends App {
  val b = for {
    a <- List(1, 2, 3)
  } println(a)

  trait A

  Nil
  trait B extends A


  println(s"$$${10}")
}
