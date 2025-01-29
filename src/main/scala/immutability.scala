object immutability extends App {
  def addOneF(n: Int): Int = n + 1

  val addOneF: Int => Int = n => n + 1

  val pf: PartialFunction[Int, Int] = {
    case 10 => 10
  }

  pf(11)

//  val addOne: Int => Int = addOneF

  trait Cat
  trait Dog

  trait Foo[T] {
    def dog(implicit ev: T <:< Dog): String = "gav"

    def cat(implicit ev: T <:< Cat): String = "mew"
  }

  val foo = new Foo[Cat] {}

  foo.cat
//  foo.dog


}
