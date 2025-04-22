import cats._
import cats.data.NonEmptyList
import cats.effect._
import cats.effect.std.{Dispatcher, MapRef}
import cats.effect.unsafe.implicits.global
import cats.implicits._
import com.github.blemale.scaffeine.{AsyncCache, Scaffeine}
import fetch._

import scala.collection.mutable
import scala.concurrent.{ExecutionContext, Future}
import scala.concurrent.duration.DurationInt
import scala.util.{Failure, Success, Try}


object Fetchh extends App {


  val mm = Map(NonEmptyList(1,List(2, 3)) -> 1)
  println(mm.get(NonEmptyList(1,List(3, 2))))






  val m = Map(List(1, 2) -> "a", List(1, 3) -> "b")
  println(m.get(List(1, 2)))

  def latency[F[_] : Sync](milis: Long): F[Unit] =
    Sync[F].delay(Thread.sleep(milis))


  object ToString extends Data[Int, String] {
    def name = "To String"

    def source[F[_] : Async](ref: Ref[F, Int]): DataSource[F, Int, String] = new DataSource[F, Int, String]{
      override def data = ToString

      override def CF = Concurrent[F]

      override def fetch(id: Int): F[Option[String]] = for {
        _ <- CF.delay(println(s"--> [${Thread.currentThread.getId}] One ToString $id"))
        _ <- latency(1000)
        _ <- ref.update(_ + 1).whenA(id == 1)
        _ <- CF.delay(println(s"<-- [${Thread.currentThread.getId}] One ToString $id"))
      } yield Option(id.toString)

      override def batch(ids: NonEmptyList[Int]): F[Map[Int, String]] = for {
        _ <- CF.delay(println(s"--> [${Thread.currentThread.getId}] Batch ToString $ids"))
        _ <- latency(1000)
        _ <- CF.delay(println(s"<-- [${Thread.currentThread.getId}] Batch ToString $ids"))
      } yield ids.toList.map(i => (i, i.toString)).toMap
    }
  }
  object UnbatchedToString extends Data[Int, String] {
    def name = "Unbatched to string"

    def source[F[_]: Async] = new DataSource[F, Int, String] {
      override def data = UnbatchedToString

      override def CF = Concurrent[F]

      override def fetch(id: Int): F[Option[String]] =
        CF.delay(println(s"--> [${Thread.currentThread.getId}] One UnbatchedToString $id")) >>
          latency(5000) >>
          CF.delay(println(s"<-- [${Thread.currentThread.getId}] One UnbatchedToString $id")) >>
          CF.pure(id.toString.some)
    }
  }

  object SingleInFlightDS {
    var cache: AsyncCache[Any, Any] = null
    // бывают повторые запуски
    def wrap[F[_]: Async, I, O](underlying: DataSource[F, I, O])(implicit ec: ExecutionContext, dispatcher: Dispatcher[F]): F[DataSource[F, I, O]] = {
      def makeSingleInFlight(): AsyncCache[I, O] =
        Scaffeine()
          .recordStats()
          .expireAfterWrite(100.millis)
          .maximumSize(500)
          .buildAsync()

      Async[F].delay(makeSingleInFlight()).map(
        singleInFlight =>  new DataSource[F, I, O] {

          cache = singleInFlight.asInstanceOf[AsyncCache[Any, Any]]

          override def data: Data[I, O] = underlying.data

          override implicit def CF: Concurrent[F] = underlying.CF

          override def fetch(id: I): F[Option[O]] = {
            val cf = Async[F].delay {
              singleInFlight.getAllFuture(
                id :: Nil, i => {
                  i.headOption.fold(Future.successful(Option.empty[O])) {
                    key => dispatcher.unsafeToFuture(underlying.fetch(key))
                  }
                }.map(
                  optionO => optionO.map(o => Map(id -> o)).getOrElse(Map.empty[I, O])
                )
              )
            }

            Async[F].async {
              cb => cf.map(
                future => future.andThen {
                  case Failure(exception) => cb(Left(exception))
                  case Success(value)     => cb(Right(value.get(id)))
                }
              ).as(None)
            }
          }

          override def batch(ids: NonEmptyList[I]): F[Map[I, O]] = {
            val cf = Async[F].delay {
              singleInFlight.getAllFuture(
                ids.toList,
                _.toList.toNel.fold(Future.successful(Map.empty[I, O])) {
                  nel => {
                    dispatcher.unsafeToFuture(underlying.batch(nel))
                  }
                }
              )
            }

            Async[F].async {
              cb => cf.map(
                future => future.andThen {
                  case Failure(exception) => cb(Left(exception))
                  case Success(value)     => cb(Right(value))
                }
              ).as(None)
            }
          }
        }
      )
    }
  }

