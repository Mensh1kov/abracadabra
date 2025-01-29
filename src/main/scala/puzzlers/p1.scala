package puzzlers

object p1 extends App {
  object Case1 {
    trait Foo[A]

    implicit class FooOps[A, F[_] <: Foo[_]](foo: F[A]) {
      def test(a: A): String = a.toString
    }

    object Test extends Foo[Int]

    Test.test(123)
  }

  object Case2 {
    trait Foo[A] {
      type Bar

      def apply(a: A): Bar
    }

    implicit class FooOps[A, F[T] <: Foo[T]](foo: F[A]) {
      def test(a: A): F[A]#Bar = foo(a)
    }

    object Test extends Foo[Int] {
      type Bar = String

      def apply(a: Int): Bar = a.toString
    }

    object TestInt extends Foo[Int] {
      type Bar = String
      def apply(a: Int): Bar = a.toString
    }

    object TestString extends Foo[String] {
      type Bar = Long
      def apply(a: String): Bar = a.length
    }

    println(Test.test(123))
    // println(TestInt.test(123): String) err
    // println(TestString.test("123"): Long) err
  }
  object Case3 {
    trait Foo[A] {
      type Bar

      def apply(a: A): Bar
    }

    implicit class FooOps[A, F[A] <: Foo[A]](foo: F[A]) {
      def test(a: A): F[A]#Bar = foo(a)
    }

    object Test extends Foo[Int] {
      type Bar = String

      def apply(a: Int): Bar = a.toString
    }

    object TestInt extends Foo[Int] {
      type Bar = String
      def apply(a: Int): Bar = a.toString
    }

    object TestString extends Foo[String] {
      type Bar = Long
      def apply(a: String): Bar = a.length
    }

    println(Test.test(123))
//    println(TestInt.test(123): String)
//    println(TestString.test("123"): Long)
  }

  Case3
}
