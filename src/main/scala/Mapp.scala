import cats.effect.unsafe.Scheduler

import java.util.concurrent.{Executors, ThreadPoolExecutor}
import java.util.concurrent.atomic.AtomicBoolean
import scala.collection.IterableFactory.toBuildFrom
import scala.collection.generic.IsSeq
import scala.collection.{BuildFrom, Factory}
import scala.collection.immutable.TreeMap
import scala.concurrent.{ExecutionContext, Future}

//import scala.collection.mutable.Map
object Mapp extends App {
  val m = Map(1 -> "f")

  println(m.getClass)


  trait A

  object Bar extends A
  object Foo extends A {
    override def equals(other: Any): Boolean = {println(s"eq to $other"); true}
    override def hashCode(): Int = Bar.##
  }

  println(Map[A, String](Bar -> "bar", new A {} -> "foo", new A {} -> "foo", new A {} -> "foo", new A {} -> "foo").get(Foo))

  val a = TreeMap(3 -> "c", 1 -> "a", 2 -> "b")


  println(implicitly[BuildFrom[List[Int], Int, List[Int]]].fromSpecific(List(1, 2, 3))(Seq(3)))
  println(implicitly[Factory[Int, List[Int]]].fromSpecific(List(1, 2, 3)))


  println(List.fromSpecific()(List(1, 2, 3)))





  import scala.collection.concurrent.TrieMap
  import scala.util.hashing._

  Scheduler


  object H
  println(Int.MaxValue.##.toBinaryString, byteswap32(H.##).toBinaryString.length)

  TrieMap.empty[String, Int]



  ExecutionContext.fromExecutor(Executors.newCachedThreadPool())
  import cats.effect.unsafe.Scheduler


  println("""\d""".r matches "1")


 println("qwe\nq".trim)

  println("\u041d\u0435 \u043f\u043e\u043b\u0443\u0447\u0438\u043b\u043e\u0441\u044c \u0437\u0430\u043a\u0440\u044b\u0442\u044c \u0441\u0447\u0435\u0442. \u041d\u0443\u0436\u043d\u043e \u0441\u043d\u0430\u0447\u0430\u043b\u0430 \u0437\u0430\u043a\u0440\u044b\u0442\u044c \u043d\u0430\u043a\u043e\u043f\u0438\u0442\u0435\u043b\u044c\u043d\u044b\u0435 \u0441\u0447\u0435\u0442\u0430 \u0432.*", "\u041d\u0435 \u043f\u043e\u043b\u0443\u0447\u0438\u043b\u043e\u0441\u044c \u0437\u0430\u043a\u0440\u044b\u0442\u044c \u0441\u0447\u0435\u0442. \u041d\u0443\u0436\u043d\u043e \u0441\u043d\u0430\u0447\u0430\u043b\u0430 \u0437\u0430\u043a\u0440\u044b\u0442\u044c \u0432\u043a\u043b\u0430\u0434\u044b \u0432.*", "\u041d\u0435 \u043f\u043e\u043b\u0443\u0447\u0438\u043b\u043e\u0441\u044c \u0437\u0430\u043a\u0440\u044b\u0442\u044c \u0441\u0447\u0435\u0442. \u041d\u0430 \u043d\u0435\u043c \u0434\u043e\u043b\u0436\u043d\u043e \u0431\u044b\u0442\u044c 0.*")

  println(new Exception("foo") == new Exception("foo"))
}
