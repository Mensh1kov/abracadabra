import scala.concurrent.{ExecutionContext, Future}

object RetryForFuture {
  implicit val ec = scala.concurrent.ExecutionContext.global

  trait Retry {

    def makeWithRetry[A](future: () => Future[A], count: Int = 0)(
      implicit
      ec: ExecutionContext
    ): Future[A]

  }

  class FutureRetry extends Retry {

    override def makeWithRetry[A](
      future: () => Future[A],
      count: Int
    )(
      implicit
      ec: ExecutionContext
    ): Future[A] = future().recoverWith {
      case _ if count > 0 => makeWithRetry(future, count - 1)
    }

  }

  def f(): Future[Int] = Future {
    println("hello")
    throw new Exception()
    10
  }

  val retryer = new FutureRetry()

  retryer.makeWithRetry(f, 2)
}
