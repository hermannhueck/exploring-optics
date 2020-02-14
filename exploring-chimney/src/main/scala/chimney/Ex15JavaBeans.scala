package chimney

import io.scalaland.chimney.dsl._
import io.scalaland.chimney.Transformer
import scala.util.chaining._

object Ex15JavaBeans extends util.App {

  "\n----- Reading from Java beans" pipe println

  class MyBean1(private var id: Long, private var name: String, private var flag: Boolean) {
    def getId: Long     = id
    def getName: String = name
    def isFlag: Boolean = flag

    override def toString = s"MyBean1(id = $id, name = $name, flag = $flag)"
  }

  case class MyCaseClass(id: Long, name: String, flag: Boolean)

  // The conversion works if you explicitly enable it with .enableBeanGetters:

  val myBean1 = new MyBean1(1L, "beanie", true) tap println

  myBean1
    .into[MyCaseClass]
    .enableBeanGetters
    .transform
    .pipe(println)
  //  MyCaseClass(1L, "beanie", true)

  "\n----- Writing to Java beans" pipe println

  class MyBean2 {
    private var id: Long      = _
    private var name: String  = _
    private var flag: Boolean = _

    def getId: Long           = id
    def setId(id: Long): Unit = { this.id = id }

    def getName: String             = name
    def setName(name: String): Unit = { this.name = name }

    def isFlag: Boolean              = flag
    def setFlag(flag: Boolean): Unit = { this.flag = flag }

    override def toString = s"MyBean2(id = $id, name = $name, flag = $flag)"
  }

  // The conversion works if you explicitly enable it with .enableBeanSetters:

  val myCaseClassInstance =
    MyCaseClass(10L, "beanie", true) tap println

  val myBean2 =
    myCaseClassInstance
      .into[MyBean2]
      .enableBeanSetters
      .transform
      .tap(println)
}
