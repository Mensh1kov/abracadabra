import scala.reflect.ClassTag

object ClassTagg extends App {
  trait Animal
  class Cat extends Animal
  class Dog extends Animal

  def printType[T: ClassTag](t: T): Unit = println(t.getClass.getName)

  val cat: Cat = new Cat
  val animal: Animal = cat

  printType(cat)
  printType(animal)
}
