package chimney

import io.scalaland.chimney.dsl._
import scala.util.chaining._

object Ex04DefaultValues extends util.App {

  case class Catterpillar(size: Int, name: String)
  case class Butterfly(size: Int, name: String, wingsColor: String = "purple")

  val stevie = Catterpillar(5, "Steve") tap println

  val steve = stevie.transformInto[Butterfly] tap println

  // does not compile with default values disabled
  // val steve2 = stevie
  //   .into[Butterfly]
  //   .disableDefaultValues
  //   .transform
  // Chimney can't derive transformation from chimney.Ex04DefaultValues.Catterpillar to chimney.Ex04DefaultValues.Butterfly
}
