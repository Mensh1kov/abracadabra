import cats.Monad
import cats.syntax.all._
import cats.data.OptionT

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object MonadT extends App {

  val oft: OptionT[Future, Int] = OptionT.some(10)
  println(oft.map(_ + 10).value)

  class Toggle {
    def unary_+ = "on"
    def unary_- = "off"
    def unary_! = "qwe"
    def unary_? = "on or off"
  }
  val toggle = new Toggle
  println(-toggle) // off
  println(+toggle) // on
  println(toggle) // qwe


  def n(n: Int, v: Boolean) = {
    println(n)
    v
  }
  println((n(1, false) || n(2, true) && n(3, true)))



  trait Foo[F[_]] {
    def kek[A](a: A): F[A]
  }


}
