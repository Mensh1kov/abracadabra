//package bar
//
//
//import scala.concurrent.{ExecutionContext, Future}
//
//class Printer(implicit ec: ExecutionContext) {
//
//  def printF(text: String): Future[Unit] = Future {
//    print(text)
//  }
//
//  def printlnF(text: String): Future[Unit] = Future {
//    println(text)
//  }
//
//  def printFieldF(field: Field): Future[Unit] = Future {
//    (0 until  field.height).foldLeft("") { (acc1, x) =>
//      val r = (0 until field.width).foldLeft("") { (acc2, y) =>
//        val c = field.get(x, y) match {
//          case Right(MarkedCell(mark)) => " " + mark.toString + " "
//          case Right(EmptyCell) => "   "
//          case Left(_) => "ERR"
//        }
//        acc2 + c + "|"
//      }
//      acc1 + r.dropRight(1) + "\n" + ("---|" * field.width).dropRight(1) + "\n"
//    }
//  }.flatMap(s => printlnF(s.dropRight(3 * field.width + 3)))
//}
