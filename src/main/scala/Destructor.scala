object Destructor extends App {
  case class A(name: String, age: Int, email: String)

  val a = A("123", 123, "bar@foo.ru")

  a match {
    case _ => {
      import a._
      println(name)
    }
  }

}
