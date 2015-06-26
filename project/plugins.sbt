libraryDependencies += "org.scala-sbt" % "scripted-plugin" % sbtVersion.value

scalaVersion := "2.10.4"

addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.0")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "0.2.2")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0")