  object SingleFlightWraper {
    def wrap[F[_] : Async : Functor, I, O](underlying: DataSource[F, I, O]): F[DataSource[F, I, O]] = {
      Ref.of[F, Map[I, Deferred[F, Option[O]]]](Map.empty).map{ ref =>
        new DataSource[F, I, O] {
          override def data = underlying.data

          override implicit def CF: Concurrent[F] = underlying.CF
          // канцеляция и вечная блокровака на Deferred#get
          override def fetch(id: I): F[Option[O]] = Deferred[F, Option[O]].flatMap { newPromise =>
            ref.flatModify { state =>
              state.get(id).fold(
                (state.updated(id, newPromise), underlying.fetch(id).flatTap(newPromise.complete) <* ref.update(_.removed(id)))
              )(
                promise => (state, promise.get)
              )
            }
          }

//          override def batch(ids: NonEmptyList[K]): F[Map[K, V]] = ids.traverse(id => Deferred[F, Option[V]].flatMap { newPromise =>
//            ref.modify { state =>
//              state.get(id).fold(
//                (state.updated(id, newPromise), (id -> newPromise).asLeft[(K, Deferred[F,Option[V]])])
//              )(
//                promise => (state, (id -> promise).asRight[(K, Deferred[F,Option[V]])])
//              )
//            }
//          }).map(_.toList.separate).flatMap { case (needCalc, calculating) =>
//            val needCalcMap = needCalc.toMap
//            val keys = needCalcMap.keys.toList
//            val foo = keys.toNel.fold(Map.empty[K, V].pure[F])(
//              underlying.batch(_).flatTap(res =>
//                keys.traverse(k => needCalcMap.get(k).fold(false.pure[F])(_.complete(res.get(k))))))
//
//            for {
//              res <- foo
//              bar <- calculating
//              .traverse { case (k, defer) => defer.get.map(k -> _) }.map(_.collect { case (k, Some(v)) => (k, v) }.toMap)
//              _ <- ref.update(_.removedAll(keys))
//            } yield res ++ bar
//          }

        override def batch(ids: NonEmptyList[I]): F[Map[I, O]] = ids.traverse(id => Deferred[F, Option[O]].map { newPromise => (id -> newPromise) })
            .flatMap(id2NewPromisList =>
              ref.modify { state =>
                id2NewPromisList
                  .foldLeft(state -> List.empty[Either[(I, Deferred[F, Option[O]]), (I, Deferred[F, Option[O]])]]) {
                    case ((state, pairs), pair @ (id, newPromise)) => state.get(id)
                        .fold(state.updated(id, newPromise) -> (pair.asLeft[(I, Deferred[F, Option[O]])] :: pairs))(
                          promise => (state -> ((id -> promise).asRight[(I, Deferred[F, Option[O]])] :: pairs))
                        )
                  }
              }
            ).map(_.separate).flatMap { case (toFetchList, waitingList) =>
              val deferredsToFetch = toFetchList.toMap
              val keysToFetch = deferredsToFetch.keys.toList
              val fetchedValuesF = keysToFetch.toNel.fold(Map.empty[I, O].pure[F])(
                underlying.batch(_).flatTap(res =>
                  keysToFetch.traverse(k => deferredsToFetch.get(k).fold(().pure[F])(_.complete(res.get(k)).void))
                )
              )

              for {
                fetched <- fetchedValuesF
                waitingResults <- waitingList.traverse { case (key, defer) => defer.get.map(key -> _) }.map(_.collect {
                  case (key, Some(value)) => key -> value
                })
                _ <- ref.update(_.removedAll(keysToFetch))
              } yield fetched ++ waitingResults
            }

        }
      }
    }
  }

