package org.scrupal

import sbt._
import sbt.Keys._

import scrupal.sbt.ScrupalPlugin

/** Title Of Thing.
  *
  * Description of thing
  */
object Settings extends PluginSettings {

  val filter = { (ms: Seq[(File, String)]) =>
    ms filter {
      case (file, path) =>
        path != "logback.xml" && !path.startsWith("toignore") && !path.startsWith("samples")
    }
  }

  override def projectSettings : Seq[sbt.Def.Setting[_]] = Defaults.coreDefaultSettings ++
    Seq(
      scalacOptions in(Compile, doc) ++= Opts.doc.title(ScrupalPlugin.autoImport.scrupalTitle.value),
      scalacOptions in(Compile, doc) ++= Opts.doc.version(version.value),
      fork in Test := false,
      logBuffered in Test := false,
      ivyScala := ivyScala.value map {_.copy(overrideScalaVersion = true)},
      shellPrompt := ShellPrompt.buildShellPrompt(version.value),
      mappings in(Compile, packageBin) ~= filter,
      mappings in(Compile, packageSrc) ~= filter,
      mappings in(Compile, packageDoc) ~= filter)
}
