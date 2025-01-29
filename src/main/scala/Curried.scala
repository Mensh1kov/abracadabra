object Curried extends App {


  case class Person(name: String, age: Int)

  def getName(id: Int): String = "Boba"
  def getAge(id: Int): Int = 12

  def makePerson(name: String, age: Int): Person = Person(name, age)


}
