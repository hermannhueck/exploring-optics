package chimney

import io.scalaland.chimney.dsl._
import scala.util.chaining._
import util.formatting._

object Ex01GettingStartedWithTransformations extends util.App {

  case class Catterpillar(size: Int, name: String)
  case class Butterfly(size: Int, name: String)

  val stevie = Catterpillar(5, "Steve")
    .tap(println)

  s"$dash10 Basic transformations $dash10".magenta pipe println

  val steve = stevie
    .transformInto[Butterfly]
    .tap(println)

  s"$dash10 Nested transformations $dash10".magenta pipe println

  case class Youngs(insects: List[Catterpillar])
  case class Adults(insects: List[Butterfly])

  val kindergarden = Youngs(List(Catterpillar(5, "Steve"), Catterpillar(4, "Joe")))
    .tap(println)

  val highschool = kindergarden
    .transformInto[Adults]
    .tap(println)
}
