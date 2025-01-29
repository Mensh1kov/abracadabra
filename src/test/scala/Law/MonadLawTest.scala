package Law

import cats._
import cats.laws.discipline.MonadTests
import munit.DisciplineSuite
import org.scalacheck._
import arbitraries._


object arbitraries {
  implicit def arbMyOption[A: Arbitrary]: Arbitrary[MyOption[A]] =
    Arbitrary(Gen.oneOf(Gen.const(MyNone), Arbitrary.arbitrary[A].map(MySome(_))))
  implicit def eqMyOption[A: Eq]: Eq[MyOption[A]] = Eq.fromUniversalEquals
}
class MonadLawTest extends DisciplineSuite{
  checkAll("MyOption.MonadLaws", MonadTests[MyOption].monad[Int, Int, String])
}
