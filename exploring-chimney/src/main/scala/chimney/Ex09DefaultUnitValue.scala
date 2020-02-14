package chimney

import io.scalaland.chimney.dsl._
import scala.util.chaining._

object Ex09DefaultUnitValue extends util.App {

  case class Foo(a: Int, b: String)
  case class Bar(a: Int, b: String, newField: Unit)

  Foo(5, "test")
    .into[Bar]
    .withFieldConst(_.newField, ()) // should work w/o this line
    .transform
    .pipe(println)
  // Bar(5, "test", ())
}
