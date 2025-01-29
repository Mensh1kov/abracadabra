//package bar
//
//import cats.syntax.all._
//
//import scala.concurrent.{ExecutionContext, Future}
//import scala.io.StdIn
//import XOGame._
//import cats.effect.Sync
//
//case class XOGameConfig(height: Int, width: Int)
//
//class XOGame[F[_]: Sync](config: XOGameConfig) {
//  private val printer: Printer = new Printer()
//
//  private def gameLoop(field: Field): Future[Unit] = {
//    for {
//      _ <- printer.printFieldF(field)
//      (x, y, mark) <- input()
//    } yield field.mark(x, y, mark)
//  }.flatMap {
//    case Left(err) => printer.printlnF(err.getClass.getSimpleName) *> gameLoop(field)
//    case Right(field) => gameLoop(field)
//  }
//
//  private def input(): Future[(Int, Int, Mark)] = for {
//    line <- Future(StdIn.readLine())
//  } yield line match {
//    case inputRegex(x, y, mark) => (x.toInt, y.toInt, if (mark == "X") X else O)
//    case _ => throw new Exception(s"Invalid input: $line") // bad
//  }
//
//  def start: F[Unit] =
//    gameLoop(new FieldImpl(height = config.height, width = config.width))
//}
//
//object XOGame {
//  val inputRegex = raw"(\d+)\s+?(\d+)\s+?([XO])".r
//}
