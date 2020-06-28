package github

import scala.util.chaining._
import util.formatting._
import com.softwaremill.diffx._

/*
  See also:
  https://github.com/softwaremill/diffx
 */
object DiffxReadmeExample extends util.App {

  {
    sealed trait Parent
    case class Bar(s: String, i: Int)                              extends Parent
    case class Foo(bar: Bar, b: List[Int], parent: Option[Parent]) extends Parent

    val right: Foo = Foo(
      Bar("asdf", 5),
      List(123, 1234),
      Some(Bar("asdf", 5))
    )
    // right: Foo = Foo(Bar("asdf", 5), List(123, 1234), Some(Bar("asdf", 5)))

    val left: Foo = Foo(
      Bar("asdf", 66),
      List(1234),
      Some(right)
    )
    // left: Foo = Foo(
    //   Bar("asdf", 66),
    //   List(1234),
    //   Some(Foo(Bar("asdf", 5), List(123, 1234), Some(Bar("asdf", 5))))
    // )

    import com.softwaremill.diffx._
    compare(left, right) pipe println

  }

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
    @annotation.nowarn("msg=never used")
    implicit val diffA: Derived[Diff[A]] = Derived(Diff.gen[A].value.ignore((x: A) => x.id))
    Diff[Parent]
      .apply(a1, a2)
      .ensuring(_.isIdentical) pipe println
  }
}
