import sbt._
import sbt.Keys._

import java.io.File

import sbt.mavenint.PomExtraDependencyAttributes

object ScrupalSbtPluginBuilder extends Build {
  lazy val scrupal_resolvers = Seq(
    "BinTray" at "https://dl.bintray.com/sbt/sbt-plugin-releases",
    Resolver.sonatypeRepo("releases"),
    "Typesafe repository" at "http://repo.typesafe.com/typesafe/releases/",
    "jgit-repo" at "http://download.eclipse.org/jgit/maven",
    Resolver.sonatypeRepo("snapshots")
  )

  val scalaV = "2.10"
  val sbtV = "0.13"

  def sbtPluginID(m: ModuleID) : ModuleID = {
    m.extra(PomExtraDependencyAttributes.SbtVersionKey -> sbtV,
      PomExtraDependencyAttributes.ScalaVersionKey -> scalaV)
      .copy(crossVersion = CrossVersion.Disabled)
  }


  lazy val project = Project("scrupal-sbt", new File(".")).
    settings(
      sbtPlugin       := true,
      organization    := "org.scrupal",
      version         := "0.1.0-SNAPSHOT",
      scalaVersion    := "2.10.4",
      scalacOptions   ++= Seq("-deprecation", "-unchecked", "-feature", "-Xlint"),
      logLevel        := Level.Info,
      resolvers       ++= scrupal_resolvers,
      libraryDependencies ++= Seq (
        "org.scalatest" %% "scalatest" % "2.2.4" % "test",
        sbtPluginID("com.typesafe.sbt" % "sbt-scalariform" % "1.3.0"),
        sbtPluginID("com.eed3si9n" % "sbt-unidoc" % "0.3.2"),
        sbtPluginID("com.typesafe.sbt" % "sbt-git" % "0.8.4"),
        sbtPluginID("com.typesafe.sbt" % "sbt-ghpages" % "0.5.3"),
        sbtPluginID("com.eed3si9n" % "sbt-assembly" % "0.13.0"),
        sbtPluginID("com.typesafe.sbt" % "sbt-site" % "0.8.1"),
        sbtPluginID("com.typesafe.play" % "sbt-plugin" % "2.4.0")
      )
    )
}
