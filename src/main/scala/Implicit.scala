object ImplicitBool extends App {
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

  implicit val a = 10
  implicit val b = "qwe"

  implicit class Foo(v: Int)(implicit val a: Int, b: String) {
    def foo = v + a + b
  }

  println(1.foo)

  println(List[Int]().exists(_ > 2))
}
