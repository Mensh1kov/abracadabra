package sqlParser

import org.antlr.v4.runtime._
import org.antlr.v4.runtime.tree._

import scala.annotation.tailrec
//import my.grammar.SQLLexer
//import my.grammar.SQLParser

object SqlParser extends App {


  val keywords = Set("select", "as", "or", "and", "group", "order", "by", "where", "limit",
    "join", "asc", "desc", "from", "on", "not", "having", "distinct",
    "case", "when", "then", "else", "end", "for", "from", "exists", "between", "like", "in",
    "year", "month", "day", "null", "is", "date", "interval", "group", "order",
    "date", "left", "right", "outer", "inner")

  val delimiters = Set("*", "+", "-", "<", "=", "<>", "!=", "<=", ">=", ">", "/", "(", ")", ",", ".", ";")

  val functions = Set("count", "sum", "avg", "min", "max", "substring", "extract")

  trait Parser {
    def start(text: String)
//    def finish()
//    def part(text: String)
  }

  case class Line(
                   val number: Int,
                   val offset: Int,
                 ) {
    def moveLine(n: Int): Line = Line(number + n, 0)

    def moveOffset(off: Int): Line = Line(number, off + offset)
  }

  class MyParser(emitToken: (String, Int) => Unit) extends Parser {
    private var currentPart = new StringBuilder()

    private def getTypeId(token: String): Int = 1 // TODO

    private def emitCurrent(): Unit = {
      val current = currentPart.toString
      currentPart.clear()
      emitToken(current, getTypeId(current))
    }

    def skip(): Unit = ()


    @tailrec
    private def parse(text: List[Char]): Unit = text match {
      case Nil => emitCurrent()
      case (' ' | '\t' | '\n' | '\r' | '|') :: tail =>
        skip()
        parse(tail)
      case c :: (' ' | '\t' | '\n' | '\r' | '|') :: tail =>
        currentPart.addOne(c)
        emitCurrent()
        skip()
        parse(tail)
      case c :: tail =>
        currentPart.addOne(c)
        parse(tail)
    }


    def tokenizeIdentifierOrKeyword(text: List[Char]): List[Char] = text match {
      case Letter(ch) :: tail =>
        currentPart.addOne(ch)
        tokenizeIdentifierOrKeyword(tail)
      case t =>
        val part = currentPart.toString
        val token = if (keywords.contains(part)) Keyword(part) else Identifier(part)

        t

    }

    object Letter {
      def unapply(ch: Char): Option[Char] = Option.when(ch.isLetter)(ch)
    }

    override def start(text: String): Unit = parse(text.toList)
  }

  def emitToken(token: String, typeId: Int): Unit = println(s"$token -> $typeId")



  val text1 = "select * from table"
  val text2 =
    """
      select foo, bar
      from table
        where a like "foo"
      """

  new MyParser(emitToken).start(text2)



}

import scala.annotation.tailrec
import scala.collection.mutable.ListBuffer

sealed trait SQLToken
case class Keyword(value: String) extends SQLToken
case class Identifier(value: String) extends SQLToken
case class Function(value: String) extends SQLToken
case class Literal(value: String) extends SQLToken
case class Number(value: String) extends SQLToken
case class Operator(value: String) extends SQLToken
case class Whitespace(value: String) extends SQLToken
case class Comment(value: String) extends SQLToken
case class Other(value: String) extends SQLToken
case object EOF extends SQLToken

class SQLTokenizerOld(input: String) {
  private val keywords = Set("select", "as", "or", "and", "group", "order", "by", "where", "limit",
    "join", "asc", "desc", "from", "on", "not", "having", "distinct",
    "case", "when", "then", "else", "end", "for", "from", "exists", "between", "like", "in",
    "year", "month", "day", "null", "is", "date", "interval", "group", "order",
    "date", "left", "right", "outer", "inner")
  private val operators = Set("=", "<", ">", "<=", ">=", "<>", "!=", "+", "-", "*", "/", "%")
  private val buffer = new ListBuffer[SQLToken]()
  private var pos = 0

  def tokenize(): List[SQLToken] = {
    while (pos < input.length) {
      input(pos) match {
        case c if c.isWhitespace => tokenizeWhitespace()
        case c if c.isLetter => tokenizeIdentifierOrKeyword()
        case c if c.isDigit || c == '\'' => tokenizeLiteral()
//        case c if c == '-' && peekNextChar() == '-' => tokenizeLineComment()
//        case c if c == '/' && peekNextChar() == '*' => tokenizeBlockComment()
        case c if operators.exists(op => input.startsWith(op, pos)) => tokenizeOperator()
        case _ => pos += 1 // Skip unrecognized characters
      }
    }
    buffer += EOF
    buffer.toList
  }

