package monocle.optics

import scala.util.chaining._
import util.formatting._

object Ex02Lens extends util.App {

  s"${line(10)} Lens:".green pipe println
  "  A Lens is an optic used to zoom inside a Product, e.g. case class, Tuple, HList or even Map." pipe println
  "  Lenses have two type parameters generally called S and A: Lens[S, A] where S represents the Product and A an element inside of S." pipe println
  "  Let’s take a simple case class with two fields:" pipe println
  "  get: Address => Int" pipe println
  "  set: Int => Address => Address" pipe println

  s"${line(10)} street number lens".green pipe println

  case class Address(streetNumber: Int, streetName: String)

  val address = Address(10, "High Street")
  address pipe println

  import monocle.Lens

  val lensStreetNumber: Lens[Address, Int] =
    Lens[Address, Int](_.streetNumber)(n => a => a.copy(streetNumber = n))

  println()
  lensStreetNumber.get(address) pipe println
  lensStreetNumber.replace(5)(address) pipe println

  s"${line(10)} macro-generated street number lens".green pipe println

  import monocle.macros.GenLens

  val lensStreetNumber2: Lens[Address, Int] =
    GenLens[Address](_.streetNumber)

  lensStreetNumber2.get(address) pipe println
  lensStreetNumber2.replace(5)(address) pipe println

  s"${line(10)} lens.modify".green pipe println

  val n = lensStreetNumber2.get(address) tap println
  lensStreetNumber2.replace(n + 1)(address) pipe println
  // modify is the same as get followed by set
  lensStreetNumber2.modify(_ + 1)(address) pipe println

  s"${line(10)} lens.modifyF with List".green pipe println

  def neighbors(n: Int): List[Int] =
    if (n > 0) List(n - 1, n + 1) else List(n + 1)

  import cats.instances.list._ // to get Functor instance for List

  lensStreetNumber2.modifyF(neighbors)(address) pipe println
  lensStreetNumber2.modifyF(neighbors)(Address(135, "High Street")) pipe println

  s"${line(10)} lens.modifyF with Future".green pipe println

  import scala.concurrent._
  import scala.concurrent.ExecutionContext.Implicits.global
  import cats.instances.future._ // to get Functor instance for Future

  def updateNumber(n: Int): Future[Int] = Future.successful(n + 1)

  lensStreetNumber2.modifyF(updateNumber)(address) pipe println

  s"${line(10)} composition of lenses".green pipe println

  case class Person(name: String, age: Int, address: Address)
  val john = Person("John", 20, address)

  val lensAddress: Lens[Person, Address] = GenLens[Person](_.address)

  (lensAddress andThen lensStreetNumber2).get(john) pipe println
  (lensAddress andThen lensStreetNumber2).replace(2)(john) pipe println

  s"${line(10)} other ways of lens composition".green pipe println

  val lensJohnToMike: Person => Person = GenLens[Person](_.name).replace("Mike") compose
    GenLens[Person](_.age).modify(_ + 1)
  john pipe lensJohnToMike pipe println

  import monocle.macros.syntax.lens._
  john.lens(_.name).replace("Mike").lens(_.age).modify(_ + 1) pipe println

  s"${line(10)} composing prism with lens".green pipe println

  import monocle.std.option.some

  case class B(c: Int)
  case class A(b: Option[B])

  val c = GenLens[B](_.c)
  val b = GenLens[A](_.b)

  // (b composePrism some composeLens c).getOption(A(Some(B(1)))) pipe println
  b.some.andThen(c).getOption(A(Some(B(1)))) pipe println

  s"${line(10)} lens generation".green pipe println

  val ageLens = GenLens[Person](_.age)
  ageLens.modify(_ + 10)(john) tap println
  GenLens[Person](_.address.streetName).replace("Iffley Road")(john) pipe println

  s"${line(10)} @Lenses macro annotation".green pipe println

  import monocle.macros.Lenses
  @Lenses case class Point(x: Int, y: Int)
  val p = Point(5, 3)

  Point.x.get(p) pipe println
  Point.y.get(p) pipe println
  Point.y.replace(0)(p) pipe println

  @Lenses("_") case class Point2(x: Int, y: Int)
  val p2 = Point2(5, 3)

  Point2._x.get(p2) pipe println

  s"${line(10)} Lens Laws".green pipe println

  import monocle.law.discipline.LensTests
  import cats.derived.auto.eq._ // from kittens
  import cats.instances.all._
  import org.scalacheck.ScalacheckShapeless._

  checkRules(LensTests(lensStreetNumber).all, "Lens", "lensStreetNumber")
  checkRules(LensTests(lensStreetNumber2).all, "Lens", "lensStreetNumber2")
  checkRules(LensTests(lensAddress).all, "Lens", "lensAddress")
  checkRules(
    LensTests((lensAddress andThen lensStreetNumber2)).all,
    "Lens",
    "(lensAddress composeLens lensStreetNumber2)"
  )
  checkRules(LensTests(c).all, "Lens", "c")
  checkRules(LensTests(b).all, "Lens", "b")
  checkRules(LensTests(ageLens).all, "Lens", "ageLens")
  checkRules(LensTests(GenLens[Person](_.address.streetName)).all, "Lens", "GenLens[Person](_.address.streetName)")
  checkRules(LensTests(Point.x).all, "Lens", "Point.x")
  checkRules(LensTests(Point.y).all, "Lens", "Point.y")
}
