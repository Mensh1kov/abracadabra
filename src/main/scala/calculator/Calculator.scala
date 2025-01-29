package calculator

import scala.Integral.Implicits.infixIntegralOps
import scala.annotation.tailrec

// Необходимо реализовать функцию сalculate для вычисления выражений
class Calculator[T: Integral] {
  def isZero(t: T): Boolean =
    t == implicitly[Integral[T]].zero

  def calculateV1(expr: Expr[T]): Result[T] = {
    @tailrec
    def loop(stack: List[Expr[T]], acc: List[Expr[T]]): Result[T] = {
      (stack, acc) match {
        case (_, Val(v1) :: Val(v2) :: Div(_, _) :: tail) =>
          if (isZero(v2)) DivisionByZero
          else loop(stack, Val(v1 / v2) :: tail)
        case (_, Val(v1) :: Val(v2) :: Mul(_, _) :: tail)   => loop(stack, Val(v1 * v2) :: tail)
        case (_, Val(v1) :: Val(v2) :: Plus(_, _) :: tail)  => loop(stack, Val(v1 + v2) :: tail)
        case (_, Val(v1) :: Val(v2) :: Minus(_, _) :: tail) => loop(stack, Val(v1 - v2) :: tail)
        case (_, Val(v) :: If(iff, _, left, right) :: tail) => loop((if (iff(v)) left else right) :: stack, tail)
        case ((head @ Mul(left, right)) :: tail, _)         => loop(right :: left :: tail, head :: acc)
        case ((head @ Div(left, right)) :: tail, _)         => loop(right :: left :: tail, head :: acc)
        case ((head @ Plus(left, right)) :: tail, _)        => loop(right :: left :: tail, head :: acc)
        case ((head @ Minus(left, right)) :: tail, _)       => loop(right :: left :: tail, head :: acc)
        case ((head @ If(_, cond, _, _)) :: tail, _)        => loop(cond :: tail, head :: acc)
        case ((head: Val[T]) :: tail, _)                    => loop(tail, head :: acc)
        case (Nil, Val(v) :: Nil)                           => Success(v)
      }
    }
    loop(List(expr), Nil)
  }

  def calculateV2(expr: Expr[T]): Result[T] = {
    @tailrec
    def loop(stack: List[Expr[T]], acc: List[Expr[T]]): Result[T] = {
      (stack, acc) match {
        case (Mul(expr1, expr2) :: tail, _) => loop(expr1 :: expr2 :: tail, Nil)
        case (Val(v1) :: Val(v2) :: (_: Mul[T]) :: tail, _) => loop(Val(v1 * v2) :: tail, Nil)
        case (Val(v) :: _, _) => Success(v)


//        case (_, Val(v1) :: Val(v2) :: Div(_, _) :: tail) =>
//          if (isZero(v2)) DivisionByZero
//          else loop(stack, Val(v1 / v2) :: tail)
//        case (_, Val(v1) :: Val(v2) :: Mul(_, _) :: tail)   => loop(stack, Val(v1 * v2) :: tail)
//        case (_, Val(v1) :: Val(v2) :: Plus(_, _) :: tail)  => loop(stack, Val(v1 + v2) :: tail)
//        case (_, Val(v1) :: Val(v2) :: Minus(_, _) :: tail) => loop(stack, Val(v1 - v2) :: tail)
//        case (_, Val(v) :: If(iff, _, left, right) :: tail) => loop((if (iff(v)) left else right) :: stack, tail)
//        case ((head: BinExpr[T]) :: tail, _)                => loop(head.right :: head.left :: tail, head :: acc)
//        case ((head @ If(_, cond, _, _)) :: tail, _)        => loop(cond :: tail, head :: acc)
//        case ((head: Val[T]) :: tail, _)                    => loop(tail, head :: acc)
//        case (Nil, Val(v) :: Nil)                           => Success(v)
      }
    }
    loop(List(expr), Nil)
  }
}

object Test extends App {
  val calc = new Calculator[Int]
  val onlyMul = Mul(Mul(Val(2), Val(3)), Val(3)) // 18
  val exp = Mul(Plus(Val(1), Val(2)), Minus(Val(2), Val(1))) // 3
  println(calc.calculateV1(onlyMul))
  println(calc.calculateV2(onlyMul))

//  val f1: Int => Int = identity
//
//  val f1: Int => Int => Int = v1 => v2 => v1 * v2
//
//  val f2: Int => Int => Int = f1 compose (v1 => v2 => v1 + v1)
//
}
