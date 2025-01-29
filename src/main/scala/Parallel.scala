import cats.effect.IO
import cats.effect.unsafe.implicits.global
import cats.syntax.all._
import cats.instances.EitherInstances

import scala.concurrent.Future
//import scala.concurrent.ExecutionContext.Implicits.global

object Parallel extends App {
  def task(n: Int): Int = {
    if (n == 2) throw new Exception
    println(s"start task by $n")
    Thread.sleep(300L)
    println(s"end task by $n")
    n
  }

  val future1 = IO(task(1))
  val future2 = IO(task(2))

  (future1, future2).mapN(_ + _).unsafeRunSync()

  val a = (Right(10): Either[String, Int], Left("foo"): Either[String, Int], Left("bar"): Either[String, Int]).mapN(_ + _ + _)
  println(a)
}
