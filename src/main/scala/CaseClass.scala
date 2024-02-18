object CaseClass extends App {

  case class A() {
    override def toString: String = "my to string"
  }

  val a = A

  A.toString()
//  A.clone()

  println(Map(1 -> "foo", 2 -> "bar").view.mkString(", "))
  "foo"
  "foo"

}