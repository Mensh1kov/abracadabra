import enumeratum._
import tethys._
import tethys.derivation.builder.ReaderBuilder
import tethys.derivation.semiauto.{ReaderFieldSymbolOps, describe, jsonReader}
import tethys.enumeratum._
import tethys.jackson._
import tethys.readers.FieldName
import tethys.readers.tokens.TokenIterator

import scala.util.chaining.scalaUtilChainingOps

object Tethys extends App {

  abstract sealed class MyEnum(override val entryName: String) extends EnumEntry

  object MyEnum extends Enum[MyEnum]
    with TethysEnum[MyEnum] // provides JsonReader and JsonWriter instances
    with TethysKeyEnum[MyEnum] {

    val values = findValues

    case object Enum1 extends MyEnum("1")
    case object Enum2 extends MyEnum("2")
  }

  case class Foo(enum: MyEnum)

//  Foo(MyEnum.Enum2).pipe

  val json = """{"enum":"111"}"""

//  implicit def reader: JsonReader[Foo] = jsonReader[Foo] {
//    ReaderBuilder[Foo].extractReader(_.`enum`)('enum.as[String]) {
//      case "111" => new JsonReader[MyEnum] {
//        override def read(it: TokenIterator)(implicit fieldName: FieldName): MyEnum = MyEnum.Enum1
//      }
//      case _ => MyEnum.tethysReader
//    }
//  }
//
//  println(json.jsonAs[Foo])

}
