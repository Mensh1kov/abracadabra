import cats.Show
import cats.kernel.Eq
import scala.reflect.runtime.universe._

import scala.language.existentials
import scala.reflect.ClassTag
import cats.syntax.all._

object Unapply extends App {
  case class A[T: ClassTag, +G: ClassTag](a: T, b: G) {
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


  case class Foo[+T](a: T)


  type PairOf[+T] = Product2[T, T]

  object PairOf {
    def unapply[T, A, B](t: (A, B)): Option[(T, T)] = t match {
      case (a: T, b: T) => Some(a, b)
      case _ => None
    }
  }

  trait Animal
  trait Name
  trait Age


  case class Dog() extends Animal with Name
  case class Cat() extends Animal with Age

  val pairOfAnimal: (Animal, Animal) = (Dog(), Dog())

//  (pairOfAnimal) match {
//    case (a: Cat, b: Cat) => println(s"cat tup")
//    case (a: Dog, b: Dog) => println(s"dog tup")
//    case pair@PairOf(b: Cat, a: Cat) => println(s"cat")
//    case pair@PairOf(_: Dog, _: Dog) => println(s"dog")
//    case _ => println("no")
//  }

//
//  (Foo("10"): Foo[Any]) match {
//    case a: Foo[Int] => println(s"int ${a}")
//    case a: Foo[String] => println(s"string ${a}")
//  }



  class Matsh[T: TypeTag] {
    def matsh[U: TypeTag](u: U): Boolean =  typeOf[U] <:< typeOf[T]
  }

  val matshCat = new Matsh[Animal with Age]
  val dog: Animal = Dog()
  println(matshCat.matsh(dog))





}
