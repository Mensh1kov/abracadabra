import cats.Id

object CollectBug extends App {
  final case class Kek(id: Int)
  final case class SuperKek(id: Int, str: String)

  val kekSeq = Seq(
    Kek(1),
    Kek(2),
    Kek(3),
    Kek(4)
  )

  val m = Map(
    1 -> "foo",
    2 -> "bar"
  )

  def enrich(keks: Seq[Kek], mapa: Map[Int, String]): Seq[SuperKek] = keks.collect { kek =>
    println("")
    mapa.get(kek.id) match {
      case Some(str) => SuperKek(kek.id, str)
    }
  }

  println(s"${enrich(kekSeq, m)}")

  Id
}
