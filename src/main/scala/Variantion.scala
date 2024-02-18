object Variantion extends App {

  trait Biba
  trait Boba extends Biba

  class Foo[-A](bar: A)

  val a: Foo[Boba] = new Foo[Biba](new Biba {})





  trait Animal {
    def name: String
  }
  case class Cat(name: String) extends Animal
  case class Dog(name: String) extends Animal

  abstract class Serializer[-A] {
    def serialize(a: A): String
  }

  val animalSerializer: Serializer[Animal] = new Serializer[Animal] {
    def serialize(animal: Animal): String = s"""{ "name": "${animal.name}" }"""
  }

  val catSerializer: Serializer[Cat] = animalSerializer
  catSerializer.serialize(Cat("Felix"))

}
