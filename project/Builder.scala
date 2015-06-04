import sbt._
import sbt.Keys._

import java.io.File

object ScrupalSbtPluginBuilder extends Build {
  lazy val scrupal_resolvers = Seq(
    Resolver.sonatypeRepo("releases"),
    "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
    "jgit-repo" at "http://download.eclipse.org/jgit/maven",
    Resolver.sonatypeRepo("snapshots")
  )

  lazy val project = Project("scrupal-sbt", new File(".")).
    settings(
      sbtPlugin       := true,
      organization    := "com.reactific",
      version         := "0.1.0-SNAPSHOT",
      scalaVersion    := "2.10.4",
      scalacOptions   ++= Seq("-deprecation", "-unchecked", "-feature", "-Xlint"),
      logLevel        := Level.Info,
      resolvers       ++= scrupal_resolvers,
      libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"
  //    libraryDependencies += "org.scala-sbt" % "scripted-plugin" % sbtVersion.value

  // libraryDependencies += "com.eed3si9n" % "sbt-assembly" % "0.13.0"

  // libraryDependencies += "com.eed3si9n" % "sbt-unidoc" % "0.3.2"

  // libraryDependencies += "com.typesafe.sbt" % "sbt-scalariform" % "1.3.0"

  // libraryDependencies += "com.typesafe.sbt" % "sbt-ghpages" % "0.5.3"

  // libraryDependencies += "com.typesafe.sbt" % "sbt-git" % "0.8.4"

  // libraryDependencies += "com.typesafe.sbt" % "sbt-site" % "0.8.1"

  // libraryDependencies += "com.github.gseitz" % "sbt-release" % "1.0.0"

  // Scripted - sbt plugin tests
//  scriptedSettings

 // scriptedLaunchOpts += "-Dproject.version=" + version.value

    )
}
