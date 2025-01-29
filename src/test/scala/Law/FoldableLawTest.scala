package Law

import cats._
import cats.laws.discipline.FoldableTests
import munit.DisciplineSuite
import org.scalacheck._
import arbitrariess._


object arbitrariess {

  implicit def arbTree[A: Arbitrary]: Arbitrary[Tree[A]] =
    Arbitrary(Gen.oneOf(Gen.const(Leaf), Arbitrary.arbitrary[A].map(Node(_, Leaf, Leaf))))
  implicit def eqTree[A: Eq]: Eq[Tree[A]] = Eq.fromUniversalEquals
}
class FoldableLawTest extends DisciplineSuite{
  checkAll("Tree.FoldableLaws", FoldableTests[Tree].foldable[Int, Int])
}