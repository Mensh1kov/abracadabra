import scala.collection.mutable

object TwoSum extends App {
  type Index = Int
  object Solution {
    def twoSum(nums: Array[Int], target: Int): Array[Int] = {
      def loop(map: Map[Int, Index], i: Index): Array[Int] = {
        val num = nums(i)
        map.get(target - num) match {
          case Some(index) => Array(index, i)
          case None => loop(map + (num -> i), i + 1)
        }
      }
      loop(Map.empty, 0)
    }
  }


  println(('а' to 'я').mkString)

//  List("123").zip


//  class A(c: String) {
//    private def printShit: Int = a
//
//  }

//  object A {
//    def makeA(from: Int): A =
//      new A(from.toString)
//
//    def unapply(a: A): Option[(Int, Int)] = {
//      println(s"Call unapply ${a.printShit}")
//      Some(a.a, a.b)
//    }
//  }

//  for {
//    A(a, b) <- Some(new A(1, 2))
//  } yield a + b
//
//  val a = 1
//
//  val b = 1
//
//  b match {
//    case _: a.type => println("123")
//    case _ => println("foo")
//  }
}
