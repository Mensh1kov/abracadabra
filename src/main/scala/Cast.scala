
object Cast extends App {
  import java.math.{BigDecimal => JavaBigDecimal}

  val a = JavaBigDecimal.valueOf(11)
  val aa: Option[BigDecimal] = Option(a)
  val bb: Option[BigDecimal] = Some(a)
  val cc: Option[BigDecimal] = Option.apply(a)

//  val b = Some(a)
//  val bb: Option[BigDecimal] = b

//  val b11: Option[BigDecimal] = Some(a)
//  val b22: Option[BigDecimal] = aa.map(BigDecimal.apply)
//  implicit class A(o: Option[JavaBigDecimal]) {
//    def filterr(f: JavaBigDecimal => Boolean): Option[JavaBigDecimal] = {
//      if (o.isEmpty || !f(o.get)) None
//      else Some(o.get)
//    }
//  }

//  val qwe: Option[BigDecimal] = b11

//  val b1: Option[BigDecimal] = Option[BigDecimal](a).filter(_.equals(that = JavaBigDecimal.ZERO))
//  println(b1)
//
//  def f(a: JavaBigDecimal) = if (a == JavaBigDecimal.ZERO) None else Some(a)

//  val bbb = f(a)
//  val b2: Option[BigDecimal] = b11

//  val b: BigDecimal = a
}