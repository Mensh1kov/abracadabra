import cats.data._

object NonEmptyData extends App {
  val sum = NonEmptyList(1, 2 :: 3 :: Nil).reduce
  println(sum)

  case class MyPlus(a: Int, b: Int)

  MyPlus(1, 2) match {
    case a MyPlus b => println(a, b)
  }
}
