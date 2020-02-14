package chimney

import io.scalaland.chimney.dsl._
import scala.util.chaining._

object Ex03ProvidingMissingValues extends util.App {

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
}
