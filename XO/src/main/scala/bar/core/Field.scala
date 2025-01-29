package bar.core

import Field._

trait Field {
  def mark(x: Int, y: Int, mark: Mark): Either[Error, Field]
  def height: Int
  def width: Int
  def size: (Int, Int) = (height, width)
  def get(x: Int, y: Int): Either[Error, Cell]
}

trait Cell {
  def isEmpty: Boolean
}

case object EmptyCell extends Cell {
  override def isEmpty: Boolean = true
}

case class MarkedCell(mark: Mark) extends Cell {
  override def isEmpty: Boolean = false
}

class FieldImpl(val width: Int, val height: Int) extends Field {
  private val field: Array[Array[Cell]] = Array.fill(height)(Array.fill(width)(EmptyCell))

  override def mark(x: Int, y: Int, mark: Mark): Either[Error, Field] = for {
    cell <- get(x, y)
    _ <- Either.cond(cell.isEmpty, (), AlreadyMarked)
  } yield {
    field(x)(y) = MarkedCell(mark) // bad
    this // keka
  }

  override def get(x: Int, y: Int): Either[Error, Cell] = for {
    row <- field.lift(x).toRight(OutOfRange)
    cell <- row.lift(y).toRight(OutOfRange)
  } yield cell
}

object Field {
  trait Error
  case object OutOfRange extends Error
  case object AlreadyMarked extends Error
}
