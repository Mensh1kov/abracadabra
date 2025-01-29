import cats.Always

import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global
import cats.syntax.all._

import scala.concurrent.duration
import scala.concurrent.duration.DurationInt

object Race extends App {
  object Printer {
    def print[A](a: A) = println(a)
  }

  def genFuture(n: Int): Future[Unit] = Future(Printer.print(n))


  List(1, 2, 3, 4).map(genFuture)




  val faifF = Future[Int](throw new Exception("error"))
  val successF = Future(123)

  val a = (successF, successF).mapN((_, _))
  val b = (successF, successF).tupled

  Thread.sleep(2.seconds.toMillis)

  println(a, b)


  val config = Map.empty[String, String]
  case class Config(feld1: String, feld2: String, feldN: String)

}
