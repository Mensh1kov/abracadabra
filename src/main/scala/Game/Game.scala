package Game

import cats.effect.{ExitCode, IO, IOApp}

import scala.io.StdIn
import scala.util.Random

object Game extends IOApp {

  def generateRaund(countBox: Int): IO[Int] = IO(Random.nextInt(countBox) + 1)

  def showRound(countBox: Int): IO[Unit] =  IO(println("|_| " * countBox))

  def inputAnswer(): IO[Int] = IO(StdIn.readInt())

  def startGame: IO[ExitCode] = for {
    expected <- generateRaund(3)
    _ <- showRound(3)
    actual <- inputAnswer()
    _ <- IO(if (actual == expected) println("Yes") else println("No"))
    _ <- IO(println((1 to 3).foldLeft("")((acc, i) => if (i == expected) acc + "|@| " else acc + "|_| ")))
  } yield ExitCode.Success

  override def run(args: List[String]): IO[ExitCode] = startGame

}
