object ContextBound extends App {
  sealed trait Boba {
    def bob
  }

  case class B() extends Boba {
    override def bob: Unit = println("bob")
  }
  case class Biba()

  implicit def f(a: Biba): B = B()

  def func[A <% Boba](a: A): Unit = a.bob
  def func2[A](a: A)(implicit f: A => Boba) = f(a).bob

  func(B())
  func2(B())

  def g[T <: String](a: T) = ()

//  g(3)
}
