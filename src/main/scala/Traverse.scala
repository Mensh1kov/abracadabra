import cats.data.OptionT
import cats.syntax.all._

import scala.concurrent.{Future, blocking}
import scala.concurrent.ExecutionContext.Implicits.global
object Traverse extends App {
  def f(n: Int): Future[String] = n match {
    case 1 => "one".pure[Future]
    case 2 => "two".pure[Future]
    case _ => throw new Exception("Unknow")
  }

  val l = List(1, 2, 3, 2, 1)
//
//  Thread.sleep(1000L)
//  println(l.traverse(f))



  def furure(n: Int): Future[Int] = Future {
    println(s"start $n")
    Thread.sleep(1000L)
    println(s"end ${n}")
    n
  }

  Future.traverse(List(1, 2, 3))(furure)
  List(1, 2, 3).map(furure).sequence


//  List(1, 2, 3).parTraverse(furure)


//  List(1, 2, 3).map(furure).sequence

//  List(furure(1), furure(2), furure(3)).sequence

  cats.Traverse[List]

  def g(n: Int): Future[Option[String]] = Future {
    println(s"start ${n}")
    Thread.sleep(1000L)
    println(s"end ${n}")
    n.toString.some
  }
//  val v1 = List(1,2,3).map(n => g(n).map(_.map(n -> _))).sequence.map(_.)

  def gg(n: Int): OptionT[Future, String] = OptionT(Future {
    println(s"start ${n}")
    Thread.sleep(1000L)
    println(s"end ${n}")
    n.toString.some
  })
//  val v2 = List(1,2,3)
//    .map(n => OptionT(g(n)).map(n -> _).value)
//    .sequence
//    .map(_.flatten.toMap)

//  val a = (g(1), g(2)).mapN((a, b) => Some("f00"))
//    .map(n => OptionT(g(n)).(n -> _).value)
//    .sequence
//    .map()

  Thread.sleep(2000L)

}

