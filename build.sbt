import SonatypeKeys._

sonatypeSettings

lazy val root = project.in(file(".")).
  enablePlugins(ScalaJSPlugin).
  aggregate(cssJS, cssJVM).
  settings(
    publish := {},
    publishLocal := {}
  )

lazy val css = crossProject.in(file(".")).
  settings(
    name := "cssparse",
    version := "0.1",
    scalaVersion := "2.11.6",
    libraryDependencies += "com.lihaoyi" %%% "fastparse" % "0.2.1"
  )

lazy val cssJS = css.js
lazy val cssJVM = css.jvm

organization := "org.querki"

homepage := Some(url("http://www.querki.net/"))

licenses += ("MIT License", url("http://www.opensource.org/licenses/mit-license.php"))

scmInfo := Some(ScmInfo(
    url("https://github.com/jducoeur/cssparse"),
    "scm:git:git@github.com/jducoeur/cssparse.git",
    Some("scm:git:git@github.com/jducoeur/cssparse.git")))

publishMavenStyle := true

publishTo := {
  val nexus = "https://oss.sonatype.org/"
  if (isSnapshot.value)
    Some("snapshots" at nexus + "content/repositories/snapshots")
  else
    Some("releases" at nexus + "service/local/staging/deploy/maven2")
}

pomExtra := (
  <developers>
    <developer>
      <id>jducoeur</id>
      <name>Mark Waks</name>
      <url>https://github.com/jducoeur/</url>
    </developer>
  </developers>
)

pomIncludeRepository := { _ => false }
