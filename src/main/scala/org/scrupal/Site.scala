package scrupal.sbt

/** Settings For Site Plugin */
object Site extends PluginSettings {
  import com.typesafe.sbt.SbtSite
  override def projectSettings = SbtSite.settings
}
