import cats._

import scala.annotation.tailrec
sealed trait Tree[+A]
case object Leaf extends Tree[Nothing]
case class Node[A](a: A, left: Tree[A], right: Tree[A]) extends Tree[A]


object Tree {
  implicit val TreeReducibleInst = new Reducible[Tree] {

    override def reduceLeftTo[A, B](fa:  Tree[A])(f:  A => B)(g:  (B, A) => B): B = ???

    override def reduceRightTo[A, B](fa:  Tree[A])(f:  A => B)(g:  (A, Eval[B]) => Eval[B]): Eval[B] = ???

    override def foldLeft[A, B](fa:  Tree[A], b:  B)(f:  (B, A) => B): B = ???

    override def foldRight[A, B](fa:  Tree[A], lb:  Eval[B])(f:  (A, Eval[B]) => Eval[B]): Eval[B] = ???
  }
}