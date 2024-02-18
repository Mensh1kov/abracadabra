import cats.Eval
import cats.syntax.all._
//import cats.implicits._
import mouse.all._

import scala.concurrent.Future

object MouseCats extends App {
  implicit val ec = scala.concurrent.ExecutionContext.global
  Future(true).ifF(1, 0)
  Eval.later()
  "1.2".parseFloat


  true.fold(None, Some())

  lazy val a = {
    println("cakkk==")
    1000
  }

  println("1qwe")
  a
  a

}
