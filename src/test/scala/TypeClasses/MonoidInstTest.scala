package TypeClasses

import cats._
import cats.syntax.all._
import cats.laws.discipline.FunctorTests
import munit.DisciplineSuite
import arbitraries._

import org.scalacheck.{Arbitrary, Gen}

sealed trait Tree[+A]
case object Leaf extends Tree[Nothing]
case class Node[A](p: A, left: Tree[A], right: Tree[A]) extends Tree[A]

object Tree {
  implicit val functorTree: Functor[Tree] = new Functor[Tree] {
    def map[A, B](tree: Tree[A])(f: A => B) = tree match {
      case Leaf => Leaf
      case Node(p, left, right) => Node(f(p), map(left)(f), map(right)(f))
    }
  }
}
object arbitraries {
  implicit def arbTree[A: Arbitrary]: Arbitrary[Tree[A]] =
    Arbitrary(Gen.oneOf(Gen.const(Leaf), (for {
      e <- Arbitrary.arbitrary[A]
    } yield Node(e, Leaf, Leaf)))
    )
  implicit def eqTree[A: Eq]: Eq[Tree[A]] = Eq.fromUniversalEquals
}
class TreeLawTests extends DisciplineSuite {
  checkAll("Tree.FunctorLaws", FunctorTests[Tree].functor[Int, Int, String])
}
