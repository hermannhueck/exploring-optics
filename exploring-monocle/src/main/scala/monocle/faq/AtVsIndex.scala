package monocle.faq

import scala.util.chaining._

object AtVsIndex extends util.App {

  import monocle.Iso
  import monocle.function.all._

  val m = Map("one" -> 1, "two" -> 2) tap println

  val root = Iso.id[Map[String, Int]]

  // (root composeOptional index("two")).set(0)(m) pipe println // update value at index "two"
  root.index("two").replace(0)(m) pipe println // update value at index "two"
// res1: Map[String,Int] = Map(one -> 1, two -> 0)

  // (root composeOptional index("three")).set(3)(m) pipe println // noop because m doesn't have a value at "three"
  root.index("three").replace(3)(m) pipe println // noop because m doesn't have a value at "three"
  // res2: Map[String,Int] = Map(one -> 1, two -> 2)

  // (root composeLens at("three")).set(Some(3))(m) pipe println // insert element at "three"
  root.at("three").replace(Some(3))(m) pipe println // insert element at "three"
  // res3: Map[String,Int] = Map(one -> 1, two -> 2, three -> 3)

  // (root composeLens at("two")).set(None)(m) pipe println // delete element at "two"
  root.at("two").replace(None)(m) pipe println // delete element at "two"
  // res4: Map[String,Int] = Map(one -> 1)

  // (root composeLens at("two")).set(Some(0))(m) pipe println // upsert element at "two"
  root.at("two").replace(Some(0))(m) pipe println // upsert element at "two"
  // res5: Map[String,Int] = Map(one -> 1, two -> 0)

  // Note: root is a trick to help type inference. Without it, we would get ambiguous implicit error
  // The problem is that the compiler does not have enough information to infer the correct Index instance.
  // By using Iso.id[Map[String, Int]] as a prefix, we give a hint to the type inference
  // saying we focus on a Map[String, Int]. Similarly, if the Map was in a case class,
  // a Lens would provide the same kind of hint than Iso.id

  println()
  case class Bar(kv: Map[String, Int])

  import monocle.macros.GenLens

  // (GenLens[Bar](_.kv) composeOptional index("two")).set(0)(Bar(m)) pipe println
  GenLens[Bar](_.kv).index("two").replace(0)(Bar(m)) pipe println
// res7: Bar = Bar(Map(one -> 1, two -> 0))
}
