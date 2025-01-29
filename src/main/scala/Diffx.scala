import com.softwaremill.diffx._
//import com.softwaremill.diffx.Diff._
//import com.softwaremill.diffx.Diff
import com.softwaremill.diffx.generic.auto._


object Diffx extends App {
  case class Foo(name: Option[String])

  case class Bar(foo: Option[Foo])


  implicit val stringDif: Diff[Bar] = Diff.derived[Bar]


  val bar1 = Bar(Some(Foo(None)))
  val bar2 = Bar(None)

  val diff = compare(bar1, bar2)

  println(diff.show())









}
