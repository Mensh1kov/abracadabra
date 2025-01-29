object TaggedType extends App {
  class Foo
  class Bar extends Foo

  type @@[+A, Tag] = A with Tag

//  type Id[Tag] =  @@ Tag

  case class Dog(id: Foo @@ Dog)

  val fooWithDog: Foo @@ Dog = new Foo {}.asInstanceOf[TaggedType.Foo @@ Dog]

  implicit class IdOps[A](val a: A) extends AnyVal {
    @inline def @@[Tag]: A @@ Tag = a.asInstanceOf[A @@ Tag]
  }
  val id: Foo @@ Dog = new Bar {}.@@[Dog]
  val dog = Dog(id)

  println(dog)


  type Puk
  type Puk2
  type StringPuk = String
  type OtherStr = String
  def foo1(a: String @@ Puk) = ()
  def foo2(a: StringPuk) = ()

  val a: StringPuk = "123"
  val b: OtherStr = "1234"
  val c: String @@ Puk = "1234".asInstanceOf[String @@ Puk]
  val d: String @@ StringPuk = "1234".asInstanceOf[String @@ OtherStr]

  foo1(c)
  foo2(d)


  val gaga = new {
    val a = 123
    val b = 123
  }

  type foo = {
    val a: Int
    val c: Int
  }
//
//  trait F00 extends foo {
//    override val a: Int = 10
//    override val c: Int = 12
//  }

//  type @@[A, B] = A with B
//
//  implicit class TaggingExtensions[A](val a: A) extends AnyVal {
//    @inline def taggedWith[B]: A @@ B = a.asInstanceOf[A @@ B]
//
//    /** Synonym operator for `taggedWith`. */
//    @inline def @@[B]: A @@ B = taggedWith[B]
//  }
//
//  trait IdTag
//  type Id = Int @@ IdTag
//
//  implicit val maybeA: Option[Int] = Some(10)
//
//  implicit def implicitOption[A, B](
//    implicit
//    maybeA: Option[A]
//  ): Option[A @@ B] = maybeA.asInstanceOf[Option[A @@ B]]
//
//  implicitly[Option[Int @@ IdTag]] // this compiles
//  implicitly[Option[Id]]           // diverging implicit expansion
}
