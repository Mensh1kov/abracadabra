object This extends App {

  trait Animal
  case class Cat() extends Animal


  class Foo[+A](a: A, b: A) {
    def this(a: A) = this(a, a)
//    def foo(aa: A): Foo[A] = new Foo(aa)

  }
//
//  val foo: Foo[Cat] = new Foo[Cat](Cat())
//  val foofoo: Foo[Cat] = foo.foo(Cat())

}
