package org.scrupal

import com.typesafe.sbt.SbtGhPages

/** Settings For GitHub Pages Plugin */
object GhPages extends PluginSettings {

  override def projectSettings = SbtGhPages.projectSettings

  override def buildSettings = SbtGhPages.buildSettings
}
