package scrupal.sbt

import sbt._

import com.typesafe.sbt.bundle.SbtBundle.autoImport.BundleKeys._
import com.typesafe.sbt.bundle.Import.ByteConversions.IntOps

/** The SBT Bundle Plugin Configuration */
object Bundle extends PluginSettings {

  override def projectSettings : Seq[Setting[_]] = Seq(
    memory := IntOps(1).GiB,
    diskSpace := IntOps(1).GiB,
    nrOfCpus := 2.0
  )
}
