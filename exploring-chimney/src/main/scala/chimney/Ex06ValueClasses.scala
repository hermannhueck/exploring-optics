package chimney

import io.scalaland.chimney.dsl._
import scala.util.chaining._

object Ex06ValueClasses extends util.App {

  object rich {
    case class PersonId(id: Int)        extends AnyVal
    case class PersonName(name: String) extends AnyVal
    case class Person(personId: PersonId, personName: PersonName, age: Int)
  }

  object plain {
    case class Person(personId: Int, personName: String, age: Int)
  }

  val richPerson = rich.Person(rich.PersonId(10), rich.PersonName("Bill"), 30) tap println
  // rich.Person(PersonId(10), PersonName("Bill"), 30)

  val plainPerson = richPerson.transformInto[plain.Person] tap println
  // plain.Person(10, "Bill", 30)

  val richPerson2 = plainPerson.transformInto[rich.Person] tap println
  // rich.Person(PersonId(10), PersonName("Bill"), 30)
}
