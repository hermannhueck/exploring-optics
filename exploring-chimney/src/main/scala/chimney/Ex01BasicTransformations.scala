package chimney

import io.scalaland.chimney.dsl._
import scala.util.chaining._

object Ex01BasicTransformations extends util.App {

  case class Catterpillar(size: Int, name: String)
  case class Butterfly(size: Int, name: String)

  val stevie = Catterpillar(5, "Steve")
    .tap(println)

  val steve = stevie
    .transformInto[Butterfly]
    .tap(println)
}
