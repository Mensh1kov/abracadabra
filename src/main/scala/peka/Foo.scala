package peka

import cats.effect.IO
import cats.{Always, Monad, MonadThrow, Now}
import enumeratum.EnumEntry.Lowercase
import enumeratum.{Enum, EnumEntry}
import monocle.Lens
import monocle.Monocle.toAppliedFocusOps

import java.security.SecureRandom
import java.time.{Instant, LocalDate, LocalDateTime}
import java.util.Random
import java.util.concurrent.{Executors, ThreadLocalRandom, TimeUnit}
import scala.annotation.tailrec
import scala.collection.immutable.HashMap
import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try
import scala.util.matching.Regex

//import cats.Applicative
import cats.syntax.all._
//import cats.arrow.Arrow
//import cats.data.{NonEmptyList, OptionT}
//import cats.effect.unsafe.IORuntimeBuilder
//import cats.effect.{IO, Ref}

//import scala.collection.BuildFrom
//import scala.collection.immutable.TreeMap
//import cats.effect.unsafe.implicits.global
//import cats.syntax.all._
//import io.estatico.newtype.macros.newtype
//import org.scalatest.time.SpanSugar.convertIntToGrainOfTime

//import java.time.format.DateTimeFormatter
//import java.util.Optional
import scala.concurrent.ExecutionContext.Implicits.global
//import cats.instances.future._
//import scala.collection.concurrent.Map


import scala.concurrent.{ExecutionContext, Future}
import scala.util.Try
import scala.Integral.Implicits.infixIntegralOps


object Foo extends App {
  object Gooo
  object Hooo
  object Jooo

  implicit class F(val a: Int) extends AnyVal

  val a = new F(1)
  a.a
  case class Derevo[T]()
  def derevo[T]: Derevo[T] = Derevo()

  type And[A, B]



  def f[A](a: IterableOnce[A]) = a.iterator.foreach(println(_))




  f(List(1, 2, 3))
  f(Option(1))
  List
  def lestDerevo[T]: List[Derevo[_]] = List(derevo[T])
}

//object Application extends App {
//  // MARK#1
//
//  implicit val mapping: Map[String, Vector[String]] =
//    Map(
//      "2" -> Vector("a", "b", "c"),     // 2 -> a, b, c
//      "3" -> Vector("d", "e", "f"),     // 3 -> d, e, f
//      "4" -> Vector("g", "h", "i"),     // 4 -> g, h, i
//      "5" -> Vector("j", "k", "l"),     // 5 -> j, k, l
//      "6" -> Vector("m", "n", "o"),     // 6 -> m, n, o
//      "7" -> Vector("p", "q", "r", "s"),// 7 -> p, q, r, s
//      "8" -> Vector("t", "u", "v"),     // 8 -> t, u, v
//      "9" -> Vector("w", "x", "y", "z") // 9 -> w, x, y, z
//    )
//
//  implicit def phoneDigitToCombinations(in: String): Vector[String] = Solution.solution(in)
//
//  val input: String = StdIn.readLine()
//  val outputs: Vector[String] = phoneDigitToCombinations(input)
//
//  // MARK#2
//
//  println(outputs.mkString(","))
//}

//object Application2 extends App {
//  val s1 = StdIn.readLine().split(" ").map(_.toInt).sorted
//  val s2 = StdIn.readLine().split(" ").map(_.toInt).sorted
//
//
//  val result: String = s1.zip(s2).map {case (x, y) => Math.abs(x - y) }.sum.toString
//  println(result)
//  import cats.effect.std.Backpressure
//}