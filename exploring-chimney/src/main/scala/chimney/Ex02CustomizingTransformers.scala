package chimney

import io.scalaland.chimney.dsl._
import scala.util.chaining._
import util.formatting._

object Ex02CustomizingTransformers extends util.App {

  s"$dash10 Providing missing values $dash10".magenta pipe println

  case class Catterpillar(size: Int, name: String)
  case class Butterfly(size: Int, name: String, wingsColor: String)

  val stevie = Catterpillar(5, "Steve") tap println

  // val steve  = stevie.transformInto[Butterfly] // compile error
  // wingsColor: java.lang.String - no accessor named wingsColor in source type chimney.Ex03ProvidingMissingValues.Catterpillar

  val steve = stevie
    .into[Butterfly]
    .withFieldConst(_.wingsColor, "white")
    .transform
    .tap(println)

  val steve2 = stevie
    .into[Butterfly]
    .withFieldComputed(_.wingsColor, c => if (c.size > 4) "yellow" else "gray")
    .transform
    .tap(println)

  s"$dash10 Fields renaming $dash10".magenta pipe println

  case class SpyGB(name: String, surname: String)
  case class SpyRU(imya: String, familia: String)

  val jamesGB = SpyGB("James", "Bond")

  val jamesRU = jamesGB
    .into[SpyRU]
    .withFieldRenamed(_.name, _.imya)
    .withFieldRenamed(_.surname, _.familia)
    .transform
    .tap(println)
  // SpyRU("James", "Bond")

  s"$dash10 Using method accessors $dash10".magenta pipe println

  case class Foo(a: Int) {
    def m: String = "m"
  }
  case class FooV2(a: Int, m: String)

  Foo(1)
    .into[FooV2]
    .enableMethodAccessors
    .transform
    .tap(println)
  // FooV2(1, "m")

  s"$dash10 Transforming coproducts $dash10".magenta pipe println

  sealed trait Color

  object Color {
    case object Red   extends Color
    case object Green extends Color
    case object Blue  extends Color
  }

  sealed trait Channel

  object Channel {
    case object Alpha extends Channel
    case object Blue  extends Channel
    case object Green extends Channel
    case object Red   extends Channel
  }

  val colRed: Color =
    Color
      .Red
      .tap(println)

  val chanRed =
    colRed
      .transformInto[Channel]
      .tap(println)
  // chanRed: Channel = Red

  // chanRed.transformInto[Color]
  // error: Chimney can't derive transformation from Channel to Color
  //
  // Color
  //   can't transform coproduct instance Channel.Alpha to Color
  //
  // Consult https://scalalandio.github.io/chimney for usage examples.
  //
  //        chanRed.transformInto[Color]
  //                             ^

  val red = chanRed
    .into[Color]
    .withCoproductInstance { (_: Channel.Alpha.type) =>
      Color.Blue
    }
    .transform
    .tap(println)
  // red: Color = Red

  val alpha: Channel = Channel.Alpha

  val blue = alpha
    .into[Color]
    .withCoproductInstance { (_: Channel.Alpha.type) =>
      Color.Blue
    }
    .transform
    .tap(println)
  // blue: Color = Blue
}
