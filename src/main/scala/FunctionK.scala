//import cats.arrow.FunctionK
//import cats.syntax.all._
//import cats.syntax.arrow
//import cats.~>
//import scala.reflect.runtime.universe._

object FunctionK extends App {
  trait FunktionK[F[_], G[_]] {
    def _apply(f: F[Nothing]): G[Nothing]

    def apply[A](fa: F[A]): G[A] = _apply(fa.asInstanceOf[F[Nothing]]).asInstanceOf[G[A]]
  }

  type ~>[F[_], G[_]] = FunktionK[F, G]

  val fk: List ~> Option = a => None // problem :(

  val a = fk(List(1 ,2 ,3))
  val b = fk(List("a", "b"))

  println(a)
  println(b)

}
