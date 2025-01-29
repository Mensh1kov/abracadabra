import java.net.URI
object JavaURI extends App {
  println(new URI("https://api-test.tinkoff.ru/tariff-hashes/private").getPath)
  println(new URI("https://api-test.tinkoff.ru/tariff-hashes/private").resolve("/"))

}
