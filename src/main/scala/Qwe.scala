import cats.effect.IO

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AAAA(c: String) {
  def f = c
}

object AAAA {

  val o = for {
    x :: t <- Future(List(1,2,3))
  } yield 0
}

