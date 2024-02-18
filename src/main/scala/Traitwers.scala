object Traitwers extends App {
  trait Animal extends CanTalk {
    def name: String = "Boba"
  }

  trait CanTalk {
    self: Animal =>
    val talkPhrase: String = s"My name is ${name}"
  }

  val an = new Animal {}
  println(an.talkPhrase)

  Blocking
}
