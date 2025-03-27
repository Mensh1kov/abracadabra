package metrics

import metrics.BaseSender.{BaseRequest, BaseResponse}
import metrics.Sender.{Request, Response}

trait Metric {
  var value: Int = 0
  def name: String
  def set(value: Int): Unit = this.value = value
  def get: Int = value
}

object Metric {
  def make(namee: String): Metric = new Metric {
    override def name: String = namee
  }
}
trait BaseSender {
  def send(r: BaseRequest): BaseResponse
}
object BaseSender {
  case class BaseRequest()
  case class BaseResponse()
}

trait Sender[I, O] {
  def send(request: Request[I]): Response[O]
}

object Sender {
  sealed trait Request[T]
  case class PureRequest[T](req: T, timeout: Int) extends Request[T]
  case class RetryRequest[T](wrapped: T, timeout: Int, attemp: Int, delay: Int) extends Request[T]

  case class Response[T](value: T)
}



object A extends App {
  val metric = Metric.make("foo")
  metric.set(10)
  println(metric.get)

}
