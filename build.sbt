enablePlugins(ScalaJSPlugin)

name := "scalatags-vdom"

val scalaV      = "2.11.8"
val crossScalaV = Seq("2.11.8", "2.12.1")

scalaVersion in ThisBuild := scalaV
//scalaOrganization in ThisBuild := "org.typelevel"
organization in ThisBuild := "com.marekkadek"

name := "scalatags-vdom"

val noPublishSettings = Seq(
  publish := (),
  publishLocal := (),
  publishArtifact := false
)

val publishSettings = Seq(
  homepage := Some(url("https://github.com/KadekM/scalatags-vdom")),
  organizationHomepage := Some(url("https://github.com/KadekM/scalatags-vdom")),
  licenses += ("MIT license", url("http://www.opensource.org/licenses/mit-license.php")),
  publishMavenStyle := true,
  publishArtifact in Test := false,
  releasePublishArtifactsAction := PgpKeys.publishSigned.value,
  publishTo := {
    val nexus = "https://oss.sonatype.org/"
    if (isSnapshot.value)
      Some("snapshots" at nexus + "content/repositories/snapshots")
    else
      Some("releases" at nexus + "service/local/staging/deploy/maven2")
  },
  pomIncludeRepository := { _ =>
    false
  },
  pomExtra :=
    <scm>
      <url>git@github.com:kadekm/scalatags-vdom.git</url>
      <connection>scm:git:git@github.com:kadekm/scalatags-vdom.git</connection>
    </scm>
      <developers>
        <developer>
          <id>kadekm</id>
          <name>Marek Kadek</name>
          <url>https://github.com/KadekM</url>
        </developer>
      </developers>)

noPublishSettings

val common = Seq(
  scalaVersion := scalaV,
  crossScalaVersions := crossScalaV,
  releaseCrossBuild := true,
  requiresDOM := true,
  skip in packageJSDependencies := false,
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
    //"-Xlog-implicits",
    "-Ywarn-numeric-widen"
  ),
  testFrameworks += new TestFramework("utest.runner.Framework")
)

val libsDeps = Seq(
  libraryDependencies ++= Seq(
    "com.lihaoyi" %%% "scalatags" % "0.6.2",
    "com.lihaoyi" %%% "utest"     % "0.4.4" % "test"
  ),
  jsDependencies ++= Seq(
    "org.webjars.bower" % "virtual-dom" % "2.1.1" / "virtual-dom.js"
    //,"org.webjars" % "requirejs" % "2.1.22" / "require.js"
    //,"org.webjars.npm"   % "dom-delegator" % "13.1.0" / "13.1.0/dom-delegator.js"
  )
)

lazy val vdom =
  Project(id = "scalatags-vdom", base = file("modules/vdom"))
    .settings(common, publishSettings, libsDeps)
    .enablePlugins(ScalaJSPlugin)

lazy val sample = Project(id = "sample", base = file("modules/sample"))
  .settings(common, noPublishSettings, libsDeps)
  .settings(
    libraryDependencies += "com.marekkadek" %%% "rxscala-js-cats" % "0.2.0-SNAPSHOT" changing()
  )
  .enablePlugins(ScalaJSPlugin)
  .dependsOn(vdom)
  .aggregate(vdom)
