package monocle.examples

import scala.util.chaining._

@annotation.nowarn("cat=deprecation")
object UniversityApp extends util.App {

  case class Lecturer(firstName: String, lastName: String, salary: Int)
  case class Department(budget: Int, lecturers: List[Lecturer])
  case class University(name: String, departments: Map[String, Department])

  val uni = University(
    "oxford",
    Map(
      "Computer Science" -> Department(
        45,
        List(
          Lecturer("john", "doe", 10),
          Lecturer("robert", "johnson", 16)
        )
      ),
      "History" -> Department(
        30,
        List(
          Lecturer("arnold", "stones", 20)
        )
      )
    )
  ) tap println

  import monocle.macros.GenLens // require monocle-macro module
  import monocle.function.At.at // to get at Lens
  // import monocle.std.map._      // to get Map instance for At

  val departments = GenLens[University](_.departments)

  (departments composeLens at("History")).set(None)(uni) pipe println

  val physics = Department(
    36,
    List(
      Lecturer("daniel", "jones", 12),
      Lecturer("roger", "smith", 14)
    )
  ) tap println

  (departments composeLens at("Physics")).set(Some(physics))(uni) pipe println

  val lecturers = GenLens[Department](_.lecturers)
  val salary    = GenLens[Lecturer](_.salary)

  import monocle.function.all._ // to get each and other typeclass based optics such as at or headOption
  import monocle.Traversal
  import monocle.unsafe.MapTraversal._ // to get Each instance for Map (SortedMap does not require this import)

  val allLecturers: Traversal[University, Lecturer] =
    departments composeTraversal each composeLens lecturers composeTraversal each

  (allLecturers composeLens salary).modify(_ + 2)(uni) pipe println

  val firstName = GenLens[Lecturer](_.firstName)
  val lastName  = GenLens[Lecturer](_.lastName)

  // import monocle.std.string._ // to get String instance for Cons

  val upperCasedFirstName = (allLecturers composeLens firstName composeOptional headOption)
    .modify(_.toUpper)(uni) tap println

  val upperCasedFirstAndName = (allLecturers composeLens lastName composeOptional headOption)
    .modify(_.toUpper)(upperCasedFirstName) tap println

  val firstAndLastNames = Traversal.apply2[Lecturer, String](_.firstName, _.lastName) {
    case (fn, ln, l) => l.copy(firstName = fn, lastName = ln)
  }

  val upperCasedFirstAndName2 = (allLecturers composeTraversal firstAndLastNames composeOptional headOption)
    .modify(_.toUpper)(uni) tap println
}
