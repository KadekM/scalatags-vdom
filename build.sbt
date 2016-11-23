enablePlugins(ScalaJSPlugin)

name := "Scala.js Tutorial"

scalaVersion in ThisBuild := "2.11.8"

organization := "be.tzbob"
name := "scalatags-vdom"
version := "0.2-SNAPSHOT"

scalaJSUseRhino in Global := false
requiresDOM := true

scalacOptions ++= Seq(
  "-encoding",
  "UTF-8",
  "-target:jvm-1.6",
  "-feature",
  "-deprecation",
  "-Xlint",
  "-Yinline-warnings",
  "-Yno-adapted-args",
  "-Ywarn-dead-code",
  "-Ywarn-numeric-widen",
  "-Ywarn-value-discard",
  "-Xfuture",
  "-language:higherKinds",
  "-language:existentials"
)

libraryDependencies ++= Seq(
  "com.lihaoyi" %%% "scalatags" % "0.6.2",
  "com.lihaoyi" %%% "utest" % "0.3.1" % "test"
)

jsDependencies ++= Seq(
  "org.webjars.bower" % "virtual-dom" % "2.1.1" / "virtual-dom.js"
)

testFrameworks += new TestFramework("utest.runner.Framework")

