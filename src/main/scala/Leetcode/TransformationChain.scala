package Leetcode

/**
 *  Transformation Chain
 *  Дан набор возможных трансформаций: type Transformation[T] = T => Option[T]
 *  Написать функцию преобразования последовательности трансформаций в возможную трансформацию.
 *  Новая трансформация это результат работы всей цепочки трансформаций, которые не вернули None.
 *  Если все вернули None, то общий результат None.
 */
object TransformationChain extends App {


  type Transformation[T] = T => Option[T]

  def transformationChain[T](chain: Seq[Transformation[T]]): Transformation[T] = {
//    chain.foldLeft[Transformation[T]](_ => None)((accTr, tr) => t => accTr(t).fold(tr(t))(tr).orElse(accTr(t)))
//    chain.foldLeft[Transformation[T]](_ => None)((accTr, tr) => t => accTr(t).map(a => tr(a).getOrElse(a)).orElse(tr(t)))
    t => chain.foldLeft(Option.empty[T])((acc, tr) => acc.flatMap(tr).orElse(acc.orElse(tr(t))))
  }


  val t1: Transformation[Int] = t => Some(t + t)
  val t2: Transformation[Int] = _ => None
  val t3: Transformation[Int] = t => if(t > 2)Some(t * t) else None

  val tc = transformationChain(Seq(t1,t2,t3))

  println(tc(1)) // Some(2)
  println(tc(2)) // Some(16)
  println(tc(4)) // Some(64)


  println(true > false)
  Ordered.orderingToOrdered(true)
}