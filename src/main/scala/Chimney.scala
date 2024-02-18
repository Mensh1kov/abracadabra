import io.scalaland.chimney.Transformer
import io.scalaland.chimney.dsl._

object Chimney extends App {
  case class Foo(name: String) {
    val age: Int = 10
  }

  case class Bar(name: String, age: Int) {
    def getName = name
  }

  val transformer = Transformer.define[Foo, Bar].buildTransformer

  val foo = Foo("Boba")
  val bar = transformer.transform(foo)
  println(bar)


  case class Baz(n: String, bar: Bar) {
    def getName = bar.name
  }

  case class To(name: String)

  val t = Transformer.define[Baz, To].withFieldRenamed(_.getName, _.name).buildTransformer


  println(t.transform(Baz("123", Bar("qwe", 10))))



  trait A {
    def name: String
    def age: Int
  }



  case class DTO(name: String, age: Int)

  implicit val tr = Transformer.define[A, DTO].enableMethodAccessors.buildTransformer

  println(new A {
    override def name: String = "Qwe"
    override def age: Int = 10
  }.transformInto[DTO])


}
