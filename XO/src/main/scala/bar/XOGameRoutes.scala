package bar

import cats.effect.{Concurrent, Sync}
import cats.implicits._
//import org.http4s.circe._
import org.http4s.HttpRoutes
import org.http4s.dsl.Http4sDsl

object XOGameRoutes {

  def jokeRoutes[F[_]: Sync](J: Jokes[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "joke" =>
        for {
          joke <- J.get
          resp <- Ok(joke)
        } yield resp
    }
  }

  def helloWorldRoutes[F[_]: Sync](H: HelloWorld[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "hello" / name =>
        for {
          greeting <- H.hello(HelloWorld.Name(name))
          resp <- Ok(greeting)
        } yield resp
    }
  }

  def xoGameRoutes[F[_]: Concurrent](): HttpRoutes[F] = {
//    case class Request(x: Int, y: Int, mark: String)
//    implicit val decoder: EntityDecoder[F, Request] = jsonOf[F, Request]

    val dsl = new Http4sDsl[F] {}
    import dsl._
    HttpRoutes.of[F] {
      case POST -> Root / "XO" / "start" => Ok("start")
      case req@POST -> Root / "XO" / "mark" => Ok(s"$req mark")
      case GET -> Root / "XO" / "satus" => Ok("status")
    }
  }
}