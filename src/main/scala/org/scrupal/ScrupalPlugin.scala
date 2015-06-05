package scrupal.sbt


import scala.language.postfixOps

import sbt._
import sbt.Keys._
import sbt.complete.Parsers._

import com.typesafe.sbt.{GitVersioning, GitPlugin, JavaVersionCheckPlugin}
import com.typesafe.sbt.JavaVersionCheckPlugin.autoImport._
import com.typesafe.sbt.bundle.SbtBundle
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging

import de.heikoseeberger.sbtheader.HeaderPlugin

import play.sbt.{Play => PlayPlugin, PlayScala}

import sbtsh.ShPlugin

/** The ScrupalPlugin For Scrupal Based Modules */
object ScrupalPlugin extends AutoPlugin {

  // Not AutoPlugins Yet: Scalariform, Unidoc, SbtSite, SbtGhPages, SbtUnidocPlugin :(

  override def requires =
    PlayScala && JavaAppPackaging && GitPlugin && HeaderPlugin && SbtBundle &&
    JavaVersionCheckPlugin && ShPlugin && GitVersioning

  override def trigger = noTrigger

  /**
   * Defines all settings/tasks that get automatically imported,
   * when the plugin is enabled
   */
  object autoImport {
    val scrupalCompileOnly = inputKey[Unit]("Compile just the specified files")
    val scrupalTitle = settingKey[String]("A title for the Scrupal module for use in documentation")
    val scrupalCopyrightHolder = settingKey[String]("The name of the copyright holder for the scrupal module")
    val scrupalCopyrightYears = settingKey[Seq[Int]]("The years in which the copyright was in place")
    val scrupalDeveloperUrl = settingKey[URL]("The URL for the developer's home page")
  }

  val scrupalResolvers = Seq(
    "Typesafe Releases" at "http://repo.typesafe.com/typesafe/releases/",
    "Typesafe Snapshots" at "http://repo.typesafe.com/typesafe/snapshots/",
    "Sonatype Releases" at "https://oss.sonatype.org/content/repositories/releases/",
    "Sonatype Snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"
  )

  // Get all the autoImport keys into this namespace for easier reference

  import autoImport._

  /**
   * Define the values of the settings
   */
  override def projectSettings: Seq[Setting[_]] = PlayPlugin.projectSettings ++ Defaults.coreDefaultSettings ++
    Compiler.projectSettings ++
    Scalariform.projectSettings ++
    Unidoc.projectSettings ++
    SonatypePublishing.projectSettings ++
    Site.projectSettings ++
    GhPages.projectSettings ++
    Seq(
      organization := "",
      resolvers := scrupalResolvers,
      ivyScala  := ivyScala.value map { _.copy(overrideScalaVersion = true) },
      javaVersionPrefix in javaVersionCheck := Some("1.8")
    )

  override def buildSettings : Seq[Setting[_]] = Defaults.buildCore
  override def globalSettings: Seq[Setting[_]] = Seq(
    compileOnlySetting
  )

  def compileOnlySetting: Setting[_] = scrupalCompileOnly := {
    val args: Seq[String] = spaceDelimited("<arg>").parsed
    args foreach println
  }
}

object Compiler {
  lazy val projectSettings : Seq[Setting[_]] = Defaults.coreDefaultSettings ++ Seq(
    scalaVersion := "2.11.6",
    javaOptions in test ++= Seq("-Xmx512m"),
    scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation", "-target:jvm-1.8"),
    scalacOptions in(Compile, doc) ++=
      Seq("-feature", "-unchecked", "-deprecation", "-diagrams", "-implicits", "-skip-packages", "samples")

  )
}

object Settings {

  import ScrupalPlugin.autoImport._

  val filter = { (ms: Seq[(File, String)]) =>
    ms filter {
      case (file, path) =>
        path != "logback.xml" && !path.startsWith("toignore") && !path.startsWith("samples")
    }
  }

  val buildSettings: Seq[sbt.Def.Setting[_]] = Defaults.coreDefaultSettings ++
    Seq(
      scalacOptions in(Compile, doc) ++= Opts.doc.title(scrupalTitle.value),
      scalacOptions in(Compile, doc) ++= Opts.doc.version(version.value),
      sourceDirectories in Compile := Seq(baseDirectory.value / "src"),
      sourceDirectories in Test := Seq(baseDirectory.value / "test"),
      unmanagedSourceDirectories in Compile := Seq(baseDirectory.value / "src"),
      unmanagedSourceDirectories in Test := Seq(baseDirectory.value / "test"),
      scalaSource in Compile := baseDirectory.value / "src",
      scalaSource in Test := baseDirectory.value / "test",
      javaSource in Compile := baseDirectory.value / "src",
      javaSource in Test := baseDirectory.value / "test",
      resourceDirectory in Compile := baseDirectory.value / "src/resources",
      resourceDirectory in Test := baseDirectory.value / "test/resources",
      fork in Test := false,
      parallelExecution in Test := false,
      logBuffered in Test := false,
      ivyScala := ivyScala.value map {_.copy(overrideScalaVersion = true)},
      shellPrompt := ShellPrompt.buildShellPrompt(version.value),
      mappings in(Compile, packageBin) ~= filter,
      mappings in(Compile, packageSrc) ~= filter,
      mappings in(Compile, packageDoc) ~= filter
    )
}

