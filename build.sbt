name := "website-vacuum-script"

organization := "jml.example"

version := "0.0.1"

scalaVersion := "2.11.8"

libraryDependencies ++= Seq(
  "org.scalatest" % "scalatest_2.11" % "2.2.6" % "test" withSources() withJavadoc(),
  "org.scalacheck" % "scalacheck_2.11" % "1.13.1" withSources() withJavadoc(),
  "org.scala-lang.modules" % "scala-xml_2.11" % "1.0.4" withSources() withJavadoc(),
  "org.ccil.cowan.tagsoup" % "tagsoup" % "1.2.1" withSources() withJavadoc(),
  "com.lihaoyi" %% "scalatags" % "0.5.5" withSources() withJavadoc()
)

initialCommands := "import jml.example._"

