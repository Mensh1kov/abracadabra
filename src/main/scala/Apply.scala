import com.sun.net.httpserver.Authenticator.Success

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Apply extends MyApp {
  trait Boba

  trait Biba {self: Boba =>
    println(self)
  }

  class Qwe extends Biba with Boba

  case class A() { self => println("сработает при инициализации")
    def apply(): Unit = println("call apply")
  }

  val a = new A

  case class Foo(hidden: Boolean, name: String) extends WithHidden {
    override def unhidden: Foo = this.copy(hidden = false)
  }

  val qwe = Foo(true, "Foo")
  val ewq = qwe.unhidden

  println(qwe, ewq)

  trait WithHidden { self =>
    def hidden: Boolean
    def unhidden: WithHidden
  }


}

trait MyApp {
  final def main(args: Array[String]) = {
    println("call main")
  }
}
