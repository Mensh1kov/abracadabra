import scala.language.existentials
object ForSome extends App {
  trait A
  case class B() extends A

  val list: List[T] forSome {type T <: A} = List(B(), B())

  def int2String(i: Int): String = i.toString

  def f(g: _ => _): Unit = ()
  f(int2String) // нет ошибки

//  def f2(g: (T => T) forSome {type T}): Unit = ()
//  f2(int2String) // ошибка
//
//  def f3[T](g: T => T): Unit = ()
//  f3(int2String) // ошибка


  def q(l: Array[T] forSome {type T}) = ()

  q(Array[String]("qwerty"))

  trait Loggable {
    def log
  }

  class LoggableClass extends Loggable {
    def log = print("Hello")
  }

  def displayLoggableMap(map: Map[Loggable, Loggable]) = {}

  def displayLoggableMap2(map: Map[T forSome { type T <: Loggable }, Loggable]) = {}

  def displayLoggableMap3(map: Map[T forSome { type T <: Loggable }, T forSome { type T <: Loggable }]) = {}

  def displayLoggableMap4(map: Map[T, Loggable] forSome { type T <: Loggable }) = {}

  val map = Map(new LoggableClass -> new LoggableClass)

//  displayLoggableMap(map) // type mismatch
//  displayLoggableMap2(map) // type mismatch
//  displayLoggableMap3(map) // type mismatch
  displayLoggableMap4(map) // ok

  var l1 : List[Class[_]] = List(classOf[Int],classOf[String]) // works fine
  var l2 : List[Class[_]] = List(classOf[String],classOf[String]) // works fine
  var l3 : List[Class[_]] = List(classOf[Any],classOf[Any]) // works fine

//  var l4 : List[Class[T forSome{type T}]] = List(classOf[Int],classOf[String]) // type error
//  var l5 : List[Class[T forSome{type T}]] = List(classOf[String],classOf[String]) // type error
  var l6 : List[Class[T forSome{type T}]] = List(classOf[Any],classOf[Any]) // works fine

  var l7 : List[Class[T] forSome{type T}] = List(classOf[Int],classOf[String]) // works fine
  var l8 : List[Class[T] forSome{type T}] = List(classOf[String],classOf[String]) // works fine
  var l9 : List[Class[T] forSome{type T}] = List(classOf[Any],classOf[Any]) // works fine

//  var l10 : List[Class[T]] forSome{type T} = List(classOf[Int],classOf[String]) // type error
  var l11 : List[Class[T]] forSome{type T} = List(classOf[String],classOf[String]) // works fine
  var l12 : List[Class[T]] forSome{type T} = List(classOf[Any],classOf[Any]) // works fine

  trait Typer {
    type A = Int
  }
  class Foo[A](a: A)
  val listtt: List[x.A] forSome { val x: Typer } = List(1, 3, 4)
  val listtt2: List[Typer#A] = List(1, 2, 3)

  class Baz[A](val i: A)
  class Bar[F[_]](val baz: F[_])

  val a = new Bar[Baz](new Baz[Int](1))

  def fff(q: Bar[Baz]) = q.baz.i

  fff(a)
}
