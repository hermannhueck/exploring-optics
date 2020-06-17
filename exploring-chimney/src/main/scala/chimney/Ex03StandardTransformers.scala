package chimney

import io.scalaland.chimney.dsl._
import scala.util.chaining._
import util.formatting._

object Ex03StandardTransformers extends util.App {

  s"$dash10 Identity transformation $dash10".magenta pipe println

  case class Catterpillar(size: Int, name: String)
  case class Butterfly(size: Int, name: String)

  1234.transformInto[Int] tap println                        // 1234: Int
  true.transformInto[Boolean] tap println                    // true: Bool
  3.14159.transformInto[Double] tap println                  // 3.14159: Double
  "test".transformInto[String] tap println                   // test: String
  Butterfly(3, "Steve").transformInto[Butterfly] tap println // Butterfly(3, Steve): Butterfly

  s"$dash10 Supertype transformation $dash10".magenta pipe println

  class Vehicle(val maxSpeed: Double)
  class Car(maxSpeed: Double, val seats: Int) extends Vehicle(maxSpeed)

  (new Car(180, 5))
    .transformInto[Vehicle]
    .tap(println)
  // Vehicle(180.0)

  s"$dash10 Value classes $dash10".magenta pipe println

  object rich {
    case class PersonId(id: Int)        extends AnyVal
    case class PersonName(name: String) extends AnyVal
    case class Person(personId: PersonId, personName: PersonName, age: Int)
  }

  object plain {
    case class Person(personId: Int, personName: String, age: Int)
  }

  val richPerson = rich.Person(rich.PersonId(10), rich.PersonName("Bill"), 30) tap println
  // rich.Person(PersonId(10), PersonName("Bill"), 30)

  val plainPerson = richPerson.transformInto[plain.Person] tap println
  // plain.Person(10, "Bill", 30)

  val richPerson2 = plainPerson.transformInto[rich.Person] tap println
  // rich.Person(PersonId(10), PersonName("Bill"), 30)

  s"$dash10 Options $dash10".magenta pipe println

  Some(1234)
    .transformInto[Option[Int]]
    .tap(println)
  // Some(1234): Option[Int]
  Option
    .empty[Int]
    .transformInto[Option[Int]]
    .tap(println)
  // None: Option[Int]

  Some("test")
    .transformInto[Option[String]]
    .tap(println)
  // Some(test): Option[String]
  Option
    .empty[String]
    .transformInto[Option[String]]
    .tap(println)
  // None: Option[String]

  Some(new Car(180, 5))
    .transformInto[Option[Vehicle]]
    .tap(println)
  // Some(Vehicle(180.0)): Option[Vehicle]
  Option
    .empty[Car]
    .transformInto[Option[Vehicle]]
    .tap(println)
  // None: Option[Vehicle]

  Some(rich.Person(rich.PersonId(10), rich.PersonName("Bill"), 30))
    .transformInto[Option[plain.Person]]
    .tap(println)
  // Some(plain.Person(10, "Bill", 30)): Option[plain.Person]
  Option
    .empty[rich.Person]
    .transformInto[Option[plain.Person]]
    .tap(println)
  // None: Option[plain.Person]

  s"$dash10 Collections $dash10".magenta pipe println

  List(123, 456)
    .transformInto[Array[Int]]
    .tap(println)
  // Array(123, 456)

  Seq("foo", "bar")
    .transformInto[Vector[String]]
    .tap(println)
  // Vector(foo, bar)

  Vector(new Car(160, 4), new Car(220, 5))
    .transformInto[List[Vehicle]]
    .tap(println)
  // List(Vehicle(160), Vehicle(220))

  s"$dash10 Maps $dash10".magenta pipe println

  Map(1 -> "Alice", 2 -> "Bob")
    .transformInto[Map[Int, rich.PersonName]]
    .tap(println)
  // Map(1 -> PersonName(Alice), 2 -> PersonName(Bob))

  Map(rich.PersonId(10) -> new Car(200, 5), rich.PersonId(22) -> new Car(170, 4))
    .transformInto[Map[Int, Vehicle]]
    .tap(println)
  // Map(10 -> Vehicle(200), 22 -> Vehicle(170))

  s"$dash10 Either $dash10".magenta pipe println

  (Right("Batman"): Either[Int, String])
    .transformInto[Either[rich.PersonId, rich.PersonName]]
    .tap(println)
  // Right(PersonName(Batman)): Either[PersonId, PersonName]

  (Left(10): Either[Int, String])
    .transformInto[Either[rich.PersonId, rich.PersonName]]
    .tap(println)
  // Left(PersonId(10)): Either[PersonId, PersonName]

  (Right(Array(10, 20)): Either[String, Array[Int]])
    .transformInto[Either[String, List[Int]]]
    .tap(println)
  // Right(List(10, 20)): Either[String, List[Int]]

  (Left("test"): Either[String, Array[Int]])
    .transformInto[Either[String, List[Int]]]
    .tap(println)
  // Left(test): Either[String, List[Int]]
}
