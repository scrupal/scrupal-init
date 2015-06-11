package scrupal.sbt

import sbt._
import sbt.Keys._

import com.typesafe.sbt.{GitPlugin, JavaVersionCheckPlugin}
import com.typesafe.sbt.JavaVersionCheckPlugin.autoImport._
import com.typesafe.sbt.bundle.SbtBundle
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging

import de.heikoseeberger.sbtheader.HeaderPlugin

import play.sbt.PlayScala

import sbtsh.ShPlugin

/** The ScrupalPlugin For Scrupal Based Modules */
object ScrupalPlugin extends AutoPlugin {

  val autoplugins : Seq[AutoPlugin] = Seq( PlayScala, JavaAppPackaging, JavaVersionCheckPlugin,
    GitPlugin, HeaderPlugin, SbtBundle, ShPlugin)

  override def requires = {
    // Enable all the AutoPlugin instances listed in autoplugins
    autoplugins.foldLeft(empty) { (b,plugin) => b && plugin }
  }

  // Not AutoPlugins Yet: CompileQuick, Scalariform, Unidoc, SbtSite, SbtGhPages, SbtUnidocPlugin :(

  /** Settings For Plugins that are not yet AutoPlugins so we can mimic them.
    * This trait provides the same settings methods as an AutoPlugin. See [[sbt.AutoPlugin]]
    * This is used to override settings in both AutoPlugins and regular Plugins.
    */
  val pluginSettings : Seq[PluginSettings] = Seq(
    CompileQuick, Compiler, Settings, Bundle, /*Scalariform,*/ Unidoc, SonatypePublishing, Site, GhPages
  )


  override def trigger = noTrigger

  /**
   * Defines all settings/tasks that get automatically imported,
   * when the plugin is enabled
   */
  object autoImport {
    val scrupalTitle = settingKey[String]("A title for the Scrupal module for use in documentation")
    val scrupalCopyrightHolder = settingKey[String]("The name of the copyright holder for the scrupal module")
    val scrupalCopyrightYears = settingKey[Seq[Int]]("The years in which the copyright was in place")
    val scrupalDeveloperUrl = settingKey[URL]("The URL for the developer's home page")

    val compileOnly = TaskKey[File]("compile-only", "Compile just the specified files")

    val printClasspath = TaskKey[File]("print-class-path", "Print the project's compilation class path.")
    val printTestClasspath = TaskKey[File]("print-test-class-path", "Print the project's testing class path.")
    val printRuntimeClasspath = TaskKey[File]("print-runtime-class-path", "Print the project's runtime class path.")
  }

  val scrupalResolvers = Seq(
    "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
    "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
    "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/",
    "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/",
    "Scalaz Bintray" at "http://dl.bintray.com/scalaz/releases"
  )

  // Get all the autoImport keys into this namespace for easier reference

  import autoImport._

  /**
   * Define the values of the settings
   */
  override def projectSettings: Seq[Setting[_]] = Defaults.coreDefaultSettings ++
    autoplugins.foldLeft(Seq.empty[Setting[_]]) { (s,p) => s ++ p.projectSettings } ++
    pluginSettings.foldLeft(Seq.empty[Setting[_]]) { (s,p) => s ++ p.projectSettings } ++
    Seq(
      resolvers := scrupalResolvers,
      javaVersionPrefix in javaVersionCheck := Some("1.8"),
      ivyScala  := ivyScala.value map { _.copy(overrideScalaVersion = true) },
      printClasspath <<= Commands.print_class_path,
      printTestClasspath <<= Commands.print_test_class_path,
      printRuntimeClasspath <<= Commands.print_runtime_class_path,
      compileOnly <<= Commands.compile_only,
      libraryDependencies ++= Seq(
        "org.specs2"    %% "specs2-core"     % "3.6.1"     % "test",
        "org.specs2"    %% "specs2-junit"    % "3.6.1"     % "test",
        "ch.qos.logback" % "logback-classic" % "1.1.3"     % "test"
      )
    )

  override def buildSettings : Seq[Setting[_]] = Defaults.buildCore ++
    Seq(
      baseDirectory := thisProject.value.base,
      target := baseDirectory.value / "target"
    ) ++
    Commands.aliases ++
    autoplugins.foldLeft(Seq.empty[Setting[_]]) { (s,p) => s ++ p.buildSettings } ++
    pluginSettings.foldLeft(Seq.empty[Setting[_]]) { (s,p) => s ++ p.buildSettings }
}






















