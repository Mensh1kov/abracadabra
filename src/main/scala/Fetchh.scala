import cats._
import cats.data.NonEmptyList
import cats.effect._
import cats.effect.unsafe.implicits.global
import cats.implicits._
import fetch._

import scala.concurrent.duration.DurationInt

object Fetchh extends App {
  Math.abs(10)
  def latency[F[_] : Sync](milis: Long): F[Unit] =
    Sync[F].delay(Thread.sleep(milis))

  object UnbatchedToString extends Data[Int, String] {
    def name = "Unbatched to string"

    def source[F[_]: Async] = new DataSource[F, Int, String] {
      override def data = UnbatchedToString

      override def CF = Concurrent[F]

      override def fetch(id: Int): F[Option[String]] =
        CF.delay(println(s"--> [${Thread.currentThread.getId}] One UnbatchedToString $id")) >>
          latency(100) >>
          CF.delay(println(s"<-- [${Thread.currentThread.getId}] One UnbatchedToString $id")) >>
          CF.pure(if (id == 1) none else id.toString.some)
    }
  }

  def unbatchedString[F[_]: Async](n: Int): Fetch[F, Option[String]] =
    Fetch.optional(n, UnbatchedToString.source)

//  def fetchUnbatchedThree[F[_] : Async]: Fetch[F, String] =
//    unbatchedString(1) *> unbatchedString(1) *> unbatchedString(1)

  val a = for {
    (cache, res1) <- Fetch.runCache[IO](unbatchedString(2))
    res2 <- Fetch.run[IO](unbatchedString(2), cache)
  } yield (res1, res2)

  a.unsafeRunTimed(5.seconds)
//  Fetch.run[IO](fetchUnbatchedThree).handleError(_ => "asd").unsafeRunTimed(5.seconds)


}
