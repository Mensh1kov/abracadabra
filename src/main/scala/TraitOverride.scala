object TraitOverride extends App {
  def getName: String = "Boba"
  trait A {
    def name: String
  }

  class Interpreter extends A {
    def name: String = getName
  }


  new Interpreter {}.name
}
