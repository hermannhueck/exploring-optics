package chimney

import io.scalaland.chimney.dsl._
import scala.util.chaining._

object Ex07FieldRelabelling extends util.App {

  case class SpyGB(name: String, surname: String)
  case class SpyRU(imya: String, familia: String)

  val jamesGB = SpyGB("James", "Bond") tap println

  val jamesRU = jamesGB
    .into[SpyRU]
    .withFieldRenamed(_.name, _.imya)
    .withFieldRenamed(_.surname, _.familia)
    .transform
    .tap(println)
  // SpyRU("James", "Bond")
}
