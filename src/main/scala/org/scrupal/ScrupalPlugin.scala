package org.scrupal

import sbt._
import sbt.Keys._

import sbt.complete.Parsers._
import sbtunidoc.{Plugin => SbtUnidocPlugin}
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtGit
import com.typesafe.sbt.GitPlugin
import com.typesafe.sbt.SbtSite
import com.typesafe.sbt.SbtGhPages
import play.sbt.{Play => PlayPlugin }

/** The ScrupalPlugin For Scrupal Based Modules
 */
object ScrupalPlugin extends AutoPlugin {
  // Not AutoPlugins: Scalariform, Unidoc, SbtSite, SbtGhPages, SbtUnidocPlugin

  override def requires = PlayPlugin && sbtassembly.AssemblyPlugin && GitPlugin
  override def trigger = noTrigger

  /**
   * Defines all settings/tasks that get automatically imported,
   * when the plugin is enabled
   */
  object autoImport {
    val hello = inputKey[Unit]("Prints Hello")
    val compileOnly = inputKey[Unit]("Compile just the specified files")
  }

  // Get all the autoImport keys into this namespace for easier reference
  import autoImport._

  /**
   * Define the values of the settings
   */
  override def projectSettings: Seq[Setting[_]] = Seq(
    helloSetting
  )

  override def globalSettings: Seq[Setting[_]] = Seq (
    compileOnlySetting
  )

  def helloSetting: Setting[_] = hello := {
    // Sbt provided logger.
    val log = streams.value.log
    log.info("Hello task")
  }

  def compileOnlySetting : Setting[_] = compileOnly := {
    val args: Seq[String] = spaceDelimited("<arg>").parsed
    args foreach println
  }

}
