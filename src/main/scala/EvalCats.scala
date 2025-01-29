import cats.{Always, Eval}

object EvalCats extends App {
  val a = Eval.now { println("now"); 1 }
  val b = Eval.later { println("later"); 1 }
  val c = Eval.always { println("always"); 1 }

  Eval.later()
//  c.flatMap()
//  lazy val a = {println("boba"); 1}

  val f1: () => Int   = () => 1
  val f2: Unit => Int = _ => 1

  f1()
  f2()

  case class Config(num: Int)
  case class A(config: () => Config)
  case class B(num: () => Int)

  case class EA(config: Always[Config])
  case class EB(num: Eval[Int])

  def makeConf(): Config = {
    println("make conf")
    Config(20)
  }

  def test(): Unit = {
    val a = A(makeConf)
    val b = B(() => a.config().num)

    b.num()
    b.num()
    ()
  }

  def test2() = {
    val a = EA(Always(makeConf))
    val b = EB(a.config.map(_.num))

    b.num.value
    b.num.value
    ()
  }

  test2()


  Always {
    println("always init")
    10
  }

  println("-------------")

  def build: () => String = () => {
    println("build")
    "conf"
  }

  val al = Always(build())
println("fooo")
  al.value
  al.value
}
