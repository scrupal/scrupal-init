package org.scrupal

import sbt._
import sbt.Keys._
import scrupal.sbt.ScrupalPlugin
import ScrupalPlugin.autoImport._
import xerial.sbt.Sonatype

/** Settings For SonatypePublishing Plugin */
object SonatypePublishing extends PluginSettings {


  def targetRepository: Def.Initialize[Option[Resolver]] = Def.setting {
    val nexus = "https://oss.sonatype.org/"
    val snapshotsR = "snapshots" at nexus + "content/repositories/snapshots"
    val releasesR  = "releases"  at nexus + "service/local/staging/deploy/maven2"
    val resolver = if (isSnapshot.value) snapshotsR else releasesR
    Some(resolver)
  }

  val defaultScmInfo = Def.setting {
    val gitUrl = "//github.com/scrupal/" + normalizedName.value + ".git"
    ScmInfo(url("https:" ++ gitUrl), "scm:git:" ++ gitUrl, Some("https:" ++ gitUrl) )
  }

  override def projectSettings = Sonatype.sonatypeSettings ++ Seq(
    Sonatype.SonatypeKeys.profileName := "org.scrupal",
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    licenses := Seq("Apache2" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    homepage := Some(new URL("http://scrupal.org/modules/" + normalizedName.value)),
    pomExtra :=
      <scm>
        <url>{scmInfo.value.getOrElse(defaultScmInfo.value).browseUrl.toString}</url>
        <connection>{scmInfo.value.getOrElse(defaultScmInfo.value).connection}</connection>
      </scm>
      <developers>
        <developer>
          <id>{scrupalCopyrightHolder.value}</id>
          <name>{scrupalCopyrightHolder.value}</name>
          <url>{scrupalDeveloperUrl.value}</url>
        </developer>
      </developers>
  )
}
