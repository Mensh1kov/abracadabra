object Collections extends App {
  val c = scala.collection.mutable.ArraySeq(1, 2, 3, 4)
  List(1).lift
  println(c.lift(4))
  c(0) = 10
  println(c)
}
