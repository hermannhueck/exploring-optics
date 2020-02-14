package chimney

import io.scalaland.chimney.dsl._
import io.scalaland.chimney.Transformer
import scala.util.chaining._

object Ex11aCustomTransformations extends util.App {

  object v1 {
    case class User(id: Int, name: String, street: String, postalCode: String)
  }

  object v2 {
    case class Address(street: String, postalCode: String)
    case class User(id: Int, name: String, addresses: List[Address])
  }

  implicit val userV1toV2: Transformer[v1.User, v2.User] =
    (user: v1.User) =>
      v2.User(
        id = user.id,
        name = user.name,
        addresses = List(v2.Address(user.street, user.postalCode))
      )

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
