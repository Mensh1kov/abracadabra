import cats.Id
import monocle.Traversal
import monocle.syntax.all._
import cats.syntax.all._

object Monocle extends App {
  case class Popa(size: Int)
  case class User(name: String, age: Int, addres: Option[String], popa: Popa)

  val user = User("asdfs", 123, None, Popa(100))
  println(user.copy(popa = user.popa.copy(90)))

  println(user.focus(_.popa.size).modify(_ - 10))


//  val user = User("Boba", 20, Some("qwd"))

  println(user.focus(_.age).modify(_ + 10))
  println(user.focus())
  println(user)

  val l = Traversal.fromTraverse[List, Int]


  println(l.replace(0)(List(1, 2 ,3, 4)))


  case class Point(id: String, x: Int, y: Int, z: Int)


  val points = Traversal.apply3[Point, Int](_.x, _.y, _.z)((x, y, z, p) => p.copy(x = x, y = y, z = z))
  println(points.replace(5)(Point("bottom-left", 0, 0, 0)))


  class Foo private (private val a: Int = 100) {
    private val b = 100
    private def f = ()
  }

  object Foo {
    def apply(b: Int) = {
      val ff = new Foo(b)
      ff.a
    }
  }
  Foo(10)


  println(List(1, 2, 3).traverse[Id, Int](_ + 1))

//  class User(name: String) {
//    private def f = println("foo")
//
//    override def toString: String = "Bar"
//  }
//
//  object User {
//    def apply(name: String) = new User(name)
//
//    override def toString: String = "Foo"
//  }
//
//  println(User("boba"))
//  println(User)
//
//
//  case class Person(name: String)
//
//  object Person {
//    def apply(name: String): String = "popa"
//  }
//
//  println(Person("adfsfasd"))
//
//  Person("Boba") match {
//    case Person(name) => println(name)
//  }
  println("===================================")

  case class Dog(name: String)

  val dog = Dog("dog")
  println(dog, dog.focus(_.name).replace("bar"), dog)
  val name2DogMap: Map[String, Dog] = Map("Gav" -> Dog("Gav"), "Bob" -> Dog("Bob"))

}
