import Sorter.MemberOf

import java.util
import scala.Tuple10
import scala.reflect.{ClassTag, classTag}
import scala.reflect.runtime.universe._
import scala.tools.nsc.doc.html.HtmlTags.Sub

object Sorter extends App {

  /*
  Алгоритм сортировки:
  1.Коты
    1) Мульти коты, отсорт. по имени
    2) Немульти коты, отсорт. по имени
    3) Если среди мульти котов нет кота "Bob", то сначала будут немульти коты "Bob"
  2.Собаки
    1) по возрасту
  3.Птицы
    1) по скорости
   */

  trait Animal

  case class Cat(name: String, multi: Boolean) extends Animal
  case class Dog(age: Int) extends Animal
  case class Bird(speed: Int) extends Animal

  val ordCatByMulty: Ordering[Cat] = Ordering.by[Cat, Boolean](_.multi).reverse
  val ordCatByNameBob: Ordering[Cat] = Ordering.by[Cat, Boolean](cat => cat.name == "Bob").reverse
  val ordCatByName: Ordering[Cat] = Ordering.by[Cat, String](_.name)

  val ordCat: Ordering[Cat] = ordCatByMulty.orElse(ordCatByName)
  val ordCatIfNoMultyBob: Ordering[Cat] = ordCatByNameBob.orElse(ordCat)
  implicit val ordDog: Ordering[Dog] = Ordering.by[Dog, Int](_.age)
  implicit val ordBird: Ordering[Bird] = Ordering.by[Bird, Int](_.speed)

  implicit val ordAnimal: Ordering[Animal] = Ordering.by {
    case _: Cat  => 1
    case _: Dog  => 2
    case _: Bird => 3
    case _       => Int.MaxValue
  }

  class OrdOf[T: Ordering] {
    self =>
    private def ordInGroups: Ordering[T] = toOrdering

    def compare: (T, T) => Int = implicitly[Ordering[T]].orElse(ordInGroups).compare

    def addGroup[M <: T: Ordering: ClassTag]: OrdOf[T] = {
      new OrdOf[T] {
        def ordInGroups: Ordering[T] = {
          case (x: M, y: M) => implicitly[Ordering[M]].compare(x, y)
          case (x, y)       => self.ordInGroups.compare(x, y)
        }
      }
    }

    def toOrdering: Ordering[T] = compare.apply
  }

  trait MemberOf[G] {
    def value: G
    def tpe: Type
    def compare(that: MemberOf[G]): Int
  }

  object MemberOf {
    def apply[G: Ordering, T <: G: TypeTag: Ordering](t: T): MemberOf[G] = new MemberOf[G] {
      override val value = t
      override val tpe = typeOf[T]

      override def compare(that: MemberOf[G]): Int = that.value match {
        case thatValue: T if that.tpe =:= this.tpe => implicitly[Ordering[T]].compare(this.value, thatValue)
        case _                                     => implicitly[Ordering[G]].compare(this.value, that.value)
      }
    }
  }

  def ordAnimalV1(hasMultyCatBob: Boolean): Ordering[Animal] = {
    case (x: Cat, y: Cat)   => if (hasMultyCatBob) ordCat.compare(x, y) else ordCatIfNoMultyBob.compare(x, y)
    case (x: Dog, y: Dog)   => ordDog.compare(x, y)
    case (x: Bird, y: Bird) => ordBird.compare(x, y)
    case (x, y)             => ordAnimal.compare(x, y)
  }

  def ordAnimalV2(hasMultyCatBob: Boolean): Ordering[Animal] = {
    implicit val ordCatImp: Ordering[Cat] = if (hasMultyCatBob) ordCat else ordCatIfNoMultyBob

    new OrdOf[Animal].addGroup[Cat].addGroup[Dog].addGroup[Bird].toOrdering
  }

  implicit val ordMembers: Ordering[MemberOf[Animal]] = _ compare _
//
//  def ordAnimalNewV3(hasMultyCatBob: Boolean): Ordering[Animal] = {
//    implicit val ordCatImp: Ordering[Cat] = if (hasMultyCatBob) ordCat else ordCatIfNoMultyBob
//
////    Ordering.by {
////      case cat: Cat   => SubOf[Animal, Cat](cat)
////      case dog: Dog   => MemberOf[Animal, Dog](dog)
////      case bird: Bird => MemberOf[Animal, Bird](bird)
////    }
//  }

//  def ordAnimalNewV4(hasMultyCatBob: Boolean): Ordering[Animal] = {
//    implicit val ordCatImp: Ordering[Cat] = if (hasMultyCatBob) ordCat else ordCatIfNoMultyBob

//    Ordering.by {
//      case cat: Cat   => cat.ordBy(1, Some(cat.multi))
//      case cat: Cat   => (1, cat.memberOf.ordBy(_.multi).orElseBy(_.name))
//      case cat: Cat   => cat.sortBy()
//      case dog: Dog   => (2, None)
//      case bird: Bird => (3, None)
//    }
//  }



  val l: List[Animal] =
    List(Cat("Bob", false), Cat("D", true), Dog(1), Bird(1), Cat("Bob", false), Cat("NoBob", false), Dog(2), Bird(2))

