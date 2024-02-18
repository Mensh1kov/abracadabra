import cats.Monad
import cats.effect.{Async, Concurrent, GenTemporal, IO, Sync, Temporal}
import cats.effect.std.Dispatcher
import cats.effect.unsafe.implicits.global
import cats.implicits.catsSyntaxApplicativeId
import fs2.{Pure, Stream}

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.duration._

object Streams extends App {
  val ioStream: Stream[IO, Int] =
    Stream
      .iterate(1)(_ + 1)
      .take(3).evalTap(i => IO(println(s"Num is $i")))

  val pureStream = Stream(4, 5, 6)
  val sumStream = (ioStream ++ pureStream.evalTap(i => IO(println(s"Pure is $i")))).evalTap(i => IO(println(s"All is $i")))
  println(sumStream.compile.toList.unsafeRunSync())

  val b = IO("asdf").start

  A

//  val pureStream = Stream(1, 2, 3).evalTap(println(1))
//  println(pureStream.toList)

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
