import cats.data.NonEmptyList
import cats.effect.{Ref, _}
import cats.effect.kernel.Ref
import cats.syntax.all._
import fetch.DataSource

import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.util.Try

trait SingleFlight[F[_], K, V] {
  def getOrRun(key: K, action: F[V]): F[V]
}
//object SingleFlight {
//  def create[F[_]: Concurrent, K, V]: F[SingleFlight[F, K, V]] =
//    Ref.of[F, Map[K, Deferred[F, V]]](Map.empty).map { ref =>
//      new SingleFlight[F, K, V] {
//        def getOrRun(key: K, action: F[V]): F[V] = {
//          Deferred[F, V].flatMap { newPromise =>
//            ref.modify { state =>
//              state.get(key) match {
//                case Some(promise) =>
//                  (state, promise.get) // Если уже идет выполнение, ждем его
//                case None =>
//                  (state.updated(key, newPromise), action.flatTap(newPromise.complete))
//              }
//            }.flatten
//          }
//        }
//      }
//    }
//}
//
//object SingleFlightExample extends IOApp.Simple {
//  def run: IO[Unit] =
//    for {
//      sf <- SingleFlight.create[IO, String, String]
//      f1 <- sf.getOrRun("task1", IO.sleep(1.seconds) *> IO.println("run...") *> IO.pure("Result")).flatMap(IO.println).start
//      f2 <- sf.getOrRun("task2", IO.sleep(1.seconds) *> IO.println("run...") *> IO.pure("Result")).flatMap(IO.println).start
//      f3 <- sf.getOrRun("task1", IO.sleep(1.seconds) *> IO.println("run...") *> IO.pure("Result")).flatMap(IO.println).start
//      _ <- f1.join
//      _ <- f2.join
//      _ <- f3.join
////      _ <- List.fill(5)("task1").parTraverse { key =>
////        sf.getOrRun(key, IO.sleep(1.seconds) *> IO.println("run...") *> IO.pure("Result")).flatMap(IO.println)
////      }
//    } yield ()
//}
//
//
//object SingleInFlightDSWrapper {
//  def wrap[F[_]: Async, I, O](underlying: DataSource[F, I, O]): F[DataSource[F, I, O]] = {
//    Ref.of[F, Map[I, Deferred[F, Option[O]]]](Map.empty).map { ref =>
//      new DataSource[F, I, O] {
//        override def data = underlying.data
//
//        override implicit def CF: Concurrent[F] = underlying.CF
//
//        // TODO: обработка отмены и предотвращение вечной блокировки при вызове Deferred#get
//        override def fetch(id: I): F[Option[O]] = Deferred[F, Option[O]].flatMap { newPromise =>
//          ref.flatModify { state =>
//            state.get(id).fold((
//              state.updated(id, newPromise),
//              underlying.fetch(id).flatTap(newPromise.complete) <* ref.update(_.removed(id))
//            ))(promise => (state, promise.get))
//          }
//        }
//
//        //        override def batch(ids: NonEmptyList[I]): F[Map[I, O]] = ids.traverse(id => Deferred[F, Option[O]].map { newPromise => (id -> newPromise) })
//        //            .flatMap(id2NewPromisList =>
//        //              ref.modify { state =>
//        //                id2NewPromisList
//        //                  .foldLeft(state -> List.empty[Either[(I, Deferred[F, Option[O]]), (I, Deferred[F, Option[O]])]]) {
//        //                    case ((state, pairs), pair @ (id, newPromise)) => state.get(id)
//        //                        .fold(state.updated(id, newPromise) -> (pair.asLeft[(I, Deferred[F, Option[O]])] :: pairs))(
//        //                          promise => (state -> ((id -> promise).asRight[(I, Deferred[F, Option[O]])] :: pairs))
//        //                        )
//        //                  }
//        //              }
//        //            ).map(_.separate).flatMap { case (toFetchList, waitingList) =>
//        //              val deferredsToFetch = toFetchList.toMap
//        //              val keysToFetch = deferredsToFetch.keys.toList
//        //              val fetchedValuesF = keysToFetch.toNel.fold(Map.empty[I, O].pure[F])(
//        //                underlying.batch(_).flatTap(res =>
//        //                  keysToFetch.traverse(k => deferredsToFetch.get(k).fold(().pure[F])(_.complete(res.get(k)).void))
//        //                )
//        //              )
//        //
//        //              for {
//        //                fetched <- fetchedValuesF
//        //                waitingResults <- waitingList.traverse { case (key, defer) => defer.get.map(key -> _) }.map(_.collect {
//        //                  case (key, Some(value)) => key -> value
//        //                })
//        //                _ <- ref.update(_.removedAll(keysToFetch))
//        //              } yield fetched ++ waitingResults
//        //            }
//
//        override def batch(ids: NonEmptyList[I]): F[Map[I, O]] = ids.traverse(id =>
//          Deferred[F, Option[O]].flatMap { newPromise =>
//            ref.modify { state =>
//              state.get(id)
//                .fold((state.updated(id, newPromise), (id -> newPromise).asLeft[(I, Deferred[F, Option[O]])]))(
//                  promise => (state, (id -> promise).asRight[(I, Deferred[F, Option[O]])])
//                )
//            }
//          }
//        ).map(_.toList.separate).flatMap { case (toFetchList, waitingList) =>
//          val deferredsToFetch = toFetchList.toMap
//          val keysToFetch = deferredsToFetch.keys.toList
//          val fetchedValuesF = keysToFetch.toNel.fold(Map.empty[I, O].pure[F])(
//            underlying.batch(_).flatTap(res =>
//              keysToFetch.traverse(k => deferredsToFetch.get(k).fold(().pure[F])(_.complete(res.get(k)).void))
//            )
//          )
//
//          for {
//            fetched <- fetchedValuesF
//            waitingResults <- waitingList.traverse { case (key, defer) => defer.get.map(key -> _) }.map(_.collect {
//              case (key, Some(value)) => key -> value
//            })
//            _ <- ref.update(_.removedAll(keysToFetch))
//          } yield fetched ++ waitingResults
//        }
//      }
//    }
//  }
//}
//
//object SingleInFlightDSWrapper2 {
//  def wrap[F[_]: Async, I, O](underlying: DataSource[F, I, O]): F[DataSource[F, I, O]] = {
//    for {
//      // Хранение информации о выполняющихся запросах по отдельным ключам
//      // keyPromisesRef <- Ref.of[F, Map[I, Deferred[F, Option[O]]]](Map.empty)
//      // Хранение информации о выполняющихся батч-запросах
//      batchPromisesRef <- Ref.of[F, Map[Set[I], Deferred[F, Map[I, O]]]](Map.empty)
//    } yield new DataSource[F, I, O] {
//      override def data = underlying.data
//
//      override implicit def CF: Concurrent[F] = underlying.CF
//
//      override def fetch(id: I): F[Option[O]] = Deferred[F, Map[I, O]].flatMap { newPromise =>
//        val setKey = Set(id)
//        batchPromisesRef.flatModify { state =>
//          state.get(setKey).fold((
//            state.updated(setKey, newPromise),
//            underlying.fetch(id).flatTap(res => newPromise.complete(res.fold(Map.empty[I, O])(r => Map(id -> r)))) <*
//              batchPromisesRef.update(_.removed(setKey))
//          ))(promise => (state, promise.get.map(_.get(id))))
//        }
//      }
//
//      override def batch(ids: NonEmptyList[I]): F[Map[I, O]] = {
//        val idsSet = ids.toList.toSet
//
//        // Проверяем, есть ли уже выполняющийся батч, который включает все наши ключи
//        def findExistingBatch(
//                               batchPromises: Map[Set[I], Deferred[F, Map[I, O]]]
//                             ): Option[(Set[I], Deferred[F, Map[I, O]])] = batchPromises.find { case (batchKeys, _) =>
//          idsSet.subsetOf(batchKeys)
//        }
//
//        // Проверяем, есть ли пересечение с другими выполняющимися батчами
//        def findIntersectingBatches(
//                                     batchPromises: Map[Set[I], Deferred[F, Map[I, O]]]
//                                   ): List[(Set[I], Deferred[F, Map[I, O]])] = batchPromises.filter { case (batchKeys, _) =>
//          batchKeys.exists(idsSet.contains)
//        }.toList
//
//        def newBatch(promise: Deferred[F, Map[I, O]], ids: NonEmptyList[I]): F[Map[I, O]] = for {
//          result <- underlying.batch(ids)
//          _ <- promise.complete(result)
//          _ <- batchPromisesRef.update(_.removed(idsSet))
//        } yield result
//
//        for {
//          // Проверяем, есть ли уже батч, включающий все наши ключи
//          newPromis <- Deferred[F, Map[I, O]]
//          result <- batchPromisesRef.flatModify { state =>
//            findExistingBatch(state) match {
//              case Some((_, promise)) =>
//                // Если такой батч есть, просто ждем его результата и фильтруем нужные ключи
//                state -> promise.get.map(result => result.filter { case (k, _) => idsSet.contains(k) })
//
//              case None =>
//                println("none")
//                // Если нет, проверяем пересечения
//                val intersectingBatches = findIntersectingBatches(state)
//                if (intersectingBatches.isEmpty) {
//                  // Если пересечений нет, создаем новый батч
//                  state.updated(idsSet, newPromis) -> newBatch(newPromis, ids)
//                } else {
//                  // Если есть пересечения, объединяем все ключи в один большой батч
//                  val notFoundKeys =
//                    idsSet
//                      .filterNot(id =>
//                        intersectingBatches.exists(_._1.contains(id))
//                      ) // можно это сделать сразу в findIntersectingBatches
//
//                  state.updated(notFoundKeys, newPromis) ->
//                    (for {
//                      res <- notFoundKeys.toList.toNel.fold(Map.empty[I, O].pure[F])(newBatch(newPromis, _))
//                      foo <- intersectingBatches.traverse { case (_, promise) =>
//                        promise.get.map(_.view.filterKeys(idsSet).toMap)
//                      }
//                    } yield res ++ foo.foldLeft(Map.empty[I, O])(_ ++ _))
//                }
//            }
//          }
//        } yield result
//      }
//    }
//  }
//}