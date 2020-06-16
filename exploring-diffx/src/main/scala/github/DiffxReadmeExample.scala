package github

import scala.util.chaining._
import util.formatting._
import com.softwaremill.diffx._

/*
  See also:
  https://github.com/softwaremill/diffx
 */
object DiffxReadmeExample extends util.App {

  sealed trait Parent
  case class A(id: String, name: String) extends Parent
  case class B(id: String, name: String) extends Parent

  val a1: Parent = A("1", "X")
  val a2: Parent = A("2", "X")
  val b1: Parent = B("1", "X")

  {
    s"${line(10)} diffing a1 and a2:".green pipe println
    Diff[Parent]
      .apply(a1, a2)
      .ensuring(!_.isIdentical) pipe println

    s"${line(10)} diffing a1 and b1:".green pipe println
    Diff[Parent]
      .apply(a1, b1)
      .ensuring(!_.isIdentical) pipe println
  }

  {
    s"${line(10)} diffing a1 and a2, ignoring field 'id':".green pipe println
    implicit val diffA: Derived[Diff[A]] = Derived(Diff.gen[A].value.ignore((x: A) => x.id))
    Diff[Parent]
      .apply(a1, a2)
      .ensuring(_.isIdentical) pipe println
  }
}
