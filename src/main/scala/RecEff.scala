import cats.effect.IO
import cats.implicits.catsSyntaxApplicativeId
import cats.effect.unsafe.implicits.global

object RecEff extends App {
  def f1(i: Int): IO[Int] = IO {
    if (i <= 0) i.pure[IO]
    else {
      val a = f1(i - 1)
      a
    }
  }.flatten

  def f2(i: Int): IO[Int] =
    if (i <= 0) i.pure[IO]
    else {
      val a = f2(i - 1)
      a
    }

  f1(1000000000).unsafeRunSync()
  f2(1000000000).unsafeRunSync()
}
