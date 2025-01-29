import cats.Monoid
import cats.arrow.Arrow
import cats.data.Kleisli
import cats.effect.IO
import cats.syntax.all._
import cats.instances.option._

object Arroww extends App {
  case class INN(num: Int)
  case class SNILS(num: Int)
  case class Passport(serial: Int, num: Int)
  case class Documents(inn: Option[INN], snils: Option[SNILS], passport: Option[Passport])
  case class User(id: User, firstName: String, lastName: String, age: Int, documents: Documents)


  def log(data: String): Option[Unit] = ???

  def getINN(userId: String): Option[INN] = ???
  def getSNILS(userId: String): Option[SNILS] = ???
  def getPassport(userId: String): Option[Passport] = ???


  def updateAge(currentAge: Int): Int = ???
  def makeUser(inn: INN, snils: SNILS, passport: Passport): User = ???
  val userId = "123"



  val parse: Kleisli[Option,String,Int] =
    Kleisli((s: String) => if (s.matches("-?[0-9]+")) Some(s.toInt) else None)

  val reciprocal: Kleisli[Option,Int,Double] =
    Kleisli((i: Int) => if (i != 0) Some(1.0 / i) else None)

  val parseAndReciprocal: Kleisli[Option,String, Double] =
    reciprocal <<< parse

  println(parse.run("qwer"))


  def combine[F[_, _]: Arrow, A, B, C](fab: F[A, B], fac: F[A, C]): F[A, (B, C)] =
    Arrow[F].lift((a: A) => (a, a)) >>> (fab *** fac)

  val mean: List[Int] => Double =
    combine((_: List[Int]).sum, (_: List[Int]).size) >>> {case (x, y) => x.toDouble / y}

  val variance: List[Int] => Double =
    // Variance is mean of square minus square of mean
    combine(((_: List[Int]).map(x => x * x)) >>> mean, mean) >>> {case (x, y) => x - y * y}

  val meanAndVar: List[Int] => (Double, Double) = combine(mean, variance)


  val headK = Kleisli((_: List[Int]).headOption)
  val lastK = Kleisli((_: List[Int]).lastOption)

  val headPlusLast = combine(headK, lastK)

  case class Fee[F[_], A, B, C](a: A => F[B])
  def foo[F[_, _], A, B](f: F[A, B]): Unit = ()

  type F[A, B] = Fee[Option, A, B, Int]
  foo(Fee((a: Int) => Option(a)))
}
