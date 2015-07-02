/**********************************************************************************************************************
 * This file is part of Scrupal, a Scalable Reactive Web Application Framework for Content Management                 *
 *                                                                                                                    *
 * Copyright (c) 2015, Reactific Software LLC. All Rights Reserved.                                                   *
 *                                                                                                                    *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance     *
 * with the License. You may obtain a copy of the License at                                                          *
 *                                                                                                                    *
 *     http://www.apache.org/licenses/LICENSE-2.0                                                                     *
 *                                                                                                                    *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed   *
 * on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for  *
 * the specific language governing permissions and limitations under the License.                                     *
 **********************************************************************************************************************/

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
      version         := "0.2.0-SNAPSHOT",
      scalaVersion    := "2.10.4",
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
        pluginModuleID("com.typesafe.play" % "sbt-plugin" % "2.4.1"),
        pluginModuleID("com.etsy" % "sbt-compile-quick-plugin" % "0.5.3"),
     // pluginModuleID("com.typesafe.conductr" % "sbt-conductr" % "0.37.0"),
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
        pluginModuleID("com.jsuereth" % "sbt-pgp" % "1.0.0"),
        pluginModuleID("org.scoverage" % "sbt-scoverage" % "1.0.4"),
        pluginModuleID("org.scoverage" % "sbt-coveralls" % "1.0.0"),
        pluginModuleID("com.eed3si9n" % "sbt-buildinfo" % "0.4.0"),
        pluginModuleID("com.github.gseitz" % "sbt-release" % "1.0.1"),
        pluginModuleID("com.typesafe.sbt" % "sbt-license-report" % "1.0.0")
      )
    ).
    settings(SonatypePublishing.projectSettings:_*)
}

object SonatypePublishing {

  import xerial.sbt.Sonatype

  def targetRepository: Def.Initialize[Option[Resolver]] = Def.setting {
    val nexus = "https://oss.sonatype.org/"
    val snapshotsR = "snapshots" at nexus + "content/repositories/snapshots"
    val releasesR  = "releases"  at nexus + "content/repositories/releases"
    val resolver = if (isSnapshot.value) snapshotsR else releasesR
    Some(resolver)
  }

  def projectSettings = Sonatype.sonatypeSettings ++ Seq(
    Sonatype.SonatypeKeys.profileName := "org.scrupal",
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    licenses := Seq("Apache2" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    homepage := Some(new URL("http://scrupal.org/modules/" + normalizedName.value)),
    pomExtra :=
      <url>http://scrupal.org/modules/scrupal-sbt</url>
      <licenses>
        <license>
          <name>Apache 2</name>
          <url>http://www.apache.org/licenses/LICENSE-2.0</url>
          <distribution>repo</distribution>
        </license>
      </licenses>
      <scm>
        <url>git@github.com:scrupal/scrupal-sbt.git</url>
        <connection>scm:git:git@github.com:scrupal/scrupal-sbt.git</connection>
      </scm>
      <developers>
        <developer>
          <id>reid-spencer</id>
          <name>Reid Spencer</name>
          <url>https://github.com/reid-spencer</url>
        </developer>
      </developers>
  )
}
