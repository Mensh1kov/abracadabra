import cats.Monad

import cats.effect.{Async, Concurrent, GenTemporal, IO, IOApp, SyncIO, Temporal}
import cats.effect.std.Dispatcher
import cats.effect.unsafe.implicits.global
import cats.implicits.catsSyntaxApplicativeId
import fs2.{Chunk, Pipe, Pure, Stream}
import cats.syntax.all._

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.duration._
import scala.util.Random

object Streams extends App {
  val stream = Stream.range(1, 100)

  stream
    .map { i => println(s"publisher: $i"); i }
    .buffer(2)
    .map { i => println(s"buffer: $i"); i }
    .drain
    .compile
    .toList

}

object AsyncFoo extends App {

  implicit val ec = scala.concurrent.ExecutionContext.global

  val fib1 = IO.sleep(1000.milli).map(println).map(_ => 1)
  val fib2 = IO.sleep(2000.milli).map(println).map(_ => 2)
  val win = Concurrent[IO].race(fib1, fib2).unsafeRunSync()

  def fromFuture(future: => Future[Int]): IO[Int] = Async[IO].async_(callback => future.onComplete(tryRes => callback(tryRes.toEither)))

  fromFuture(Future{
    Thread.sleep(100)
    println("boba")
    10
  })
}


object Snake extends IOApp.Simple {

  def iterateRule(n: Int): Int = if (n < 10) n + 1 else 1

//  Stream(1,0).through().take(6).toList
  val stream = Stream.iterate[IO, Int](1)(iterateRule)

  val sliding = stream.sliding(3)
  val print = sliding.evalTap(c => IO.println(c.mkString_(" ")))
  val delay = print.evalTap(_ => IO.sleep(500.millis))


  override def run: IO[Unit] = delay.compile.drain
}
