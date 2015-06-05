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

  def pluginModuleID(m: ModuleID) : ModuleID = {
    m.extra(PomExtraDependencyAttributes.SbtVersionKey -> sbtV,
      PomExtraDependencyAttributes.ScalaVersionKey -> scalaV)
      .copy(crossVersion = CrossVersion.Disabled)
  }


  lazy val project = Project("scrupal-sbt", new File(".")).
    settings(
      sbtPlugin       := true,
      organization    := "org.scrupal",
      version         := "0.1.0-SNAPSHOT",
      scalaVersion    := "2.10.5",
      scalacOptions   ++= Seq("-deprecation", "-unchecked", "-feature", "-Xlint"),
      logLevel        := Level.Info,
      resolvers       ++= scrupal_resolvers,
      // Scripted - sbt plugin tests
      ScriptedPlugin.scriptedSettings,
      ScriptedPlugin.scriptedLaunchOpts := { ScriptedPlugin.scriptedLaunchOpts.value ++
        Seq("-Xmx1024M", "-XX:MaxPermSize=256M", "-Dplugin.version=" + version.value)
      },
      ScriptedPlugin.scriptedBufferLog := false,
      libraryDependencies ++= Seq (
        "org.scalatest" %% "scalatest" % "2.2.4" % "test",
        pluginModuleID("com.typesafe.play" % "sbt-plugin" % "2.4.0"),
        // sbtPluginID("com.typesafe.sbt" % "sbt-osgi" % "0.8.0-SNAPSHOT"),
        // sbtPluginID("com.typesafe.conductr" % "sbt-conductr" % "0.37.0"),
        pluginModuleID("com.typesafe.sbt" % "sbt-bundle" % "0.23.0"),
        pluginModuleID("org.xerial.sbt" % "sbt-sonatype" % "0.2.2"),
        pluginModuleID("com.typesafe.sbt" % "sbt-javaversioncheck" % "0.1.0"),
        pluginModuleID("com.typesafe.sbt" % "sbt-scalariform" % "1.3.0"),
        pluginModuleID("de.heikoseeberger" % "sbt-header" % "1.5.0"),
        pluginModuleID("com.eed3si9n" % "sbt-unidoc" % "0.3.2"),
        pluginModuleID("com.typesafe.sbt" % "sbt-ghpages" % "0.5.3"),
        pluginModuleID("com.typesafe.sbt" % "sbt-site" % "0.8.1"),
        pluginModuleID("com.typesafe.sbt" % "sbt-git" % "0.8.4"),
        pluginModuleID("com.eed3si9n" % "sbt-sh" % "0.1.0"),
        pluginModuleID("com.jsuereth" % "sbt-pgp" % "1.0.0")
      )
    )
}
