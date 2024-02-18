import cats.Always

import scala.concurrent.Future
import cats.syntax.all._
import cats.implicits.catsStdInstancesForFuture
import scala.concurrent.ExecutionContext.global

object GatePrototype extends App {

  trait Service {
    def handle: String
  }

  case class FooService() extends Service {
    override def handle: String = "Foo"
  }

  case class BarService() extends Service {
    override def handle: String = "Bar"
  }

  case class Repo(service: Service) {
    def handle = service.handle
  }

//  trait Gate[T] extends T

  def toggle = true
//
//  val gate: Service = new Gate[Service] {}

  println()

  case class Foo(a: Int, b: Option[Int])

  def foo(f: Int): Future[Option[Foo]] = {
    f match {
      case 1 => Future.successful(Some(Foo(1, Some(2))))
      case _ => Future.successful(None)
    }
  }


  implicit val ec = global

  val l = List(1, 2)

  val a = l.traverse(foo(_).collect { case Some(Foo(_, Some(a))) => a -> a })
  val b = l.traverse(foo(_).map(_.flatMap(_.b)).collect { case Some(mode) => mode -> mode })
//  val c = l.traverse(foo(_).collect { case Some(f) if f.b.isDefined => f.b.get -> f.b.get })

  val bb = Future.successful[Option[Int]](None).collect {case Some(a) => a }
//  println(b, c)
  Thread.sleep(100)
  println(a, b)
  println(bb)
}