  println(l.sorted(ordAnimalV2(false)))
  println(l.sorted(ordAnimalV1(false)))

}

object TestOrdOf extends App {
  import Sorter.OrdOf

  trait Animal
  trait Name {
    def name: String
  }
  trait Age {
    def age: Int
  }
  trait Cat extends Animal

  object Bob extends Cat with Age with Name {
    override def name: String = "Bob"
    override def age: Int = 11
  }
  object Alice extends Cat with Name {
    override def name: String = "Alice"
  }

  implicit val ordAnimal: Ordering[Animal] = Ordering.by {
    case _: Cat with Name => { println("cat with name"); 1 }
    case _: Cat with Age  => { println("cat with age"); 2 }
    case _                => Int.MaxValue
  }

  implicit def ordByName[T]: Ordering[T with Name] = Ordering.by(_.name)
  implicit def ordByAge[T]: Ordering[T with Age] = Ordering.by(_.age)

  val ord = new OrdOf[Animal].addGroup[Cat with Age].addGroup[Cat with Name].toOrdering

  println(List(Alice, Bob).sorted(ord))
}

object Test extends App {

  trait Animal
//  trait Cat
  trait Name
  trait Age

  case class Cat() extends Animal with Name
  case class Dog() extends Animal with Age

  case class MemberOf[+T: TypeTag](value: T) {
    val tag = typeOf[T]
  }

//  val cat: Animal with Name = Cat()
//  val dog: Animal with Name = Dog()
//  val m1 = MemberOf(cat)
//  val m2 = MemberOf(dog)
//  println(m1.tag =:= m2.tag)

}

object Test2 extends App {
  import scala.reflect.ClassTag

  class OrdOf[T: Ordering] {
    self =>
    def compare: (T, T) => Int = implicitly[Ordering[T]].compare

    def addGroup[M <: T: Ordering: ClassTag]: OrdOf[T] = {
      new OrdOf[T] {
        override def compare: (T, T) => Int = {
          case (x: M, y: M) => implicitly[Ordering[M]].compare(x, y)
          case (x, y)       => self.compare(x, y)
        }
      }
    }

    def toOrdering: Ordering[T] = compare.apply
  }

  trait Animal
  trait Name {
    def name: String
  }
  trait Age {
    def age: Int
  }
  trait Cat extends Animal

  object Bob extends Cat with Age with Name {
    override def name: String = "Bob"
    override def age: Int = 11
  }
  object Alice extends Cat with Name {
    override def name: String = "Alice"
  }

  implicit val ordAnimal: Ordering[Animal] = Ordering.by {
    case _: Cat => { println("cat"); 1 }
    case _      => { println("animal"); Int.MaxValue }
  }

  implicit def ordByName[T]: Ordering[T with Name] = Ordering.by(a => { println("by name"); a.name })
  implicit def ordByAge[T]: Ordering[T with Age] = Ordering.by(_.age)

  val ord = new OrdOf[Animal].addGroup[Cat with Name].addGroup[Cat with Age].toOrdering

  implicit val ordMembers: Ordering[MemberOf[Animal]] = _ compare _

  implicit class Foo[T: Ordering: TypeTag](t: T) {
    def memberOf[GROUP >: T: Ordering]: MemberOf[GROUP] = new MemberOf[GROUP] {
      override val value: GROUP = t
      override val tpe: Type = typeOf[T]
      override def compare(that: MemberOf[GROUP]): Int = that.value match {
        case thatValue: T if that.tpe =:= this.tpe => implicitly[Ordering[T]].compare(t, thatValue)
        case _                                     => implicitly[Ordering[GROUP]].compare(t, that.value)
      }
    }
  }

  val ord2: Ordering[Animal] = Ordering.by {
    case catWithName: Cat with Name => { println("with name"); MemberOf[Animal, Cat with Name](catWithName) }
    case catWithAge: Cat with Age   => { println("with age"); MemberOf[Animal, Cat with Age](catWithAge) }
  }

  val ord2_1: Ordering[Animal] = Ordering.by {
    case catWithAge: Cat with Age   => catWithAge.memberOf[Animal]
    case catWithName: Cat with Name => catWithName.memberOf[Animal]
  }

//  println(List(Alice, Bob).sorted(ord))
//  println(List(Alice, Bob).sorted(ord2_1))

}

object Test3 extends App {
  val ordOption: Ordering[Option[Int]] = implicitly[Ordering[Option[Int]]].reverse
  println(List(Some(1), None, Some(2), None).sorted(ordOption))

  implicit class Foo[T: Ordering: TypeTag](t: T) {
    def memberOf[G >: T: Ordering]: MemberOf[G] = new MemberOf[G] {
      override val value: G = t
      override val tpe: Type = typeOf[T]

      override def compare(that: MemberOf[G]): Int = that.value match {
        case thatValue: T if that.tpe =:= tpe => implicitly[Ordering[T]].compare(t, thatValue)
        case _                                => implicitly[Ordering[G]].compare(t, that.value)
      }

    }
  }
}
