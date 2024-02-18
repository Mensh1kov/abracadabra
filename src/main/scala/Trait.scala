import cats.syntax.all._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object Trait extends App {
  trait Foo {
    def kek = ???
  }
  case class Bar() extends Foo
  class Baz() extends Foo
  class Kek() extends Foo



  val l = List(new Bar(), new Baz(), new Kek())

//  l.foreach {
//    case bar: Bar => println("bar")
//    case other => println(other.getClass.getSimpleName)
//  }

  val tt = l.traverse {
    case foo: Bar => foo.some.pure[Future]
    case other =>
      println(other.getClass.getSimpleName)
      none.pure[Future]
  }.map(_.flatten)

  Thread.sleep(100)
  println(tt)




  val foo: Foo = new Foo {}



}
