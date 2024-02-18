import shapeless._

object Coproducttt extends App {
  type ISB = Int :+: String :+: Boolean :+: CNil

  val isb = Coproduct[ISB]("foo")

  println(isb)


  println(isb.select[Int]) // None
  println(isb.select[String]) // Some("foo")


}
