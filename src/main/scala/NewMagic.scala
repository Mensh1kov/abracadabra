object NewMagic {
  class A(n: Int)

  class B(a: A)

  val a = new B(new A(1))
}
