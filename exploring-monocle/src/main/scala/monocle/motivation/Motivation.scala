package monocle.motivation

import scala.util.chaining._

object Motivation extends util.App {

  case class Street(number: Int, name: String)
  case class Address(city: String, street: Street)
  case class Company(name: String, address: Address)
  case class Employee(name: String, company: Company)

  val employee = Employee("john", Company("awesome inc", Address("london", Street(23, "high street"))))

  employee pipe println
  println()

  // FORMAT: OFF
  val employee2 = employee.copy(
    company = employee.company.copy(
      address = employee.company.address.copy(
        street = employee.company.address.street.copy(
          name = employee.company.address.street.name.capitalize // luckily capitalize exists
        )
      )
    )
  )
  // FORMAT: ON

  employee2 pipe println

  import monocle.Lens
  import monocle.macros.GenLens

  val company: Lens[Employee, Company] = GenLens[Employee](_.company)
  val address: Lens[Company, Address]  = GenLens[Company](_.address)
  val street: Lens[Address, Street]    = GenLens[Address](_.street)
  val streetName: Lens[Street, String] = GenLens[Street](_.name)

  val composedLens: Lens[Employee, String] =
    company andThen address andThen street andThen streetName
  composedLens.modify(_.capitalize)(employee) pipe println

  import monocle.function.Cons.headOption // to use headOption (an optic from Cons typeclass)
  import monocle.function.Cons.stringCons

  @annotation.nowarn("cat=deprecation")
  val composedLens2 =
    company andThen address andThen street andThen streetName andThen headOption
  composedLens2.modify(_.toUpper)(employee) pipe println

  import monocle.macros.syntax.lens._

  @annotation.nowarn("cat=deprecation")
  val composedLens3 =
    employee.lens(_.company.address.street.name).andThen(headOption)
  composedLens3.modify(_.toUpper) pipe println
}