// Shell prompt which show the current project,
// git branch and build version
object ShellPrompt {

  object devnull extends ProcessLogger {
    def info(s: => String) {}

    def error(s: => String) {}

    def buffer[T](f: => T): T = f
  }

  def currBranch = (
    ("git status -sb" lines_! devnull headOption)
      getOrElse "-" stripPrefix "## ")

  def buildShellPrompt(version: String) = {
    (state: State) => {
      val currProject = Project.extract(state).currentProject.id
      "%s:%s:%s> ".format( currProject, currBranch, version )
    }
  }
}

trait PluginSettings {
  /** The [[sbt.Setting]]s to add in the scope of each project that activates this AutoPlugin. */
  def projectSettings: Seq[Setting[_]] = Nil

  /** The [[sbt.Setting]]s to add to the build scope for each project that activates this AutoPlugin.
    * The settings returned here are guaranteed to be added to a given build scope only once
    * regardless of how many projects for that build activate this AutoPlugin. */
  def buildSettings: Seq[Setting[_]] = Nil

  /** The [[sbt.Setting]]s to add to the global scope exactly once if any project activates this AutoPlugin. */
  def globalSettings: Seq[Setting[_]] = Nil
}

object SonatypePublishing extends PluginSettings {

  import xerial.sbt.Sonatype
  import ScrupalPlugin.autoImport._

  def targetRepository: Def.Initialize[Option[Resolver]] = Def.setting {
    val nexus = "https://oss.sonatype.org/"
    val snapshotsR = "snapshots" at nexus + "content/repositories/snapshots"
    val releasesR  = "releases"  at nexus + "service/local/staging/deploy/maven2"
    val resolver = if (isSnapshot.value) snapshotsR else releasesR
    Some(resolver)
  }

  override lazy val projectSettings = Sonatype.sonatypeSettings ++ Seq(
    Sonatype.SonatypeKeys.profileName := "org.scrupal",
    publishMavenStyle := true,
    publishArtifact in Test := false,
    pomIncludeRepository := { _ => false },
    licenses := Seq("Apache2" -> url("http://www.apache.org/licenses/LICENSE-2.0")),
    homepage := Some(new URL("http://scrupal.org/modules/" + normalizedName.value)),
    pomExtra :=
      <scm>
        <url>{scmInfo.value.get.browseUrl.toString}</url>
        <connection>{scmInfo.value.get.connection}</connection>
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

// import scala.language.postfixOps

object Scalariform extends PluginSettings {

  import com.typesafe.sbt.SbtScalariform._
  import scalariform.formatter.preferences.AlignSingleLineCaseStatements.MaxArrowIndent
  import scalariform.formatter.preferences._


  override lazy val projectSettings = scalariformSettings ++ Seq(
    ScalariformKeys.preferences := formattingPreferences
  )

  lazy val formattingPreferences = {
    FormattingPreferences().
      setPreference(AlignParameters, false).
      setPreference(AlignSingleLineCaseStatements, true).
      setPreference(CompactControlReadability, false).
      setPreference(CompactStringConcatenation, false).
      setPreference(DoubleIndentClassDeclaration, false).
      setPreference(FormatXml, true).
      setPreference(IndentLocalDefs, true).
      setPreference(IndentPackageBlocks, true).
      setPreference(IndentSpaces, 2).
      setPreference(IndentWithTabs, false).
      setPreference(MaxArrowIndent, 4).
      setPreference(MultilineScaladocCommentsStartOnFirstLine, true).
      setPreference(PlaceScaladocAsterisksBeneathSecondAsterisk, true).
      setPreference(PreserveSpaceBeforeArguments, true).
      setPreference(PreserveDanglingCloseParenthesis, true).
      setPreference(RewriteArrowSymbols, true).
      setPreference(SpaceBeforeColon, true).
      setPreference(SpaceInsideParentheses, false).
      setPreference(SpaceInsideBrackets, false).
      setPreference(SpacesWithinPatternBinders, true)
  }
}

object Unidoc extends PluginSettings {
  import sbtunidoc.{Plugin => UnidocPlugin}
  import ScrupalPlugin.autoImport._
  override lazy val projectSettings = UnidocPlugin.unidocSettings ++ Seq(
    scalacOptions in(Compile, doc) ++= Opts.doc.title(scrupalTitle.value),
    scalacOptions in(Compile, doc) ++= Opts.doc.version(version.value)
  )
}

object Site extends PluginSettings {
  import com.typesafe.sbt.SbtSite
  override lazy val projectSettings = SbtSite.settings
}

object GhPages {
  import com.typesafe.sbt.SbtGhPages

  lazy val buildSettings = SbtGhPages.buildSettings
  lazy val projectSettings = SbtGhPages.projectSettings
  lazy val globalSettings = SbtGhPages.globalSettings
}
