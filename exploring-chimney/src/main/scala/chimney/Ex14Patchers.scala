package chimney

import io.scalaland.chimney.dsl._
import io.scalaland.chimney.Transformer
import scala.util.chaining._

object Ex14Patchers extends util.App {

  "\n----- Patchers" pipe println

  case class Email(address: String) extends AnyVal
  case class Phone(number: Long)    extends AnyVal

  case class User(id: Int, email: Email, phone: Phone)
  case class UserUpdateForm(email: String, phone: Long)

  // Let's assume you want to apply update form to existing object of type User.

  val user       = User(10, Email("abc@domain.com"), Phone(1234567890L)) tap println
  val updateForm = UserUpdateForm("xyz@domain.com", 123123123L) tap println

  user.patchUsing(updateForm) pipe println
  // User(10, Email("xyz@domain.com"), Phone(123123123L))

  "\n----- Redundant fields in patch" pipe println

  case class UserUpdateForm2(email: String, phone: Long, address: String)

  // user.patchUsing(UserUpdateForm2("xyz@domain.com", 123123123L, "some address"))
  // Field named 'address' not found in target patching type User

  user
    .using(UserUpdateForm2("xyz@domain.com", 123123123L, "some address"))
    .ignoreRedundantPatcherFields
    .patch
    .tap(println)
  // User(10, Email("xyz@domain.com"), Phone(123123123L))

  "\n----- Optional patch values" pipe println

  case class UserPatch(email: Option[String], phone: Option[Phone])

  val update = UserPatch(email = Some("updated@example.com"), phone = None) tap println

  user.patchUsing(update) pipe println
  //  User(10, Email("updated@example.com"), Phone(1234567890L))

  "\n----- Option[T] on both sides" pipe println

  {
    case class User(name: Option[String], age: Option[Int])
    case class UserPatch(name: Option[String], age: Option[Int])

    val user      = User(Some("John"), Some(30)) tap println
    val userPatch = UserPatch(None, None) tap println

    user.patchUsing(userPatch) pipe println
    // clears both fields: User(None, None)

    user
      .using(userPatch)
      .ignoreNoneInPatch
      .patch
      .pipe(println)
    // ignores updating both fields: User(Some("John"), Some(30))

  }
}
