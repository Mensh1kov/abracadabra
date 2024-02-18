object Interpolator extends App {
  case class Boba(str: String)
  implicit class MyInterpolator(val c: StringContext) extends AnyVal {

    def b(args: Int*): String = {
      val n = args.sum
      val a = c.parts.foldLeft("")(_ ++ _)

      s"$a, ${Boba(s"$n years")}"
    }

  }

  println(b"${1} ${2} hello qwe foo ${10}")
}
