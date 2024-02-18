import scala.concurrent.Future
import scala.concurrent.ExecutionContext
import scala.util.{Success, Failure}


object FutureError extends App {
  implicit val ec = ExecutionContext.global
  val opt: Option[Int] = None
  val f: Future[Option[Int]] = Future.failed(new Exception("Failed future"))

  val r1 = f.map(_.getOrElse(throw new Exception("My exception")))

  val r2 = f.transformWith {
    case Success(Some(value)) => Future.successful(value)
    case Success(_) => Future.failed(new Exception("My exception"))
    case Failure(exception) => Future.failed(exception)
  }


  println(r1)
  println(r2)

  val failure = Future.failed(new Exception())
  val futureOpt = Future.successful(Some(10))

//  futureOpt.flatMap(_.fold(failure)(a => Future.successful(a)))

}
