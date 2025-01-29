import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits.catsSyntaxApplicativeId

import scala.concurrent.duration._

object Fibers extends IOApp {

  val io = for {
    fiber <- (IO.sleep(3.seconds) *> IO.println("Hello after 3 sec") *> IO.raiseError[Int](new Exception())).start
    _ <- IO.println("Bar")
    _ <- fiber.cancel
    res <- fiber.joinWith(IO.pure(10))
    _ <- IO.println(s"Out from fiber $res")
  } yield ()

  override def run(args: List[String]): IO[ExitCode] = io *> IO.pure(ExitCode.Success)


}