  private def tokenizeWhitespace(): Unit = {
    val start = pos
    while (pos < input.length && input(pos).isWhitespace) pos += 1
    buffer += Whitespace(input.substring(start, pos))
  }

  private def tokenizeIdentifierOrKeyword(): Unit = {
    val start = pos
    while (pos < input.length && (input(pos).isLetterOrDigit || input(pos) == '_')) pos += 1
    val value = input.substring(start, pos).toUpperCase
    if (keywords.contains(value)) buffer += Keyword(value)
    else buffer += Identifier(value)
  }

  private def tokenizeLiteral(): Unit = {
    val start = pos
    if (input(pos) == '\'') {
      pos += 1
      while (pos < input.length && input(pos) != '\'') pos += 1
      pos += 1 // Skip closing quote
    } else {
      while (pos < input.length && input(pos).isDigit) pos += 1
    }
    buffer += Literal(input.substring(start, pos))
  }

  private def tokenizeLineComment(): Unit = {
    val start = pos
    while (pos < input.length && input(pos) != '\n') pos += 1
    buffer += Comment(input.substring(start, pos))
  }

  private def tokenizeBlockComment(): Unit = {
    val start = pos
    pos += 2 // Skip "/*"
    while (pos < input.length && !(input(pos) == '*' && peekNextChar() == '/')) pos += 1
    pos += 2 // Skip "*/"
    buffer += Comment(input.substring(start, pos))
  }

  private def tokenizeOperator(): Unit = {
    val start = pos
    while (pos < input.length && operators.exists(op => input.startsWith(op, pos))) pos += 1
    buffer += Operator(input.substring(start, pos))
  }

  private def peekNextChar(): Char = if (pos + 1 < input.length) input(pos + 1) else '\u0000'
}


object SQLTokenizer {
  private val keywords = Set("select", "as", "or", "and", "group", "order", "by", "where", "limit",
    "join", "asc", "desc", "from", "on", "not", "having", "distinct",
    "case", "when", "then", "else", "end", "for", "from", "exists", "between", "like", "in",
    "year", "month", "day", "null", "is", "date", "interval", "group", "order",
    "date", "left", "right", "outer", "inner", "create", "table", "insert", "into")
  private val operators = Set("=", "<", ">", "<=", ">=", "<>", "!=", "+", "-", "*", "/", "%")
  private val buffer = new ListBuffer[SQLToken]()
  private var pos = 0
  private val currentPart = new StringBuilder()

  def tokenize(text: String): List[SQLToken] = {
    def loop(chars: List[Char]): Unit = chars match {
      case ch :: _ if ch.isWhitespace => loop(tokenizeWhitespace(chars))
      case ch :: _ if ch.isLetter => loop(tokenizeIdentifierOrKeywordOrFunction(chars))
      case ch :: _ if ch.isDigit => loop(tokenizeNumber(chars))
      case '\'' :: _ => loop(tokenizeLiteral(chars))
      case ch1 :: ch2 :: _ if isOperator(ch1, ch2) => loop(tokenizeOperator(chars))
      case ch :: tail =>
        buffer += Other(ch.toString)
        loop(tail)
      case Nil =>
        buffer += EOF
    }

    loop(text.toLowerCase().toList)
    buffer.toList
  }

  private def tokenizeWhitespace(chars: List[Char]): List[Char] = chars match {
    case ch :: tail if ch.isWhitespace =>
      currentPart.addOne(ch)
      tokenizeWhitespace(tail)
    case _ =>
      buffer += Whitespace(currentPart.toString)
      currentPart.clear()
      chars
  }

  private def tokenizeIdentifierOrKeywordOrFunction(chars: List[Char]): List[Char] = chars match {
    case ch :: tail if ch.isLetter =>
      currentPart.addOne(ch)
      tokenizeIdentifierOrKeywordOrFunction(tail)
    case '(' :: _ =>
      buffer += Function(currentPart.toString)
      currentPart.clear()
      chars
    case _ =>
      val part = currentPart.toString
      val token = if (keywords.contains(part)) Keyword(part) else Identifier(part)
      currentPart.clear()
      buffer += token
      chars
  }

