import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.all._

import scala.concurrent.ExecutionContext.global
import java.util.concurrent.{Executors, ThreadPoolExecutor}
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._



object SchedulerForIO extends IOApp.Simple {
  val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(1))

  trait Scheduler {
//    def schedule[A](io: IO[A], duration: Duration): IO[A] =
//      io.delayBy(duration).
  }

  override def run: IO[Unit] = IO.unit
}
