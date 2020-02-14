package chimney

import io.scalaland.chimney.dsl._
import io.scalaland.chimney.Transformer
import scala.util.chaining._

object Ex12RecursiveDataTypes extends util.App {

  case class Foo(x: Option[Foo])
  case class Bar(x: Option[Bar])

  implicit def fooToBarTransformer: Transformer[Foo, Bar] =
    Transformer.derive[Foo, Bar] // or Transformer.define[Foo, Bar].buildTransformer

  Foo(Some(Foo(None)))
    .transformInto[Bar]
    .tap(println)
  // Bar(Some(Bar(None)))
}
