import scala.util.control.NoStackTrace

object NoStackTrace_ extends App {
  NoStackTrace
  "123".toIntOption

  object A {
    println("init")
  }

  println("1")
  A
  println(2)
  A
  println(3)
}




