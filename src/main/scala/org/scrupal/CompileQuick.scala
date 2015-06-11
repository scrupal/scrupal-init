package scrupal.sbt

import sbt._
import sbt.Keys._

/** Settings For CompileQuick Plugin
  *
  * Description of thing
  */
object CompileQuick extends PluginSettings {

  import com.etsy.sbt.CompileQuick.CompileQuickTasks._

  override def projectSettings : Seq[Setting[_]] =
    com.etsy.sbt.CompileQuick.compileQuickSettings  ++ Seq(
      packageQuickOutput := new File(baseDirectory.value, "libs")
    )
}
