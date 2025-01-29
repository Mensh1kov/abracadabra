package Law

import cats._
import cats.instances.ListInstances

import scala.annotation.tailrec

sealed trait Tree[+A]
case object Leaf extends Tree[Nothing]
case class Node[A](a: A, left: Tree[A], right: Tree[A]) extends Tree[A]

object Tree {

  implicit val TreeFaldableInst = new Foldable[Tree] {
    override def foldLeft[A, B](fa: Tree[A], b: B)(f: (B, A) => B): B = fa match {
      case Leaf => b
      case Node(a, left, right) => foldLeft(right, foldLeft(left, f(b, a))(f))(f)
    }

    override def foldRight[A, B](fa: Tree[A], lb: Eval[B])(f: (A, Eval[B]) => Eval[B]): Eval[B] = fa match {
      case Leaf => lb
      case Node(a, left, right) => foldRight(left, foldRight(right, f(a, lb))(f))(f)
    }
  }
}


