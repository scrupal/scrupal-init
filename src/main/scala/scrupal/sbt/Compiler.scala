package scrupal.sbt

import sbt.Keys._
import sbt._

/** Compiler Settings Needed */
object Compiler extends PluginSettings {
  override def projectSettings : Seq[Setting[_]] = Seq(
    scalaVersion := "2.11.6",
    javaOptions in test ++= Seq("-Xmx512m"),
    scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation", "-target:jvm-1.8"),
    scalacOptions in(Compile, doc) ++=
      Seq("-feature", "-unchecked", "-deprecation", "-diagrams", "-implicits", "-skip-packages", "samples")

  )
}
