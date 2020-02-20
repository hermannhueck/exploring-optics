package monocle.optics

import scala.util.chaining._
import util.formatting._

object Ex05Traversal extends util.App {

  s"${line(10)} Traversal:".green pipe println
  "  A Traversal is the generalisation of an Optional to several targets. In other word, a Traversal allows to focus from a type S into 0 to n values of type A." pipe println
  "  The most common example of a Traversal would be to focus into all elements inside of a container (e.g. List, Vector, Option). To do this we will use the relation between the typeclass cats.Traverse and Traversal:" pipe println

  s"${line(10)} Traversal".green pipe println

  import monocle.Traversal
  import cats.implicits._ // to get all cats instances including Traverse[List]

  val xs = List(1, 2, 3, 4, 5) tap println

  val eachL: Traversal[List[Int], Int] = Traversal.fromTraverse[List, Int]
  // eachL: monocle.Traversal[List[Int],Int] = monocle.PTraversal$$anon$5@70d25509
  eachL.set(0)(xs) pipe println
  // res0: List[Int] = List(0, 0, 0, 0, 0)
  eachL.modify(_ + 1)(xs) pipe println
  // res1: List[Int] = List(2, 3, 4, 5, 6)

  s"${line(10)} A Traversal is also a Fold ...".green pipe println

  eachL.getAll(xs) pipe println
  // res2: List[Int] = List(1, 2, 3, 4, 5)
  eachL.headOption(xs) pipe println
  // res3: Option[Int] = Some(1)
  eachL.find(_ > 3)(xs) pipe println
  // res4: Option[Int] = Some(4)
  eachL.all(_ % 2 == 0)(xs) pipe println
  // res5: Boolean = false
  eachL.exist(_ % 2 == 0)(xs) pipe println
  // res5: Boolean = true

  s"${line(10)} Traversal: smart constructors".green pipe println

  case class Point(id: String, x: Int, y: Int)

  val points: Traversal[Point, Int] = Traversal.apply2[Point, Int](_.x, _.y)((x, y, p) => p.copy(x = x, y = y))

  points.set(5)(Point("bottom-left", 0, 0)) pipe println
  // res6: Point = Point(bottom-left,5,5)

  s"${line(10)} Custom Traversal with modifyF".green pipe println

  import cats.Applicative
  import alleycats.std.map._ // to get Traverse instance for Map (SortedMap does not require this import)

  def filterKey[K, V](predicate: K => Boolean): Traversal[Map[K, V], V] =
    new Traversal[Map[K, V], V] {

      def modifyF[F[_]: Applicative](f: V => F[V])(s: Map[K, V]): F[Map[K, V]] =
        s.map {
          case (k, v) =>
            k -> (if (predicate(k)) f(v) else v.pure[F])
        }.sequence // sequence requires: alleycats.std.map._
    }

  val m = Map(1 -> "one", 2 -> "two", 3 -> "three", 4 -> "Four")

  val filterEven: Traversal[Map[Int, String], String] = filterKey[Int, String](_ % 2 == 0)
  // filterEven: monocle.Traversal[Map[Int,String],String] = $anon$1@622e88d7

  filterEven.modify(_.toUpperCase)(m) pipe println
  // res7: Map[Int,String] = Map(4 -> FOUR, 3 -> three, 2 -> TWO, 1 -> one)

  s"${line(10)} Traversal Laws".green pipe println

  import monocle.law.discipline.TraversalTests
  import cats.derived.auto.eq._ // from kittens
  import cats.implicits._
  import org.scalacheck.ScalacheckShapeless._

  checkRules(TraversalTests(eachL).all, "Traversal", "eachL")
  checkRules(TraversalTests(points).all, "Traversal", "points")
  checkRules(TraversalTests(filterEven).all, "Traversal", "filterEven")
}
