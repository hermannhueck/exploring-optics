package chimney

import io.scalaland.chimney.dsl._
import io.scalaland.chimney.Transformer
import scala.util.chaining._
import util.formatting._

object Ex05PlugginInOwnTransformations extends util.App {

  object v1 {
    case class User(id: Int, name: String, street: String, postalCode: String)
  }

  object v2 {
    case class Address(street: String, postalCode: String)
    case class User(id: Int, name: String, addresses: List[Address])
  }

  val v1Users = List(
    v1.User(1, "Steve", "Love street", "27000"),
    v1.User(2, "Anna", "Broadway", "00321")
  ) tap println

  {
    s"$dash10 Transformer type class $dash10".magenta pipe println

    @annotation.nowarn("msg=never used")
    implicit val userV1toV2: Transformer[v1.User, v2.User] =
      (user: v1.User) =>
        v2.User(
          id = user.id,
          name = user.name,
          addresses = List(v2.Address(user.street, user.postalCode))
        )

    @annotation.nowarn("msg=never used")
    val v2Users = v1Users
      .transformInto[List[v2.User]]
      .tap(println)
    // List(
    //   v2.User(1, "Steve", List(Address("Love street", "27000"))),
    //   v2.User(2, "Anna", List(Address("Broadway", "00321")))
    // )
  }

  {
    s"$dash10 Transformer definition DSL $dash10".magenta pipe println

    @annotation.nowarn("msg=never used")
    implicit val userV1toV2: Transformer[v1.User, v2.User] =
      Transformer
        .define[v1.User, v2.User]
        .withFieldComputed(_.addresses, u => List(v2.Address(u.street, u.postalCode)))
        .buildTransformer

    @annotation.nowarn("msg=never used")
    val v2Users = v1Users
      .transformInto[List[v2.User]]
      .tap(println)
    // List(
    //   v2.User(1, "Steve", List(Address("Love street", "27000"))),
    //   v2.User(2, "Anna", List(Address("Broadway", "00321")))
    // )
  }

  s"$dash10 Recursive data types support $dash10".magenta pipe println

  case class Foo(x: Option[Foo])
  case class Bar(x: Option[Bar])

  @annotation.nowarn("msg=Implicit resolves to enclosing method")
  implicit def fooToBarTransformer: Transformer[Foo, Bar] =
    Transformer.derive[Foo, Bar] // or Transformer.define[Foo, Bar].buildTransformer

  Foo(Some(Foo(None)))
    .transformInto[Bar]
    .tap(println)
  // Bar(Some(Bar(None)))
}
