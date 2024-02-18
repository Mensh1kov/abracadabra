import eu.timepit.refined._
import eu.timepit.refined.api.Refined
import eu.timepit.refined.auto._
import eu.timepit.refined.numeric._
import cats.Eq
import cats.instances.all._

object Refineddd extends App {
  val a: Int Refined Positive = 5

  import eu.timepit.refined.auto._

  val eqInstance: Eq[Int Refined Positive] = _ == _
  val result: Boolean                      = eqInstance.eqv(1, 2)
//
//  def foo(first: Int)(mid: Int*)(last: Int): Int = first + mid.sum + last
//  def foo2(first: Int)(mid: Int*): Int = first + mid.sum

//  println(foo(1)()(2))

  println(((`1`: 1) => `1`)(1))

  object Jopo {
    override def toString: String = "puk"
  }

  val h = 12
  val j = valueOf[h.type]

  println(j)

  trait Pol

//  case class Lol(a: Int, b: Int) extends Pol {
//    def get: this.a.type = this.b
//  }
//
//  def foo(a: Lol): a.type = a
//  val l                   = foo(Lol())

  trait Foo[+T]
  case class Bar[+T](t: T) extends Foo[T]
  case object Baz          extends Foo[Nothing]

  trait Monad[F[_]] {
    def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
    def pure[A](a: A): F[A]
    def map[A, B](fa: F[A])(f: A => B): F[B] = flatMap(fa)(f.andThen(pure))
  }

  object Monad {
    def apply[F[_]: Monad](): Monad[F] = implicitly[Monad[F]]
  }

  object MonadInst {

    implicit val monadOption = new Monad[Option] {
      override def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] = fa.flatMap(f)
      override def pure[A](a: A): Option[A]                                   = Some(a)
    }

    implicit val monadFoo = new Monad[Foo] {
      override def flatMap[A, B](fa: Foo[A])(f: A => Foo[B]): Foo[B] = fa match {
        case Bar(v) => f(v)
        case Baz    => Baz
      }
      override def pure[A](a: A): Foo[A] = Bar(a)
    }

  }

  implicit class MonadOps[F[_], A](private val fa: F[A]) extends AnyVal {
    def flatMap[B](f: A => F[B])(implicit m: Monad[F]): F[B] = implicitly[Monad[F]].flatMap(fa)(f)
    def mapp[B](f: A => B)(implicit m: Monad[F]): F[B]        = implicitly[Monad[F]].map(fa)(f)
  }

  import MonadInst._


  val opt = Bar(10)

  Monad.apply[Foo]().map(opt)(a => a + 1)
  val b: Foo[Int] = Baz
  println(b.mapp(_ + 12))

}
