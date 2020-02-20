package monocle.optics

import scala.util.chaining._
import util.formatting._

object Ex04Optional extends util.App {

  s"${line(10)} Optional:".green pipe println
  "  An Optional is an Optic used to zoom inside a Product, e.g. case class, Tuple, HList or even Map. Unlike the Lens, the element that the Optional focuses on may not exist." pipe println
  "  Optionals have two type parameters generally called S and A: Optional[S, A] where S represents the Product and A an optional element inside of S." pipe println
  "  Let’s take a simple list with integers." pipe println
  "  We can create an Optional[List[Int], Int] which zooms from a List[Int] to its potential head by supplying a pair of functions:" pipe println
  "  getOption: List[Int] => Option[Int]" pipe println
  "  set: Int => List[Int] => List[Int]" pipe println

  s"${line(10)} Optional".green pipe println

  import monocle.Optional

  val head: Optional[List[Int], Int] =
    Optional[List[Int], Int] {
      case Nil    => None
      case x :: _ => Some(x)
    } { a =>
      {
        case Nil     => Nil
        case _ :: xs => a :: xs
      }
    }

  val xs = List(1, 2, 3)
  val ys = List.empty[Int]

  head.nonEmpty(xs) pipe println // true
  head.nonEmpty(ys) pipe println // false

  s"${line(10)} getOrModify".green pipe println

  head.getOrModify(xs) pipe println
  head.getOrModify(ys) pipe println

  s"${line(10)} getOption, set".green pipe println

  head.getOption(xs) pipe println // Some(1)
  head.set(5)(xs) pipe println    // List(5, 2, 3)
  head.getOption(ys) pipe println // None
  head.set(5)(ys) pipe println    // List()

  s"${line(10)} modify".green pipe println

  head.modify(_ + 1)(xs) pipe println       // List(2, 2, 3)
  head.modify(_ + 1)(ys) pipe println       // List()
  head.modifyOption(_ + 1)(xs) pipe println // Some(List(2, 2, 3))
  head.modifyOption(_ + 1)(ys) pipe println // None

  s"${line(10)} Optional Laws".green pipe println

  import monocle.law.discipline.OptionalTests
  import cats.derived.auto.eq._ // from kittens
  import cats.instances.all._
  import org.scalacheck.ScalacheckShapeless._

  checkRules(OptionalTests(head).all, "Optional", "head")
}
