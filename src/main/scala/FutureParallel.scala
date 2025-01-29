import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import cats.syntax.all._

import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
object FutureParallel extends App {
  def future1 = Future{
    println("future 1 sleep")
    Thread.sleep(300)
    println("furure 1 end")
    1
  }

  def future2 = Future{
    println("future 2 sleep")
    Thread.sleep(300)
    println("furure 2 end")
    2
  }

//  for {
//    (a, b) <- future1.zip(future2)
//  } yield a + b


  future1.map(foo)


  def foo: Int => Int = {
    Thread.sleep(100)
    println("call map")
    _ => 10
  }


//  val fff: Int => Int = foo.compose[Int](a => 12)

  Thread.sleep(1000)


  val m = Map("a" -> 12) + ("b" -> 13)
  println(m)

  case class Gam(name: String)

  val z = ZonedDateTime.parse("2023-02-15T07:42:41Z")

  println(z.format(DateTimeFormatter.ISO_DATE_TIME))



  Future.successful()
}
