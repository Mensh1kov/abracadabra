import cats.Eq
import cats.syntax.all._
object EqAndMemo extends App {
//  val runtime = Runtime.getRuntime
//  var s = runtime.freeMemory()
//  Eq.fromUniversalEquals[String]
//  var e = runtime.freeMemory()
//  println(e - s)
//  s = runtime.freeMemory()
//  Eq.fromUniversalEquals[Int]
//  e = runtime.freeMemory()
//  println(e - s)
//  s = runtime.freeMemory()
//  Eq.fromUniversalEquals[List[String]]
//  e = runtime.freeMemory()
//  println(e - s)

  trait Boba
  trait Biba

  type Id[T] = String with T

  val a: Id[Biba] = "a".asInstanceOf[Id[Biba]]
  val b: Id[Boba] = "a".asInstanceOf[Id[Boba]]
  implicit def eq[T]: Eq[T] = Eq.fromUniversalEquals
  a === a
  b === b
}