  object SingleInFlightDSWrapper2 {
    def wrap[F[_]: Async, I, O](underlying: DataSource[F, I, O]): F[DataSource[F, I, O]] = {
      for {
        // Хранение информации о выполняющихся запросах по отдельным ключам
        //      keyPromisesRef <- Ref.of[F, Map[I, Deferred[F, Option[O]]]](Map.empty)
        // Хранение информации о выполняющихся батч-запросах
        batchPromisesRef <- Ref.of[F, Map[Set[I], Deferred[F, Map[I, O]]]](Map.empty)
      } yield new DataSource[F, I, O] {
        override def data = underlying.data

        override implicit def CF: Concurrent[F] = underlying.CF

        override def fetch(id: I): F[Option[O]] = Deferred[F, Map[I, O]].flatMap { newPromise =>
          val setKey = Set(id)
          batchPromisesRef.flatModify { state =>
            state.get(setKey).fold((
              state.updated(setKey, newPromise),
              underlying.fetch(id).flatTap(res => newPromise.complete(res.fold(Map.empty[I, O])(r => Map(id -> r)))) <*
                batchPromisesRef.update(_.removed(setKey))
            ))(promise => (state, promise.get.map(_.get(id))))
          }
        }

        override def batch(ids: NonEmptyList[I]): F[Map[I, O]] = {
          val idsSet = ids.toList.toSet

          // Проверяем, есть ли уже выполняющийся батч, который включает все наши ключи
          def findExistingBatch(batchPromises: Map[Set[I], Deferred[F, Map[I, O]]]): Option[(Set[I], Deferred[F, Map[I, O]])] =
            batchPromises.find { case (batchKeys, _) => idsSet.subsetOf(batchKeys) }

          // Проверяем, есть ли пересечение с другими выполняющимися батчами
          def findIntersectingBatches(batchPromises: Map[Set[I], Deferred[F, Map[I, O]]]): List[(Set[I], Deferred[F, Map[I, O]])] =
            batchPromises.filter { case (batchKeys, _) => batchKeys.exists(idsSet.contains) }.toList

          def newBatch(promise: Deferred[F, Map[I, O]], ids: NonEmptyList[I]): F[Map[I, O]] =
            for {
              result <- underlying.batch(ids)
              _ <- promise.complete(result)
              _ <- batchPromisesRef.update(_.removed(idsSet))
            } yield result


          for {
            // Проверяем, есть ли уже батч, включающий все наши ключи
            newPromis <- Deferred[F, Map[I, O]]
            result <- batchPromisesRef.flatModify { state =>
              findExistingBatch(state) match {
                case Some((_, promise)) =>
                  // Если такой батч есть, просто ждем его результата и фильтруем нужные ключи
                  state -> promise.get.map(result => result.filter { case (k, _) => idsSet.contains(k) })

                case None =>
//                  println("none")
                  // Если нет, проверяем пересечения
                  val intersectingBatches = findIntersectingBatches(state)
                  if (intersectingBatches.isEmpty) {
                    // Если пересечений нет, создаем новый батч
                    state.updated(idsSet, newPromis) -> newBatch(newPromis, ids)
                  } else {
                    // Если есть пересечения, объединяем все ключи в один большой батч
                    val notFoundKeys =
                      idsSet
                        .filterNot(id =>
                          intersectingBatches.exists(_._1.contains(id))
                        ) // можно это сделать сразу в findIntersectingBatches

                    state.updated(notFoundKeys, newPromis) -> (for {
                      res <- notFoundKeys.toList.toNel.fold(Map.empty[I, O].pure[F])(newBatch(newPromis, _))
                      foo <- intersectingBatches.traverse { case (_, promise) =>
                        promise.get.map(_.view.filterKeys(idsSet).toMap)
                      }
                    } yield res ++ foo.foldLeft(Map.empty[I, O])(_ ++ _))
                  }
              }}
          } yield result
        }
      }
    }
  }

  def fetchBatch[F[_] : Async](source: DataSource[F, Int, String]): Fetch[F, (String, String, String)] =
    (Fetch(1, source), Fetch(2, source), Fetch(3, source)).mapN(Tuple3.apply)

  def fetchOne[F[_] : Async](ds: DataSource[F, Int, String]): Fetch[F, String] = Fetch(1, ds)

  val scenario = Dispatcher.parallel[IO].use { disp =>
    for {
      ref <- Ref.of[IO, Int](0)
//      ds <- SingleInFlightDS.wrap[IO, Int, String](ToString.source(ref))(implicitly[Async[IO]], concurrent.ExecutionContext.global, disp)
      ds <- SingleInFlightDSWrapper2.wrap[IO, Int, String](ToString.source(ref))
//      ds <- ToString.source[IO](ref).pure[IO]
      fetchOne1 <- Fetch.run[IO](fetchOne(ds)).map(r => println(s"fetchOne1: $r")).start
//      _ <- latency[IO](20)
      fetchOne2 <- Fetch.run[IO](fetchOne(ds)).map(r => println(s"fetchOne2: $r")).start
      fetch1 <- Fetch.run[IO](fetchBatch(ds)).map(r => println(s"fetchBatch1: $r")).start
//            _ <- latency[IO](20)
      fetch2 <- Fetch.run[IO](fetchBatch(ds)).map(r => println(s"fetchBatch2: $r")).start
      _ <- fetchOne1.join
      _ <- fetchOne2.join
      _ <- fetch1.join
      _ <- fetch2.join
//      fetch3 <- Fetch.run[IO](fetchBatch(ds)).start
//      _ <- fetch3.join
      _ <- ref.get.map(r => println(s"ref: $r"))
//      _ <- IO.println(SingleInFlightDS.cache.synchronous().stats())

    } yield ()
  }
  scenario.unsafeRunTimed(5.seconds)
}
