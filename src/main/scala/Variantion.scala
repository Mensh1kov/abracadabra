object Variantion extends App {

  trait Biba
  trait Boba extends Biba

  class Foo[-A](bar: A)

  val a: Foo[Boba] = new Foo[Biba](new Biba {})





  trait Animal {
    def name: String
  }
  class Cat(override val name: String) extends Animal
  case class Cat2(override val name: String) extends Cat(name)

  case class Dog(name: String) extends Animal


  // пример использования контрвариантности
  abstract class Serializer[-A] {
    def serialize(a: A): String
  }

  implicit val animalSerializer: Serializer[Animal] = new Serializer[Animal] {
    def serialize(animal: Animal): String = s"""{ "name": "${animal.name}" }"""
  }

Seq
  def ser[A](a: A)(implicit s: Serializer[A]): Unit = s.serialize(a)

  ser[Dog](Dog("123"))
  val catSerializer: Serializer[Cat] = animalSerializer
  catSerializer.serialize(new Cat("Felix"))

  //

  trait Puk[+A] {
    def make[B >: A](a: B): Puk[B]
  }


  trait Hmm[+A] {
    val aaa: A
  }

  case class Gg[+A](a: A)
  object Hmm {
    def apply[A](a: A): Hmm[A] = new Hmm[A] {
      val aaa = a
    }
  }

  // почему у функции аргумент контр-, а выходное значение ковариантно
  trait Func[-IN, +OUT]


  trait Car
  class Mercedes extends Car
  class BMV extends Car

  trait Country
  class Germany extends Country



//  val BMV2Germany: Func[Car, Germany] = ???

//  val func: Func[BMV, Country] = BMV2Germany


  // sample

  trait Box[+A] {
    def value: A
    def find[A1 >: A](a: A1): A1
  }
  object Box { def apply[A](a: A): Box[A] = new Box[A] {
    override def value: A = a

    override def find[A1 >: A](a: A1): A1 = a
  }}

  val boxWithCat = Box(new Cat("2"))
  val boxWithAnimal: Box[Animal] = boxWithCat

  (boxWithCat: Box[Cat]).find(Dog("123"))

  List[Cat](new Cat("1")).contains(Cat2("111"))
}
