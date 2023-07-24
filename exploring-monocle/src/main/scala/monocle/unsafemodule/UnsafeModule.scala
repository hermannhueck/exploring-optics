package monocle.unsafemodule

import scala.util.chaining._

object UnsafeModule extends util.App {

  case class Person(name: String, age: Int)

  val john  = Person("John Doe", 38)
  val jimmy = Person("Jimmy Doe", 8)

  import monocle.unsafe.UnsafeSelect
  import monocle.macros.GenLens
  import monocle.Optional

  // deprecated:
  // val unsafe = (UnsafeSelect.unsafeSelect[Person](_.age >= 18) composeLens
  //   GenLens[Person](_.name)).modify("Adult: " + _)
  // replace with:
  val unsafe = (Optional.filter[Person](_.age >= 18) andThen
    GenLens[Person](_.name)).modify("Adult: " + _)

  unsafe(john) pipe println
  unsafe(jimmy) pipe println

  // This operator is considered unsafe because it allows for inconsistency
  // if a Lens is then used to change one of the values used in the predicates.
  // For example:

  // deprecated:
  // val unsafe2 = (UnsafeSelect.unsafeSelect[Person](_.age >= 18) composeLens
  //   GenLens[Person](_.age)).set(0)
  // replace with:
  val unsafe2 = (Optional.filter[Person](_.age >= 18) andThen
    GenLens[Person](_.age)).replace(0)

  unsafe2(john) pipe println
}
