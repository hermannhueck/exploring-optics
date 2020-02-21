package blog

import com.softwaremill.diffx.scalatest.DiffMatcher
import com.softwaremill.diffx._
import java.time.LocalDate
import java.time.format.DateTimeFormatter

case class Person(age: Int, name: String)
case class Family(people: List[Person])

/*
  See also:
  https://blog.softwaremill.com/human-readable-case-class-diffs-c707e83e08a2
 */
class DiffxSpec extends TestSpec with DiffMatcher {

  "2 Persons with name Mike" should "not match" in {

    Person(11, "Mike") shouldNot matchTo(Person(12, "Mike"))
  }

  "2 LocalDates" should "not match" in {

    import scala.language.implicitConversions

    implicit def diffForLocalDate(difForString: Diff[String]): Diff[LocalDate] =
      new Diff[LocalDate] {

        override def apply(left: LocalDate, right: LocalDate, toIgnore: List[FieldPath]): DiffResult = {
          val formatter = DateTimeFormatter.ISO_DATE
          difForString.apply(formatter.format(left), formatter.format(right))
        }
      }

    val today: LocalDate    = LocalDate.now()
    val tomorrow: LocalDate = today.plusDays(1L)

    today shouldNot matchTo(tomorrow)
  }

  "2 Families" should "match respecting only the name" in {

    /*
    implicit val om: ObjectMatcher[Person] = new ObjectMatcher[Person] {
      override def isSameObject(left: Person, right: Person): Boolean = {
        left.name == right.name
      }
    }
     */
    implicit val om: ObjectMatcher[Person] =
      (left: Person, right: Person) => left.name == right.name

    implicit val dd: Derived[Diff[List[Person]]] =
      new Derived(Diff[Set[Person]].contramap(_.toSet))

    Family(List(Person(11, "Mike"))) should matchTo(Family(List(Person(12, "Mike"))))
  }

  "2 Persons with name Mike" should "match when ignoring the age" in {

    implicit val modifiedDiff: Diff[Person] = Derived[Diff[Person]].ignore(_.age)

    Person(11, "Mike") should matchTo(Person(12, "Mike"))
  }
}
