package chimney

import io.scalaland.chimney.dsl._
import scala.util.chaining._

object Ex02NestedTransformations extends util.App {

  case class Catterpillar(size: Int, name: String)
  case class Butterfly(size: Int, name: String)

  case class Youngs(insects: List[Catterpillar])
  case class Adults(insects: List[Butterfly])

  val kindergarden = Youngs(List(Catterpillar(5, "Steve"), Catterpillar(4, "Joe")))
    .tap(println)

  val highschool = kindergarden
    .transformInto[Adults]
    .tap(println)
}
