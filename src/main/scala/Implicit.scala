object Implicit extends App {
//  sealed trait Bool
//
//  case object True extends Bool
//
//  case object False extends Bool
//
//  case class And[A <: Bool, B <: Bool](a: A, b: B) extends Bool
//
//  case class Or[A <: Bool, B <: Bool](a: A, b: B) extends Bool
//
//  case class Not[A <: Bool](a: A) extends Bool
//
//  case class Xor[A <: Bool, B <: Bool](a: A, b: B) extends Bool
//
//  trait IsTrue[Bool]
//
//  implicit val trueIsTrue: IsTrue[True.type] = new IsTrue[True.type] {}
//
//  implicit def andIsTrue[A <: Bool, B <: Bool](implicit isTrueA: IsTrue[A], isTrueB: IsTrue[B]): IsTrue[And[A, B]] = new IsTrue[And[A, B]] {}
//
//  implicit def orIsTrueL[A <: Bool, B <: Bool](implicit isTrueA: IsTrue[A]): IsTrue[Or[A, B]] = new IsTrue[Or[A, B]] {}
//
//  implicit def orIsTrueR[A <: Bool, B <: Bool](implicit isTrueB: IsTrue[B]): IsTrue[Or[A, B]] = new IsTrue[Or[A, B]] {}
//
//  implicit def notIsFalse[A <: Bool](implicit isTrue: IsTrue[A]): IsTrue[Not[A]]= new IsTrue[Not[A]] {}
//  implicit def notIsTrue[A <: Bool]: IsTrue[Not[A]]= new IsTrue[Not[A]] {}
//
//  implicit def xorIsTrueA[A <: Bool, B <: Bool](implicit isTrueA: IsTrue[A]): IsTrue[Xor[A, B]] = new IsTrue[Xor[A, B]] {}
//  implicit def xorIsTrueB[A <: Bool, B <: Bool](implicit isTrueB: IsTrue[B]): IsTrue[Xor[A, B]] = new IsTrue[Xor[A, B]] {}
//  implicit def xorIsFalse[A <: Bool, B <: Bool](implicit isTrueA: IsTrue[A], isTrueB: IsTrue[B]): IsTrue[Xor[A, B]] = new IsTrue[Xor[A, B]] {}
//
//  def boolIsTrue[A <: Bool](sent: A)(implicit isTrue: IsTrue[A]): Unit = ???
//
//
//  boolIsTrue(And(True, Not(Xor(True, True))))
//}
//
//object ImplicitCalc {
//  sealed trait Calc
//
//  case object `0` extends Calc
//  case class Inc[A <: Calc](a: A) extends Calc
//
//  type `1` = Inc[`0`.type]
//  type `2` = Inc[`1`]
//  type `3` = Inc[`2`]
//  type `4` = Inc[`3`]
//
//
//
//  case class ==[A <: Calc, B <: Calc](a: A, b: B) extends Calc
//
////  implicit val zero = Inc[`0`.type]
////  implicit def inc[A <: Calc](implicit a: A): Inc[A] = Inc[A]()
//
////  implicit val res: `4` = new `4`
//
//  def g[A <: Calc](s: A)(implicit a: A): Unit = ???
//
////  g(new `4`)
//
//  trait Sum[A <: Calc, B <: Calc] {
//    type R
//    def sum(a: A, b: B): R
//  } // если такой имплисит существует для чисел x,y,z - то x + y = z
//
//  // AUX pattern
//  type SumAux[A <: Calc, B <: Calc, Res <: Calc] = Sum[A, B] {type R = Res}
//
//  implicit def sum0[A <: Calc] = new Sum[`0`.type, A] {
//    type R = A
//
//    override def sum(a: `0`.type, b: A): A = b
//  }
//
//  // 2 8 -> 10
//  // 1 9 -> 10
//  // 0 10 -> 10 => 1 9 -> 10
//  implicit def sumX[A <: Calc, B <: Calc, Res <: Calc](implicit sum_ : SumAux[A, Inc[B], Res]): SumAux[Inc[A], B, Res] =
//    new Sum[Inc[A], B] {
//      override type R = Res
//
//      override def sum(a: Inc[A], b: B): Res =
//        sum_.sum(a.a, Inc(b))
//      }
//
//
//
//  def sum[A <: Calc, B <: Calc, C <: Calc](a: A, b: B)(implicit sum: SumAux[A, B, C]): sum.R = sum.sum(a, b)
//
////  val x = sum(Inc(Inc(`0`)), Inc(Inc(`0`)))
//  val y = sum(Inc(Inc(`0`)), Inc(Inc(`0`)))
//
//
//
//
//
////  `1` == `0` + `1`
//
//  implicit val a = 10
//  implicit val b = "qwe"
//
//  implicit class Foo(v: Int)(implicit val a: Int, b: String) {
//    def foo = v + a + b
//  }
//
//  println(1.foo)
//
//  println(List[Int]().exists(_ > 2))

//  package exercises06.e2_ignore

  // отбрасывает значения, на которых предикат выдал true
  trait Ignore[M[_]] {
    def ignore[A](m: M[A])(f: A => Boolean): M[A]
  }

  object Ignore {
    def apply[M[_]: Ignore]: Ignore[M] = implicitly[Ignore[M]]
  }

  object IgnoreInstances {
    implicit val listIgnore: Ignore[List] = new Ignore[List] {
      override def ignore[A](m: List[A])(f: A => Boolean): List[A] = m.filterNot(f)
    }

    implicit val setIgnore: Ignore[Set] = new Ignore[Set] {
      override def ignore[A](m: Set[A])(f: A => Boolean): Set[A] = m.filterNot(f)
    }

    implicit val vectorIgnore: Ignore[Vector] = new Ignore[Vector] {
      override def ignore[A](m: Vector[A])(f: A => Boolean): Vector[A] = m.filterNot(f)
    }

    implicit val optionIgnore: Ignore[Option] = new Ignore[Option] {
      override def ignore[A](m: Option[A])(f: A => Boolean): Option[A] = m.filterNot(f)
    }
  }

  object IgnoreSyntax {
    implicit class IgnoreOps[M[_], A](private val m: M[A]) extends AnyVal {
      def ignore(f: A => Boolean)(implicit inst: Ignore[M]): M[A] = inst.ignore[A](m)(f)
    }
  }

  object Examples {
    import IgnoreInstances._
    import IgnoreSyntax._

    val list: List[Int]     = List[Int](1, 2, 3, 4, 5).ignore(_ => true)
    val some: Option[Int]   = Option(2).ignore(_ => true)
    val none: Option[Int]   = Option.empty[Int].ignore(_ => true)
    val vector: Vector[Int] = Vector[Int]().ignore(_ => true)
    val set: Set[Int]       = Set[Int]().ignore(_ => true)
  }



  // wow!

  object Implicit
  def needImplisit(implicit i: Implicit.type): Unit = ()

  Some(Implicit).map(implicit i => needImplisit)
}
