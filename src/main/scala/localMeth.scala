object localMeth extends App {
  class Boba(val name: String)

  def f(boba: Boba): String = {
    import boba.name
//    val name = boba.name
    name
    name
    name
    name
    name
  }

  println(f(new Boba("Boba")))
}
