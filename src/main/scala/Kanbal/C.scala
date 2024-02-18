package Kanbal

import java.io.PrintWriter
import scala.io.Source

object C extends App {
  val iter = Source.fromFile(
    "/Users/al.menshikov/IdeaProjects/abracadabra/src/main/scala/Kanbal/in.txt"
  ).getLines()

  val distance = iter.next().toInt
  val capacity :: consumption :: expenditure :: countGasStation :: _ =
    iter.next().split(' ').map(_.toDouble).toList
  val gasStations =
    (1 to countGasStation.toInt).map(_ => iter.next().split(' ').map(_.toDouble).toList)

  case class Result(expenditure: Double, trace: List[Int]) {
    override def toString: String =
      s"""|${BigDecimal(expenditure).setScale(2, BigDecimal.RoundingMode.HALF_UP)}
          |${trace.sorted.mkString(" ")}""".stripMargin
  }

  def loop(lastGS: Int, curDistance: Double, lastResult: Result): Result =
    if (capacity - (distance - curDistance) / consumption >= 0) lastResult
    else {
      {
        for {
          i                 <- lastGS + 1 to countGasStation.toInt
          dist :: price :: _ = gasStations(i - 1)
          nextDist :: _      = gasStations.lift(i).getOrElse(List(Double.MaxValue, 0))
          fuelLevel          = capacity - (dist - curDistance) / consumption
          nextFuelLevel      = capacity - (nextDist - curDistance) / consumption
          if fuelLevel >= 0 && (fuelLevel <= capacity / 2 && fuelLevel >= 0 || nextFuelLevel < 0)
        } yield loop(
          i,
          dist,
          Result(
            lastResult.expenditure + 20 + BigDecimal((capacity - fuelLevel) * price).setScale(1, BigDecimal.RoundingMode.HALF_UP).toDouble,
            lastResult.trace :+ i
          )
        )
      }.minBy(_.expenditure)
    }

  val res = loop(0, 0, Result(expenditure, List.empty))

  val writer =
    new PrintWriter("/Users/al.menshikov/IdeaProjects/abracadabra/src/main/scala/Kanbal/out.txt")

  writer.write(res.toString)
  writer.close()
}


object AAA extends App {
  def foo(a: => Int): Int => Int =
    i => i + a

  println("init...")
  val f = foo {
    println("call")
    10
  }
  println("inited")

  println(f(10))

}