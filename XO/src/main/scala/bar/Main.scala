package bar

import cats.effect.{IO, IOApp}

object Main extends IOApp.Simple {
  val run = XOGameServer.run[IO]
}
