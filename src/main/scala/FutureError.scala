import FutureError.FooExcption
import cats.syntax.all._

import java.time.YearMonth
import java.util.Objects.requireNonNull
import scala.concurrent.{Await, ExecutionContext, ExecutionException, Future, Promise}
import scala.util.{Failure, Success, Try}
import scala.concurrent.duration.{Duration, DurationInt}
import scala.runtime.NonLocalReturnControl
import scala.util.control.{ControlThrowable, NonFatal}


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

  class FooExcption extends VirtualMachineError

  val fff = Future.failed(new VirtualMachineError{}).recover {
    case NonFatal(th) => {
      println("foo", NonFatal(th))
      println(th.getClass)
      "a"
    }
  }
  private[this] final def resolve[T](value: Try[T]): Try[T] =
    if (requireNonNull(value).isInstanceOf[Success[T]]) value
    else {
      val t = value.asInstanceOf[Failure[T]].exception
      if (t.isInstanceOf[ControlThrowable] || t.isInstanceOf[InterruptedException] || t.isInstanceOf[Error]) {
        if (t.isInstanceOf[NonLocalReturnControl[T @unchecked]])
          Success(t.asInstanceOf[NonLocalReturnControl[T]].value)
        else
          Failure(new ExecutionException("Boxed Exception", t))
      } else value
    }
  val ffff: Try[Int] = resolve(Failure(new NonLocalReturnControl("key", new NullPointerException()) {} ))

  val newf = ffff.map(_ * 2)

  println(s"EXX: ${newf}")

  Thread.sleep(100L)

  println(fff)
//  futureOpt.flatMap(_.fold(failure)(a => Future.successful(a)))
  println(YearMonth.parse("2025-12"))

  val aaa = for {
    a <- Future.successful(10)
    b <- Future.successful(11)
  } yield ()


  println("-------------------")
  val ffffff = Future {
    println(1)
    Thread.sleep(400L)
    1
  }

  ffffff.map({println(2); a => {println(3); a + 10}})

  val err = new Exception("foo")
  val foo = Future.failed[Int](err)
//  val bar = foo.recover {
//    case e if e.getMessage == "foof" => 10
//  }

//  bar.transform(println, {e => println(e); e})

  def foo[A <: Int] = {
    implicitly[Ordering[A]]
  }

  foo[1]

}
