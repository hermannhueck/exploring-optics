package blog

import org.scalacheck._
import org.scalatest._
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should.Matchers
import org.scalatestplus.scalacheck._

trait TestSpec
    extends AnyFlatSpec
    with Matchers
    with BeforeAndAfterAll
    with BeforeAndAfterEach
    with ScalaCheckPropertyChecks
    with ScalacheckShapeless {
  final protected type Assertion = compatible.Assertion
}
