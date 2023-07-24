import sbt._

object Dependencies {

  lazy val kittensVersion             = "3.0.0"
  lazy val scalaTestVersion           = "3.2.16"
  lazy val scalaCheckVersion          = "1.17.0"
  lazy val scalaCheckShapelessVersion = "1.2.5"
  lazy val munitVersion               = "0.7.29"
  lazy val chimneyVersion             = "0.7.5"
  lazy val monocleVersion             = "3.2.0"
  lazy val quicklensVersion           = "1.9.6"
  lazy val diffxVersion               = "0.8.3"

  lazy val kittensCore         = "org.typelevel"              %% "kittens"                   % kittensVersion
  lazy val scalaTest           = "org.scalatest"              %% "scalatest"                 % scalaTestVersion
  lazy val scalaTestPlusCheck  = "org.scalatestplus"          %% "scalatestplus-scalacheck"  % "3.1.0.0-RC2"
  lazy val scalaCheck          = "org.scalacheck"             %% "scalacheck"                % scalaCheckVersion
  lazy val scalaCheckShapeless = "com.github.alexarchambault" %% "scalacheck-shapeless_1.14" % scalaCheckShapelessVersion
  lazy val munit               = "org.scalameta"              %% "munit"                     % munitVersion
  lazy val chimney             = "io.scalaland"               %% "chimney"                   % chimneyVersion
  lazy val monocleCore         = "dev.optics"                 %% "monocle-core"              % monocleVersion
  lazy val monocleMacro        = "dev.optics"                 %% "monocle-macro"             % monocleVersion
  lazy val monocleLaw          = "dev.optics"                 %% "monocle-law"               % monocleVersion
  lazy val monocleUnsafe       = "dev.optics"                 %% "monocle-unsafe"            % monocleVersion
  lazy val quickLens           = "com.softwaremill.quicklens" %% "quicklens"                 % quicklensVersion
  lazy val diffxCore           = "com.softwaremill.diffx"     %% "diffx-core"                % diffxVersion
  lazy val diffxScalaTest      = "com.softwaremill.diffx"     %% "diffx-scalatest"           % diffxVersion

  // compilerPlugins
  lazy val kindProjectorVersion    = "0.13.2"
  lazy val betterMonadicForVersion = "0.3.1"

  // FORMAT: OFF

  // https://github.com/typelevel/kind-projector
  lazy val kindProjectorPlugin = compilerPlugin(
    compilerPlugin("org.typelevel" % "kind-projector" % kindProjectorVersion cross CrossVersion.full)
  )
  // https://github.com/oleg-py/better-monadic-for
  lazy val betterMonadicForPlugin = compilerPlugin(
    compilerPlugin("com.olegpy" %% "better-monadic-for" % betterMonadicForVersion)
  )

  // FORMAT: ON
}
