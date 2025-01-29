import java.util.concurrent.{Executors, ThreadPoolExecutor}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt
import scala.concurrent.{Await, Future, Promise}
import scala.util.Success
//import cats.Show
//
//class Sandbox {
////  def foo[A, B](f: A => List[A], a: B) = f(a)
////  trait Animal {
////    type A = Int
////    val sound = "rrrr"
////  }
////  def f(a: Animal#A)
////
////  class Bird extends Animal { override val sound: String = "call"}
////  class Chicken extends Bird { override val sound: String = "call"}
////
////  val f1: Bird => String = (a: Animal) => a.sound
//
//
//}
//
//object Qwe {
//  sealed trait A {
//    def T[T]: T
//  }
//
//  object A {
//    def show[T <: A](t: T)(implicit s: Show[T]): String = s.show(t)
//
//    final class A1(v: Int, T: A1 = A1.this) extends A
//
//    final class A2(v: Int, T: A2 = A2.this) extends A
//
//    final class A3(v: Int, T: A3 = A3.this) extends A
//  }
//
//  import A._
//
//  implicit val a1Show: Show[A1] = Show.show(_ => "A1")
//  implicit val a2Show: Show[A2] = Show.show(_ => "A2")
//  implicit val a3Show: Show[A3] = Show.show(_ => "A3")
//
//
//  val a = new A1(10)
//  val aa = new A1(10)
//
//  def f(a: A): String =
//
//    A.show(a)
//
//}

package qwe {
  class A {
    protected[qwe] def f = 10
    private def g = 11
  }

  class B extends A {
    def b = super.f
  }
  object AA {
    def a = new A().f
  }
}

object Blocking extends App {
  import scala.concurrent._

  val ec = scala.concurrent.ExecutionContext.Implicits.global

  (0 to 100) foreach { n =>
    Future {
      println("starting Future: " + n)
      val r = blocking { Thread.sleep(3000); 1 }
      println("ending Future: " + n)
    }(ec)
  }


  Thread.sleep(3000)
}

object AAAAA extends App {
  val f = Future {
    println("start")
    Thread.sleep(100)
    println("finish")
    34
  }.map(_ * 2)

  def sin(a: Int) = {
    Thread.sleep(500)
    10
  }

  val ff = Future { 100 }.map(v => {
    Thread.sleep(1000)
    v * 2
  })

  println(ff)
  println(Await.result(ff, 2.second))

  println("boba")

  Thread.sleep(1000)
  f.foreach(r => println(r))
  println("end")

  Promise.fromTry(Success(10)).future

  locally {}
}
