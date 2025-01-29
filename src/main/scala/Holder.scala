import scala.concurrent.{Await, Future}
import scala.concurrent.ExecutionContext.Implicits.global

object Holder extends App {
  case class TokenHolder(init: String) {
    @volatile private var data = init

    def set(newData: String): Unit = data = newData

    def current: String = data

  }

  val th = TokenHolder("Init token")

  def loadNewToken(th: TokenHolder): Future[Unit] = Future {
    th.set("new token")
  }

  def errorLoadToken(th: TokenHolder): Future[Unit] = Future {
    throw new Exception("error set token")
  }

  Future {
    while (true) {
      println(th.current)
      Thread.sleep(3000L)
    }
  }
  loadNewToken(th)
  errorLoadToken(th).recover(err => println(err))

  Thread.sleep(1000000L)

}
