import java.time.YearMonth
import java.time.format.DateTimeFormatter
import cats.syntax.all._

import java.net.URI
object DateInScala extends App {
  val s = "12/25"
  val date = YearMonth.parse(s, DateTimeFormatter.ofPattern("MM/yy"))

  println(date)
//
//  val f = (a: Unit) => date
//  val ff: () => Int = ???


  case class Prop(a: Boolean, b: Boolean)

//  val prop = Some(Prop(true, true))
//  for {
//    partNum <-
//  }{
//  f.andThen(_.toString)

  println(Some(1).forall(a => {println(a); true}))

  println(new URI("https://api-test.tinkoff.ru/tariff-hashes/private").getPath)




}