package chimney

import io.scalaland.chimney.dsl._
import scala.util.chaining._

object Ex08DefaultOptionValues extends util.App {

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
}
