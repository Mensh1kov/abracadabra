import io.estatico.newtype.macros.newtype
import io.estatico.newtype.ops.toCoercibleIdOps
import shapeless.newtype

object ImplicitBool extends App {

  object BoolIsObject {
    sealed trait Bool

    case object True extends Bool
    case object False extends Bool
    case class Not[A <: Bool](a: A) extends Bool
    case class And[A <: Bool, B <: Bool](a: A, b: B) extends Bool
    case class Or[A <: Bool, B <: Bool](a: A, b: B) extends Bool
    case class Implic[A <: Bool, B <: Bool](a: A, b: B) extends Bool
    case class Xor[A <: Bool, B <: Bool](a: A, b: B) extends Bool

    trait IsTrue[A <: Bool]

    object IsTrueRules {

      implicit val TrueIsTrue = new IsTrue[True.type] {}

      implicit def NotIsTrue1[A <: Bool]: IsTrue[Not[A]] = new IsTrue[Not[A]] {}
      implicit def NotIsTrue2[A <: Bool: IsTrue]: IsTrue[Not[A]] = new IsTrue[Not[A]] {}

      implicit def AndIsTrue[A <: Bool: IsTrue, B <: Bool: IsTrue] = new IsTrue[A And B] {}

      implicit def OrIsTrue[A <: Bool, B <: Bool](implicit isTrue: IsTrue[Not[Not[A] And Not[B]]]) =
        new IsTrue[A Or B] {}

      implicit def ImplicIsTrue[A <: Bool, B <: Bool](implicit isTrue: IsTrue[Not[A] Or B]) = new IsTrue[A Implic B] {}

      implicit def XorIsTrue[A <: Bool, B <: Bool](implicit isTrue: IsTrue[(Not[A] And B) Or (A And Not[B])]) =
        new IsTrue[A Xor B] {}
    }

    object PredicateIsObject {

      import IsTrueRules._

      val T = True
      val F = False

      implicit class BoolOps[A <: Bool](val a: A) extends AnyVal {
        def &[B <: Bool](b: B): A And B = And(a, b)
        def |[B <: Bool](b: B): A Or B = Or(a, b)
        def ->[B <: Bool](b: B): A Implic B = Implic(a, b)
        def unary_!(): Not[A] = Not(a)
        def +[B <: Bool](b: B): A Xor B = Xor(a, b)
      }

      def isTrue[A <: Bool](predicate: A)(implicit isTrue: IsTrue[A]): IsTrue[A] = isTrue

      val predicate = T

      isTrue(predicate) // compiled means True
    }
  }

  object BoolIsType {
    type Bool
    type False <: Bool
    type True <: Bool
    type Not[A <: Bool] <: Bool
    type And[A <: Bool, B <: Bool] <: Bool
    type Or[A <: Bool, B <: Bool] = Not[Not[A] And Not[B]]
    type Implic[A <: Bool, B <: Bool] = Not[A] Or B
    type Xor[A <: Bool, B <: Bool] = (Not[A] And B) Or (A And Not[B])

    trait IsTrue[A <: Bool]

    object IsTrueAxioms {
      implicit val TrueIsTrue = new IsTrue[True] {}

      implicit def NotIsTrue1[A <: Bool]: IsTrue[Not[A]] = new IsTrue[Not[A]] {}
      implicit def NotIsTrue2[A <: Bool: IsTrue]: IsTrue[Not[A]] = new IsTrue[Not[A]] {}

      implicit def AndIsTrue[A <: Bool : IsTrue, B <: Bool : IsTrue]: IsTrue[A And B] = new IsTrue[A And B] {}
    }


    object PredicateIsType {
      type T = True
      type F = False
      type ![A <: Bool] = Not[A]
      type &[A <: Bool, B <: Bool] = A And B
      type |[A <: Bool, B <: Bool] = A Or B
      type +[A <: Bool, B <: Bool] = A Xor B
      type ->[A <: Bool, B <: Bool] = A Implic B

      import IsTrueAxioms._

      def isTrue[Predicate <: Bool](implicit isTrue: IsTrue[Predicate]): IsTrue[Predicate] = isTrue

      type Predicate = ![![T] & T]

      isTrue[Predicate] // compiled means True
    }
  }

  @newtype class Id (val id: String)

  object Id {
    def mk(str: String): Id = str.coerce
  }
  println(List(true, false, true, false).filterNot(identity))
  println(List(Left("123"), Left("456")).diff(List(Left(Id.mk("123")), Left(Id.mk("123")))))

}

//
//или
//и
//не
//
//1 и 1 = 1
//1 и 0 = 0
//0 и 1 = 0
//0 и 0 = 0
//
//1 или 1 = 1
//1 или 0 = 1
//0 или 1 = 1
//0 или 0 = 0
//
//не 1 = 0
//не 0 = 1
//
//(1 и 1) или (1 и 0) = 1 или (1 и 0) = 1 или 0 = 1
//
//(1 или 0) и (0 или 0) = 1 и (0 или 0) = 1 и 0 = 0
//
//не(1) или 0 = 0 или 0 = 0
//
//не(не(1) и 1) = не(0 и 1) = не(0) = 1





