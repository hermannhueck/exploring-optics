package chimney

import io.scalaland.chimney.dsl._
import scala.util.chaining._
import java.time.ZonedDateTime
import scala.util.Random

object Ex00WhatChimneyDoes extends util.App {

  case class MakeCoffee(id: Int, kind: String, addict: String)
  case class CoffeeMade(id: Int, kind: String, forAddict: String, at: ZonedDateTime)

  val command = MakeCoffee(id = Random.nextInt, kind = "Espresso", addict = "Piotr") tap println

  val event = CoffeeMade(id = command.id, kind = command.kind, forAddict = command.addict, at = ZonedDateTime.now) tap println

  val event2 = command
    .into[CoffeeMade]
    .withFieldComputed(_.at, _ => ZonedDateTime.now)
    .withFieldRenamed(_.addict, _.forAddict)
    .transform
    .tap(println)
}
