package monocle

package object optics {

  import scala.util.chaining._
  import util.formatting._
  import org.scalacheck.Properties

  def checkRules(props: Properties, opticType: String, opticName: String): Unit = {
    s"${line(10)} Checking rules for $opticType: $opticName".green pipe println
    props
      .properties
      .map(_._2)
      .foreach(_.check())
  }
}
