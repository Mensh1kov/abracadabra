import C.{monoid1, Monoid}
import Import.Monoid.Aux

import scala.reflect.ClassTag

object Import extends App {

  val monoid2: Monoid.Aux[Int] = new Monoid {
    override type T = Int
    override def empty = 0
  }

  object Monoid {
    type Aux[G] = Monoid { type T = G }
  }

  implicit def classTag[V]: ClassTag[Monoid.Aux[V]] =
    new ClassTag[Aux[V]] { override def runtimeClass: Class[_] = ??? }

  monoid2 match {
    case m: Monoid.Aux[Int] => println(10)
    case _                  => println(20)
  }


}

object C {

  trait Monoid {
    type T
    def empty: T
  }

  val monoid1: Monoid { type T = String } = new Monoid {
    override type T = String
    override def empty = ""
  }

}
