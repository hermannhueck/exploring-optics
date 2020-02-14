package chimney

import io.scalaland.chimney.dsl._
import io.scalaland.chimney.Transformer
import scala.util.chaining._

object Ex11bCustomTransformations extends util.App {

  object v1 {
    case class User(id: Int, name: String, street: String, postalCode: String)
  }

  object v2 {
    case class Address(street: String, postalCode: String)
    case class User(id: Int, name: String, addresses: List[Address])
  }

  // compilation error, or will lead to StackOverflowError at runtime
  // implicit val userV1toV2: Transformer[v1.User, v2.User] =
  //   (user: v1.User) =>
  //     user
  //       .into[v2.User]
  //       .withFieldComputed(_.addresses, u => List(v2.Address(u.street, u.postalCode)))
  //       .transform

  implicit val userV1toV2: Transformer[v1.User, v2.User] =
    Transformer
      .define[v1.User, v2.User]
      .withFieldComputed(_.addresses, u => List(v2.Address(u.street, u.postalCode)))
      .buildTransformer

  val v1Users = List(
    v1.User(1, "Steve", "Love street", "27000"),
    v1.User(2, "Anna", "Broadway", "00321")
  ) tap println

  val v2Users = v1Users.transformInto[List[v2.User]] tap println
  // List(
  //   v2.User(1, "Steve", List(Address("Love street", "27000"))),
  //   v2.User(2, "Anna", List(Address("Broadway", "00321")))
  // )

}
