object MemberType extends App {
  class A
  class B extends A

  abstract class Buffer1 {
    type T
    val element: T
  }

  val bb                         = new Buffer1 {
    override type T = B
    override val element: T = new B
  }

  val aa: Buffer1 { type T = B } = bb

  abstract class Buffer2[+T] {
    val element: T
  }

  val b             = new Buffer2[B] { override val element: B = new B }
  val a: Buffer2[A] = b

  trait Foo {
    type A
    type B
    val a: A
    val b: B
  }
  val foo: Foo {type A = Int; type B = String } = new Foo {
    override type A = Int
    override type B = String
    override  val a: A = 10
    override  val b: B = "foo"
  }
}
