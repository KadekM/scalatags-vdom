enablePlugins(ScalaJSPlugin)

name := "scalatags-vdom"

val scalaV = "2.11.8"

scalaVersion in ThisBuild := scalaV
organization in ThisBuild := "com.marekkadek"

name := "scalatags-vdom"

val common = Seq(
  requiresDOM := true,
  scalacOptions ++= Seq(
    // following two lines must be "together"
    "-encoding",
    "UTF-8",
    "-Xlint",
    "-Xlint:missing-interpolator",
    "-deprecation",
    "-feature",
    "-unchecked",
    "-Ywarn-dead-code",
    "-Yno-adapted-args",
    "-language:existentials",
    "-language:higherKinds",
    "-language:implicitConversions",
    "-Ywarn-value-discard",
    "-Ywarn-numeric-widen"
  ),
  testFrameworks += new TestFramework("utest.runner.Framework")
)

val libsDeps = Seq(
  libraryDependencies ++= Seq(
    "com.lihaoyi" %%% "scalatags" % "0.6.2",
    "com.lihaoyi" %%% "utest"     % "0.3.1" % "test"
  ),
  jsDependencies ++= Seq(
    "org.webjars.bower" % "virtual-dom"   % "2.1.1" / "virtual-dom.js"
    //"org.webjars.npm"   % "dom-delegator" % "13.1.0" / "dom-delegator.js"
  )
)

lazy val vdom =
  Project(id = "vdom", base = file("modules/vdom")).settings(common, libsDeps).enablePlugins(ScalaJSPlugin)

lazy val sample = Project(id = "sample", base = file("modules/sample"))
  .settings(common, libsDeps)
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(vdom)
  .aggregate(vdom)
