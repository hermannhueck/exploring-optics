package monocle.optics

import scala.util.chaining._
import util.formatting._

object Ex01Iso extends util.App {

  s"${line(10)} Iso:".green pipe println
  "  An Iso is an optic which converts elements of type S into elements of type A without loss." pipe println
  "  Consider a case class Person with two fields: name , age" pipe println
  "  Person is equivalent to a tuple (String, Int) and a tuple (String, Int) is equivalent to Person. So we can create an Iso between Person and (String, Int) using two total functions:" pipe println
  "  get: Person => (String, Int)" pipe println
  "  reverseGet (aka apply): (String, Int) => Person" pipe println

  s"${line(10)} Iso case class <-> Tuple".green pipe println

  case class Person(name: String, age: Int)

  import monocle.Iso
  val personIsoTuple = Iso[Person, (String, Int)](p => (p.name, p.age)) { case (name, age) => Person(name, age) }

  val tuple: (String, Int) = personIsoTuple.get(Person("Zoe", 25))
  tuple pipe println

  val person: Person = personIsoTuple.reverseGet(("Zoe", 25))
  person pipe println

  val person2: Person = personIsoTuple.apply(("Zoe", 25))
  person2 pipe println

  s"${line(10)} Iso List <-> Vector".green pipe println

  def listToVector[A] = Iso[List[A], Vector[A]](_.toVector)(_.toList)

  listToVector.get(List(1, 2, 3)) pipe println

  def vectorToList[A] = listToVector[A].reverse

  vectorToList.get(Vector(1, 2, 3)) pipe println

  s"${line(10)} Iso List[Char] <-> String".green pipe println

  val stringToList = Iso[String, List[Char]](_.toList)(_.mkString(""))

  stringToList.modify(_.tail)("Hello") pipe println

  s"${line(10)} Iso List[Char] <-> String".green pipe println

  case class MyString(s: String)
  case class Foo()
  case object Bar

  import monocle.macros.GenIso

  val mystringToString = GenIso[MyString, String]
  val string           = mystringToString.get(MyString("Hello")) tap println
  val mystring         = mystringToString.reverseGet(string) tap println

  val fooToUnit = GenIso.unit[Foo]
  val unit      = fooToUnit.get(Foo()) tap println
  val foo       = fooToUnit.reverseGet(unit) tap println

  val barToUnit = GenIso.unit[Bar.type]
  val unit2     = barToUnit.get(Bar) tap println
  val bar       = barToUnit.reverseGet(unit2) tap println

  s"${line(10)} Iso Laws".green pipe println

  "TODO: IsoTests" pipe println
}
