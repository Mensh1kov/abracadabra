import scala.concurrent.Future

object SuccessAndFalse extends App {

  import scala.concurrent.duration.Duration
  import scala.concurrent.{Await, ExecutionContext, Future}
  import cats.syntax.all._

  implicit val ec = ExecutionContext.global

  val talk = Seq(
    Future {
      Thread.sleep(1000)
      "red"
    },
    Future.failed(new RuntimeException("exception1")),
    Future.successful("blue"),
    Future.failed(new RuntimeException("exception2")),
    Future.successful("green"),
    Future.failed(new RuntimeException("exception3"))
  )

  val resultFuture: Future[(Seq[Throwable], Seq[String])] = talk.traverse(_.attempt).map(_.separate)

  val result = Await.result(resultFuture, Duration.Inf)
  println(result)

}
