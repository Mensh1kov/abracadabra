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
