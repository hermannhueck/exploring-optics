package chimney

import io.scalaland.chimney.dsl._
import scala.util.chaining._

object Ex13CoproductsSupport extends util.App {

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

  val colRed: Color = Color.Red
  val chanRed       = colRed.transformInto[Channel]
  // chanRed: Channel = Red
  chanRed.tap(println)

  // chanRed.transformInto[Color]
  // can't transform coproduct instance chimney.Ex13CoproductsSupport.Channel.Alpha to chimney.Ex13CoproductsSupport.Color

  val red = chanRed
    .into[Color]
    .withCoproductInstance { (_: Channel.Alpha.type) =>
      Color.Blue
    }
    .transform
    .tap(println)
  // red: Color = Red

  val alpha: Channel = Channel.Alpha
  alpha tap println

  val blue = alpha
    .into[Color]
    .withCoproductInstance { (_: Channel.Alpha.type) =>
      Color.Blue
    }
    .transform
    .tap(println)
  // blue: Color = Blue
}
