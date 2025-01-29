package Law

import cats._

import scala.annotation.tailrec

sealed trait MyOption[+T]
case class MySome[T](value: T) extends MyOption[T]
case object MyNone extends MyOption[Nothing]

object MyOption {
  implicit val MyOptionMonadInst = new Monad[MyOption] {
    override def pure[A](x: A): MyOption[A] = MySome(x)
    override def flatMap[A, B](fa: MyOption[A])(f: A => MyOption[B]): MyOption[B] = fa match {
      case MySome(v) => f(v)
      case _ => MyNone
    }
    @tailrec
    override def tailRecM[A, B](a:  A)(f:  A => MyOption[Either[A, B]]): MyOption[B] = {
      f(a) match {
        case MySome(Right(v)) => MySome(v)
        case MySome(Left(v)) => tailRecM(v)(f)
        case MyNone => MyNone
      }
    }
  }
}


