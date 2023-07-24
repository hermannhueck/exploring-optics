package chimney

import io.scalaland.chimney.dsl._
import scala.util.chaining._
import util.formatting._

object Ex04DefaultValuesSupport extends util.App {

  case class Catterpillar(size: Int, name: String)
  case class Butterfly(size: Int, name: String, wingsColor: String = "purple")

  val stevie =
    Catterpillar(5, "Steve")
      .tap(println)

  s"$dash10 Automatic value provision $dash10".magenta pipe println

  val steve =
    stevie
      .into[Butterfly]
      .enableDefaultValues
      .transform
      .tap(println)

  stevie
    .into[Butterfly]
    .withFieldConst(_.wingsColor, "yellow")
    .transform
    .tap(println)
  // Butterfly(5, "Steve", "yellow")

  s"$dash10 Disabling default values in generated transformer $dash10".magenta pipe println

  // does not compile with default values disabled
  // val steve2 = stevie
  //   .into[Butterfly]
  //   .disableDefaultValues
  //   .transform
  // Chimney can't derive transformation from chimney.Ex04DefaultValues.Catterpillar to chimney.Ex04DefaultValues.Butterfly

  s"$dash10 Default values for Option fields $dash10".magenta pipe println

  case class Foo(a: Int, b: String)
  case class FooV2(a: Int, b: String, newField: Option[Double])

  Foo(5, "test")
    .into[FooV2]
    .withFieldConst(_.newField, None)
    .transform
    .pipe(println)
  // FooV2(5, "test", None)

  Foo(5, "test")
    .into[FooV2]
    .enableOptionDefaultsToNone
    .transform
    .pipe(println)
  // FooV2(5, "test", None)

  s"$dash10 Default values for Unit fields $dash10".magenta pipe println

  case class FooX(x: Int, y: String)
  case class Bar(x: Int, y: String, z: Unit)

  FooX(10, "test")
    .transformInto[Bar]
    .pipe(println)
  // Foox(10, test, ())
}
