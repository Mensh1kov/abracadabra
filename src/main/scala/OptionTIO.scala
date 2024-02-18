import cats.Monad
import cats.effect.{ExitCode, IO, IOApp}
import cats.syntax.all._
import mouse.all._


object OptionTIO extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
//    def list[F[_]: Monad]: List[F[Option[Int]]] = List(10.some.pure, none[Int].pure, 5.some.pure)
    val list2: List[IO[Option[Int]]] = List(none[Int].pure[IO])

    def func: Int => IO[Option[String]] = {
      case 10 => IO("ten".some)
      case 5 => IO("five".some)
    }

    list2
      .map(_.liftOptionT)
      .collectFirstSomeM(_.flatMapF(func).value)
      .liftOptionT
      .getOrElse("default")
      .map(v => {
        println(v)
        ExitCode.Success
        }
      )
  }
}
