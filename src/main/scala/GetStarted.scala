
import cats.Id
import cats.syntax.all._
import org.scalacheck.Prop.Exception

import java.lang
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object GetStarted extends App {
  val res = PartialFunction.condOpt(1) {case 2 => "foo"}.getOrElse("bar")

  trait Animal

  case class Cat() extends Animal
  case class Dog() extends Animal

  val l = List(Cat(),Dog(), Cat(), Dog())

  val t = l.partitionMap {
    case c: Cat => c.asRight
    case d: Dog => d.asLeft
  }

  println(t)

  Future.failed[Int](new scala.Exception()).handleError { err =>
    println("handle err", err)
    10
  }


}
