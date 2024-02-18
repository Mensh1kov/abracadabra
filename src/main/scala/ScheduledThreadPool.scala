import cats.effect.IO
import cats.effect.unsafe.implicits.global

import scala.concurrent.duration._
import java.util.concurrent.{Executors, TimeUnit}
import scala.concurrent.duration.FiniteDuration

object ScheduledThreadPool extends App {
  val sch = Executors.newScheduledThreadPool(10)
  def sleep(v: FiniteDuration): IO[Unit] =
    IO.async(
      cb => IO {
        sch.schedule (new Runnable { override def run(): Unit = {
          cb(Right(()))
        } }, v.toMillis,
          TimeUnit.MILLISECONDS);
        None
      }
    )

  sleep(10.seconds)
    .flatMap(_ => IO(println("boba")))
    .unsafeRunSync()
}
