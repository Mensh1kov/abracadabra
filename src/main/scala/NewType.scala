import io.estatico.newtype.macros.{newsubtype, newtype}
import io.estatico.newtype.ops._

object NewType extends App {

  case class Bar()
  @newsubtype case class Id(id: Bar)


//  val id: Id = Id(123)
//  println(id)
//
//  case class Foo(i: Int)
//  type MyInt = Foo

  def f(i: Bar) = ()
  f(Id(Bar()))

}
