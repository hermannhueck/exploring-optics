package monocle.optics

import scala.util.chaining._
import util.formatting._

object Ex03Prism extends util.App {

  s"${line(10)} Prism:".green pipe println
  "  A Prism is an optic used to select part of a Sum type (also known as Coproduct), e.g. sealed trait or Enum." pipe println
  "  Prisms have two type parameters generally called S and A: Prism[S, A] where S represents the Sum and A a part of the Sum." pipe println
  "  We can define a Prism which only selects Json elements built with a JStr constructor by supplying a pair of functions:" pipe println
  "  getOption: Json => Option[String]" pipe println
  "  reverseGet (aka apply): String => Json" pipe println

  s"${line(10)} Prism".green pipe println

  sealed trait Json
  case object JNull                     extends Json
  case class JStr(v: String)            extends Json
  case class JNum(v: Double)            extends Json
  case class JObj(v: Map[String, Json]) extends Json

  import monocle.Prism

  val jStr0 = Prism[Json, String] {
    case JStr(v) => Some(v)
    case _       => None
  }(JStr)

  val jStr = Prism.partial[Json, String] { case JStr(v) => v }(JStr.apply)

  val json: Json = jStr("hello") tap println

  val optString1: Option[String] = jStr.getOption(JStr("Hello")) tap println

  val optString2: Option[String] = jStr.getOption(JNum(3.2)) tap println

  s"${line(10)} pattern matching over prisms".green pipe println

  def isLongString(json: Json): Boolean = json match {
    case jStr(v) => v.length > 100
    case _       => false
  }
  isLongString(JStr("hello")) pipe println
  isLongString(JStr("hello" * 20)) pipe println
  isLongString(JStr("hello" * 21)) pipe println
  isLongString(JNum(3.2)) pipe println

  s"${line(10)} Prism: set and modify".green pipe println

  jStr.replace("Bar")(JStr("Hello")) pipe println    // JStr("Bar")
  jStr.modify(_.reverse)(JStr("Hello")) pipe println // JStr("olleH")

  // If we supply another type of Json, set and modify will be a no operation:
  jStr.replace("Bar")(JNum(10)) pipe println    // JNum(10)
  jStr.modify(_.reverse)(JNum(10)) pipe println // JNum(10)

  // If we care about the success or failure of the update, we can use setOption or modifyOption:
  jStr.modifyOption(_.reverse)(JStr("Hello")) pipe println // Some(JStr(olleH))
  jStr.modifyOption(_.reverse)(JNum(10)) pipe println      // None

  s"${line(10)} composition of prisms".green pipe println

  import monocle.std.double.doubleToInt // Prism[Double, Int] defined in Monocle

  val jNum: Prism[Json, Double] = Prism.partial[Json, Double] { case JNum(v) => v }(JNum)

  val jInt: Prism[Json, Int] = jNum andThen doubleToInt

  jInt(5) pipe println                       // JNum(5.0)
  jInt.getOption(JNum(5.0)) pipe println     // Some(5)
  jInt.getOption(JNum(5.2)) pipe println     // None
  jInt.getOption(JStr("Hello")) pipe println // None

  s"${line(10)} Prism Generation".green pipe println

  import monocle.macros.GenPrism

  val rawJNum: Prism[Json, JNum] = GenPrism[Json, JNum]

  rawJNum.getOption(JNum(4.5)) pipe println     // Some(JNum(4.5))
  rawJNum.getOption(JStr("Hello")) pipe println // None

  import monocle.macros.GenIso

  val jNum2: Prism[Json, Double] = GenPrism[Json, JNum] andThen GenIso[JNum, Double]
  val jNull: Prism[Json, Unit]   = GenPrism[Json, JNull.type] andThen GenIso.unit[JNull.type]

  jNum2.getOption(JNum(4.5)) pipe println     // Some(4.5)
  jNum2.getOption(JStr("Hello")) pipe println // None

  jNull.getOption(JNum(4.5)) pipe println     // None
  jNull.getOption(JStr("Hello")) pipe println // None
  jNull.getOption(JNull) pipe println         // Some(())

  s"${line(10)} Prism Laws".green pipe println

  import monocle.law.discipline.PrismTests
  import cats.derived.auto.eq._ // from kittens
  import cats.instances.all._
  import org.scalacheck.ScalacheckShapeless._

  // checkRules(PrismTests(jStr).all, "Prism", "jStr") // TEST HANGS !!!
  checkRules(PrismTests(doubleToInt).all, "Prism", "doubleToInt")
  // checkRules(PrismTests(jNum).all, "Prism", "jNum") // TEST HANGS !!!
  // checkRules(PrismTests(jInt).all, "Prism", "jInt") // TEST HANGS !!!
  // checkRules(PrismTests(jNum2).all, "Prism", "jNum2") // TEST HANGS !!!
  // checkRules(PrismTests(jNull).all, "Prism", "jNull") // TEST HANGS !!!
}
