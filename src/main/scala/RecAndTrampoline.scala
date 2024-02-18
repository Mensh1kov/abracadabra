import scala.annotation.tailrec
import scala.util.control.NonFatal

object RecAndTrampoline extends App {
//  @scala.annotation.tailrec
//  def loop(a: Int): Int =
//    if (a <= 0) 0
//    else loop(a - 1)
//
//  println(loop(100_000_000))


  trait Trampoline[A]
  object Trampoline {
    def pure[A](a: A): Trampoline[A] = Done(a)
  }
  case class Done[A](a: A) extends Trampoline[A]
  case class More[A](run: () => Trampoline[A]) extends Trampoline[A]

  def loop2(a: Int): Trampoline[Int] = {
    if (a <= 0) Done(a)
    else More(() => loop2(a - 1))
  }

  def run[A](tr: Trampoline[A]): A = {
    println("call global run")
    tr match {
      case Done(v) => v
      case More(r) => run(r())
    }
  }

  def flatMap[A, B](ta: Trampoline[A])(f: A => Trampoline[B]): Trampoline[B] = {
    println("call flatMap")
    ta match {
      case Done(b) => More(() => {println(s"run for $b");f(b)})
      case More(run) => More(() => {println("run");flatMap(run())(f)})
    }
  }

  val t = (1 to 3).foldLeft(Trampoline.pure(1)) {case (acc, i) => flatMap(acc)(v => Done(v + i))}
  println("end init t")
  println(run(t))

  NonFatal

}
