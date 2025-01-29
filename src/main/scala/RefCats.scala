import cats.effect.{Deferred, ExitCode, IO, IOApp, Ref}

object RefCats extends IOApp {


  val ref: Ref[IO, Int] = Ref.unsafe(10)
  val defer: Deferred[IO, Unit] = Deferred.unsafe


  val io1 = for {
    d <- Deferred[IO, Unit]
    _ <- d.complete()
    v <- d.get
    _ <- IO.println()
    _ <- d.complete()
  } yield ()

  val io2: IO[Unit] = for {
    a <- ref.get
    _ <- IO.println(a)
    _ <- ref.set(11)
    b <- ref.get
    _ <- IO.println(b)
  } yield ()

  ScheduledThreadPool


import java.util.concurrent.CyclicBarrier





  override def run(args: List[String]): IO[ExitCode] = io1 *> IO.pure(ExitCode.Success)
}
