import scala.math.BigDecimal.RoundingMode
import scala.util.Failure
import scala.util.matching.Regex

object Regex extends App {
  raw"age=\d+".r // ok
//  raw"age=\".r // error

  println("""\d{3,10}""".r.matches("12"))

//  val patterns: List[String] = "foo" :: "\\d+" :: raw"bar \d\d\d" :: Nil
//
//  val regex: List[Regex] = patterns.map(new Regex(_))
//
//  println(regex.exists(_.matches("foo")))
//  println(regex.exists(_.matches("123123")))
//  println(regex.exists(_.matches("bar 123")))
//
//  val r = raw"Option with id=\d+? doesn't belong to account \d+".replace("\\", "\\\\").r
//
//  println("reg", r.matches("Option with id=1234 doesn't belong to account 12345678"))


  trait Response
  trait Error extends Response
  trait Success extends Response

  type Result[T] = Either[Throwable, T]

  def parseError: Result[Error] = Right(new Error {})
  def parseSuccess: Result[Success] = Right(new Success {})

  def decodeResult(res: Result[Boolean]): Result[Response] =
    res.flatMap {
      case true => parseError
      case false => parseSuccess
    }

}
