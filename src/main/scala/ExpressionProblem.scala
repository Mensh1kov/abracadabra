object ExpressionProblem extends App {
  trait Shape {
//    def height: Double
  }

  case class Circle(r: Double) extends Shape {
//    override def height: Double = r * 2
  }
  case class Square(h: Double) extends Shape {
//    override def height: Double = h
  }

  case class RightTriangle(a: Double) extends Shape {
//    override def height: Double = 0
  }

  def height(shape: Shape): Double = shape match {
    case Circle(r) => r * 2
    case Square(h) => h
  }

  trait Operation[S <: Shape, RES] {
    def of(s: S): RES
  }

  trait Height[S <: Shape] extends Operation[S, Double]
  trait Area[S <: Shape] extends Operation[S, Double]
  trait Perimeter[S <: Shape] extends Operation[S, Double]

  object Operation {
    implicit val heightCircle = new Height[Circle] {
      override def of(s: Circle): Double = s.r * 2
    }

    implicit val heightSquare = new Height[Square] {
      override def of(s: Square): Double = s.h
    }

    implicit val heightRightTriangle = new Height[RightTriangle] {
      override def of(s: RightTriangle): Double = math.sqrt(s.a * s.a - s.a / 2 * s.a / 2)
    }

    implicit val areaCircle = new Area[Circle] {
      override def of(s: Circle): Double = 3.14 * s.r * s.r
    }

    implicit val areaSquare = new Area[Square] {
      override def of(s: Square): Double = s.height * s.height
    }

    implicit val squarePerimeter = new Perimeter[Square] {
      override def of(s: Square): Double = s.h * 4
    }

    implicit val perimeterCircle: Perimeter[Circle] = (s: Circle) => 3.14 * s.r * 2
  }

  implicit class ShapeOps[S <: Shape](val s: S) extends AnyVal {
    def height(implicit height: Height[S]): Double = height.of(s)
    def area(implicit area: Area[S]): Double = area.of(s)
    def perimeter(implicit perimeter: Perimeter[S]): Double = perimeter.of(s)
  }

  println(Square(10).height)
  println(Square(10).area)
  println(RightTriangle(10).height)
  println(Circle(10).perimeter)
  println(Square(10).perimeter)
}
