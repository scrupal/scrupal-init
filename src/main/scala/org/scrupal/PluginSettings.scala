package org.scrupal

import sbt._

/** Basic configuration of a plugin */
trait PluginSettings {
  /** The [[sbt.Configuration]]s to add to each project that activates this [[sbt.AutoPlugin]].*/
  def projectConfigurations: Seq[Configuration] = Nil

  /** The [[sbt.Setting]]s to add in the scope of each project that activates this [[sbt.AutoPlugin]]. */
  def projectSettings: Seq[Setting[_]] = Nil

  /** The [[sbt.Setting]]s to add to the build scope for each project that activates this [[sbt.AutoPlugin]].
    * The settings returned here are guaranteed to be added to a given build scope only once
    * regardless of how many projects for that build activate this AutoPlugin. */
  def buildSettings: Seq[Setting[_]] = Nil
}
