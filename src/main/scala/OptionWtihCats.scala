import cats.syntax.all._

object OptionWtihCats extends App {
  val none: Option[Int] = None
  val some: Option[Int] = 10.some

  trait Completed
  case object Completed extends Completed
  object foo {
    def bar: Unit = 110
    def logErr(err: String): Completed = { println(s"log error: $err"); Completed }
  }

  val t = none.orRaise(foo.logErr("none")).map(_ => println("after logErr"))
  some.orRaise(foo.logErr("some")).map(_ => println("after logErr"))


}
