

object CorrectTailRecM extends App {
  trait Monad[F[_]] {
    def pure[A](a: A): F[A]
    def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
    def map[A, B](fa: F[A])(f: A => B): F[B]
  }

  object Inst {
    implicit val inst = new Monad[Box] {

      override def pure[A](a: A): Box[A] = Box(a)

      override def flatMap[A, B](fa: Box[A])(f: A => Box[B]): Box[B] = fa match {
        case Box(v) => f(v)
      }

      override def map[A, B](fa: Box[A])(f: A => B): Box[B] = flatMap(fa)(a => pure(f(a)))
    }
  }

  object Monad  {
    implicit class MonadOps[F[_], A](private val fa: F[A]) extends AnyVal {
      def flatMap[B](f: A => F[B])(implicit m: Monad[F]) = m.flatMap(fa)(f)
    }
  }


  case class Cat(name: String)
  case class Dog(name: String)
  case class Box[A](a: A)


  val cat = Cat("pupa")
  val dog = Dog("jopa")

  import Inst._
  val boxWithCat = inst.pure(cat)
  println(boxWithCat)

//  val boxWithDog = inst.flatMap(boxWithCat)(cat => Box(dog))
  import Monad._

  val boxWithDog = boxWithCat.flatMap(cat => Box(dog))
  println(boxWithDog)
}