  private def tokenizeNumber(chars: List[Char]): List[Char] = {
    def loop(chars: List[Char], hasDot: Boolean): List[Char] = chars match {
      case ch :: tail if ch.isDigit =>
        currentPart.addOne(ch)
        loop(tail, false)
      case '.' :: ch :: tail if ch.isDigit && !hasDot =>
        currentPart.addOne('.')
        currentPart.addOne(ch)
        loop(tail, true)
      case _ =>
        buffer += Number(currentPart.toString)
        currentPart.clear()
        chars
    }

    loop(chars, false)
  }

  private def tokenizeLiteral(chars: List[Char]): List[Char] = chars match {
    case '\'' :: tail =>
      currentPart.addOne('\'')
      tokenizeLiteral(tail)
    case ch :: '\'' :: tail =>
      currentPart.addOne(ch)
      currentPart.addOne('\'')
      buffer += Literal(currentPart.toString)
      currentPart.clear()
      tail
    case ch :: tail =>
      currentPart.addOne(ch)
      tokenizeLiteral(tail)
    case _ =>
      buffer += Literal(currentPart.toString)
      currentPart.clear()
      chars
  }


  private def isOperator(ch1: Char, ch2: Char): Boolean =
    operators(ch1.toString) || operators(ch1.toString + ch2.toString)


  private def tokenizeOperator(chars: List[Char]): List[Char] = chars match {
    case ch1 :: ch2 :: tail if operators(ch1.toString + ch2.toString) =>
      currentPart.addOne(ch1)
      currentPart.addOne(ch2)
      buffer += Operator(currentPart.toString)
      currentPart.clear()
      tail
    case ch :: tail if operators(ch.toString) =>
      currentPart.addOne(ch)
      buffer += Operator(currentPart.toString)
      currentPart.clear()
      tail
    case _ => chars
  }

//  private def tokenizeLineComment(): Unit = {
//    val start = pos
//    while (pos < input.length && input(pos) != '\n') pos += 1
//    buffer += Comment(input.substring(start, pos))
//  }

//  private def tokenizeBlockComment(): Unit = {
//    val start = pos
//    pos += 2 // Skip "/*"
//    while (pos < input.length && !(input(pos) == '*' && peekNextChar() == '/')) pos += 1
//    pos += 2 // Skip "*/"
//    buffer += Comment(input.substring(start, pos))
//  }

//  private def peekNextChar(): Char = if (pos + 1 < input.length) input(pos + 1) else '\u0000'
}

object SQLTokenizerTest extends App {
  val createOrdersTable =
    """
      CREATE TABLE orders (
          order_id SERIAL PRIMARY KEY,
          user_id INT REFERENCES users(id),
          product_name VARCHAR(255),
          quantity INT,
          order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
      );
    """

  val insertUser =
    """
      INSERT INTO users (name, age, email)
      VALUES ('John Doe', 35.5, 'john.doe@example.com');
    """

  val sql1 =
    """
      select name, maх(age)
      from users
      where age > 30.5 and name = 'John'"""

  val sql5 =
    """
      select city, min(temperature)
      from weather
      where recorded_at between '2024-03-01' and '2024-03-10'
      group by city
    """

  val sql6 =
    """
      SELECT
        city,
        MIN(temperature) AS min_temperature,
        MAX(temperature) AS max_temperature,
        AVG(temperature) AS avg_temperature,
        SUM(temperature) AS sum_temperature,
        COUNT(*) AS record_count,
        (MAX(temperature) - MIN(temperature)) AS temperature_range,
        (AVG(temperature) * 100 / MAX(temperature)) AS avg_temp_percentage,
        (SUM(temperature) % COUNT(*)) AS sum_mod_count
      FROM
        weather
      WHERE
        recorded_at BETWEEN '2024-03-01' AND '2024-03-10'
        AND temperature >= -10
        AND temperature <= 25
        AND temperature <> 0
        AND temperature != 15
        AND (temperature + 5) > 0
        AND (temperature - 5) < 30
        AND (temperature * 2) >= 10
        AND (temperature / 2) <= 12.5
        AND (temperature % 5) = 0
      GROUP BY
        city
      HAVING
        MIN(temperature) < 0
        AND MAX(temperature) > 20
        AND AVG(temperature) >= 5
        AND SUM(temperature) > 100
        AND COUNT(*) >= 5
        AND (MAX(temperature) - MIN(temperature)) > 10
        AND (AVG(temperature) * 100 / MAX(temperature)) > 50
        AND (SUM(temperature) % COUNT(*)) = 0
      ORDER BY
        avg_temperature ASC
    """


  //  val tokenizer = new SQLTokenizer(sql)
//  val tokens = tokenizer.tokenize()
//  println(tokens)
//  println("---------------------------------------")
  println(SQLTokenizer.tokenize(sql6))
}


