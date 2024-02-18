import cats.Show
import cats.kernel.Eq

import scala.language.existentials
import scala.reflect.ClassTag
import cats.syntax.all._

object Unapply {
  case class A[T: ClassTag, G: ClassTag](a: T, b: G) {
    def isEmpty = {
      println("isAmpty")
      false
    }
    def get = {
      println("get")
      this
    }
    def _1 = {
      println("_1")
      a
    }
    def _2 = {
      println("_2")
      b
    }
  }
  class W[T](c: T) {
    def get = c
    def isEmpty = c == null
  }

  object A {
    def unapply[T, G](x: A[T, G]): A[T, G] = x
  }

  object AAAA {
    def unapply(x: Any) = new A(1, 2)
  }

  def main(args: Array[String]): Unit = {
    new A(10, "String") match {
      case A(a, b) => println(a)
      case f: A[Int, int] => println("foo")
      case _ => println("invalid")
    }
  }
}
