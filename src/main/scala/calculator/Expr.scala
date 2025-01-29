package calculator

sealed trait Expr[A]
sealed trait BinExpr[A] extends Expr[A] {
  def left: Expr[A]
  def right: Expr[A]
}
case class Mul[A](left: Expr[A], right: Expr[A])                                  extends BinExpr[A]
case class Div[A](left: Expr[A], right: Expr[A])                                  extends BinExpr[A]
case class Plus[A](left: Expr[A], right: Expr[A])                                 extends BinExpr[A]
case class Minus[A](left: Expr[A], right: Expr[A])                                extends BinExpr[A]
case class Val[A](v: A)                                                           extends Expr[A]
case class If[A](iff: A => Boolean, cond: Expr[A], left: Expr[A], right: Expr[A]) extends Expr[A]
