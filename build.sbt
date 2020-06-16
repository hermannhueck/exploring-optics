import Dependencies._
import ScalacOptions._

val projectName        = "Exploring Optics"
val projectDescription = "Exploring Optics"
val projectVersion     = "0.1.0"

val scala212               = "2.12.11"
val scala213               = "2.13.2"
val supportedScalaVersions = List(scala212, scala213)

inThisBuild(
  Seq(
    version := projectVersion,
    scalaVersion := scala213,
    crossScalaVersions := supportedScalaVersions,
    publish / skip := true,
    libraryDependencies ++= Seq(
      collectionCompat,
      silencerLib,
      silencerPlugin,
      kindProjectorPlugin,
      betterMonadicForPlugin
    ),
    libraryDependencies ++= Seq(
      scalaTest,
      scalaCheck,
      scalaTestPlusCheck,
      scalaCheckShapeless,
      munit
    ).map(_ % Test),
    Test / parallelExecution := false,
    // S = Small Stack Traces, D = print Duration
    Test / testOptions += Tests.Argument(TestFrameworks.ScalaTest, "-oSD"),
    // run 100 tests for each property // -s = -minSuccessfulTests
    Test / testOptions += Tests.Argument(TestFrameworks.ScalaCheck, "-s", "100"),
    testFrameworks += new TestFramework("munit.Framework"),
    initialCommands :=
      s"""|
          |import scala.util.chaining._
          |println
          |""".stripMargin // initialize REPL
  )
)

lazy val root = (project in file("."))
  .aggregate(`exploring-chimney`, `exploring-monocle`, `exploring-quicklens`, `exploring-diffx`)
  .settings(
    name := projectName,
    description := projectDescription,
    crossScalaVersions := Seq.empty
  )

lazy val `exploring-chimney` = (project in file("exploring-chimney"))
  .dependsOn(compat213, util)
  .settings(
    name := "exploring-chimney",
    description := "Exploring Chimney for case class manipulation",
    scalacOptions ++= scalacOptionsFor(scalaVersion.value),
    console / scalacOptions := removeScalacOptionXlintUnusedForConsoleFrom(scalacOptions.value),
    libraryDependencies ++= Seq(chimney)
  )

lazy val `exploring-monocle` = (project in file("exploring-monocle"))
  .dependsOn(compat213, util)
  .settings(
    name := "exploring-monocle",
    description := "Exploring optics library Monocle",
    scalacOptions ++= scalacOptionsFor(scalaVersion.value),
    console / scalacOptions := removeScalacOptionXlintUnusedForConsoleFrom(scalacOptions.value),
    libraryDependencies ++= Seq(
      monocleMacro,
      monocleLaw,
      monocleUnsafe,
      alleycatsCore,
      kittensCore,
      scalaCheck,
      scalaCheckShapeless
    ),
    libraryDependencies ++= {
      if (scalaVersion.value.startsWith("2.13"))
        Seq.empty // in 2.13 we add scalacOption: -Ymacro-annotations // see project/ScalacOptions.scala
      else
        Seq(macroParadise)
    }
  )

lazy val `exploring-quicklens` = (project in file("exploring-quicklens"))
  .dependsOn(compat213, util)
  .settings(
    name := "exploring-quicklens",
    description := "Exploring QuickLencs for case class manipulation",
    scalacOptions ++= scalacOptionsFor(scalaVersion.value),
    console / scalacOptions := removeScalacOptionXlintUnusedForConsoleFrom(scalacOptions.value),
    libraryDependencies ++= Seq(quickLens)
  )

lazy val `exploring-diffx` = (project in file("exploring-diffx"))
  .dependsOn(compat213, util)
  .settings(
    name := "exploring-diffx",
    description := "Exploring diffx for case class comparison",
    scalacOptions ++= scalacOptionsFor(scalaVersion.value),
    console / scalacOptions := removeScalacOptionXlintUnusedForConsoleFrom(scalacOptions.value),
    libraryDependencies ++= Seq(
      diffxCore,
      diffxScalaTest
    )
  )

lazy val compat213 = (project in file("compat213"))
  .settings(
    name := "compat213",
    description := "compat library providing features of Scala 2.13 backported to 2.12",
    scalacOptions ++= scalacOptionsFor(scalaVersion.value)
  )

lazy val util = (project in file("util"))
  .enablePlugins(BuildInfoPlugin)
  .settings(
    name := "util",
    description := "Utilities",
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, sbtVersion),
    buildInfoPackage := "build",
    scalacOptions ++= scalacOptionsFor(scalaVersion.value)
  )
