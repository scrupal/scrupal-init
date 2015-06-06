package scrupal.sbt

import scala.language.postfixOps

import sbt._
import sbt.Keys._

import com.typesafe.sbt.{GitPlugin, JavaVersionCheckPlugin}
import com.typesafe.sbt.JavaVersionCheckPlugin.autoImport._
import com.typesafe.sbt.bundle.SbtBundle
import com.typesafe.sbt.packager.archetypes.JavaAppPackaging

import de.heikoseeberger.sbtheader.HeaderPlugin

import play.sbt.PlayScala

import sbtsh.ShPlugin

trait PluginSettings {
  /** The [[Configuration]]s to add to each project that activates this AutoPlugin.*/
  def projectConfigurations: Seq[Configuration] = Nil

  /** The [[Setting]]s to add in the scope of each project that activates this AutoPlugin. */
  def projectSettings: Seq[Setting[_]] = Nil

  /** The [[Setting]]s to add to the build scope for each project that activates this AutoPlugin.
    * The settings returned here are guaranteed to be added to a given build scope only once
    * regardless of how many projects for that build activate this AutoPlugin. */
  def buildSettings: Seq[Setting[_]] = Nil

  /** The [[Setting]]s to add to the global scope exactly once if any project activates this AutoPlugin. */
  def globalSettings: Seq[Setting[_]] = Nil
}

/** The ScrupalPlugin For Scrupal Based Modules */
object ScrupalPlugin extends AutoPlugin {

  val autoplugins : Seq[AutoPlugin] = Seq( PlayScala, JavaAppPackaging, JavaVersionCheckPlugin,
    GitPlugin, HeaderPlugin, SbtBundle, ShPlugin)

  override def requires = autoplugins.foldLeft(empty) { (b,plugin) => b && plugin }

  // Not AutoPlugins Yet: Scalariform, Unidoc, SbtSite, SbtGhPages, SbtUnidocPlugin :(

  /** Settings For Plugins that are not yet AutoPlugins so we can mimic them.
    * This trait provides the same settings methods as an AutoPlugin. See [[sbt.AutoPlugin]]
    * This is used to override settings in both AutoPlugins and regular Plugins.
    */
  val pluginSettings : Seq[PluginSettings] = Seq(
    Compiler, Settings, Bundle, Scalariform, Unidoc, SonatypePublishing, Site, GhPages
  )


  object Compiler extends PluginSettings {
    override def projectSettings : Seq[Setting[_]] = Seq(
      scalaVersion := "2.11.6",
      javaOptions in test ++= Seq("-Xmx512m"),
      scalacOptions ++= Seq("-feature", "-unchecked", "-deprecation", "-target:jvm-1.8"),
      scalacOptions in(Compile, doc) ++=
        Seq("-feature", "-unchecked", "-deprecation", "-diagrams", "-implicits", "-skip-packages", "samples")

    )
  }

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

    val printClasspath = TaskKey[File]("print-class-path", "Print the project's class path line by line.")
    val printRuntimeClasspath = TaskKey[File]("print-runtime-class-path", "Print the project's runtime class path.")
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
  override def projectSettings: Seq[Setting[_]] = Defaults.coreDefaultSettings ++
    autoplugins.foldLeft(Seq.empty[Setting[_]]) { (s,p) => s ++ p.projectSettings } ++
    pluginSettings.foldLeft(Seq.empty[Setting[_]]) { (s,p) => s ++ p.projectSettings } ++
    Seq(
      resolvers := scrupalResolvers,
      javaVersionPrefix in javaVersionCheck := Some("1.8"),
      ivyScala  := ivyScala.value map { _.copy(overrideScalaVersion = true) },
      printClasspath <<= Commands.print_class_path,
      printRuntimeClasspath <<= Commands.print_runtime_class_path,
      compileOnly <<= Commands.compile_only
    )

  override def buildSettings : Seq[Setting[_]] = Defaults.buildCore ++
    Seq(
      baseDirectory := thisProject.value.base,
      target := baseDirectory.value / "target"
    ) ++
    autoplugins.foldLeft(Seq.empty[Setting[_]]) { (s,p) => s ++ p.buildSettings } ++
    pluginSettings.foldLeft(Seq.empty[Setting[_]]) { (s,p) => s ++ p.buildSettings } ++
    Seq()

  override def globalSettings: Seq[Setting[_]] =
    autoplugins.foldLeft(Seq.empty[Setting[_]]) { (s,p) => s ++ p.globalSettings } ++
    pluginSettings.foldLeft(Seq.empty[Setting[_]]) { (s,p) => s ++ p.globalSettings } ++
    Seq(
    )
}

object Commands {
  addCommandAlias("tq", "test-quick")
  addCommandAlias("to", "test-only")

  def print_class_path = (target, fullClasspath in Compile, compile in Compile) map { (out, cp, analysis) =>
    println("----- " + out.getCanonicalPath + ": FILES:")
    println(cp.files.map(_.getCanonicalPath).mkString("\n"))
    println("----- " + out.getCanonicalPath + ": All Binary Dependencies:")
    println(analysis.relations.allBinaryDeps.toSeq.mkString("\n"))
    println("----- END")
    out
  }

  def print_runtime_class_path = (target, fullClasspath in Runtime).map { (out, cp) =>
    println("----- " + out.getCanonicalPath + ": FILES:")
    println(cp.files.map(_.getCanonicalPath).mkString("\n"))
    println("----- END")
    out
  }

  def compile_only = (target, compile in Compile) map { (out, compile) =>
    println("Not Implemented Yet.")
    out
  }

}

object Settings extends PluginSettings {

  import ScrupalPlugin.autoImport._

  val filter = { (ms: Seq[(File, String)]) =>
    ms filter {
      case (file, path) =>
        path != "logback.xml" && !path.startsWith("toignore") && !path.startsWith("samples")
    }
  }

  override def projectSettings : Seq[sbt.Def.Setting[_]] = Defaults.coreDefaultSettings ++
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

object Bundle extends PluginSettings {

  import SbtBundle.autoImport.BundleKeys._
  import com.typesafe.sbt.bundle.Import.ByteConversions.IntOps

  override def projectSettings : Seq[Setting[_]] = Seq(
    memory := IntOps(1).GiB,
    diskSpace := IntOps(1).GiB,
    nrOfCpus := 2.0
  )
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


object Scalariform extends PluginSettings {

  import com.typesafe.sbt.SbtScalariform._
  import scalariform.formatter.preferences.AlignSingleLineCaseStatements.MaxArrowIndent
  import scalariform.formatter.preferences._


  override def projectSettings = scalariformSettings ++ Seq(
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
  override def projectSettings = UnidocPlugin.unidocSettings ++ Seq(
    scalacOptions in (Compile, doc) ++= Seq("-unchecked", "-deprecation", "-implicits"),
    scalacOptions in(Compile, doc) ++= Opts.doc.title(scrupalTitle.value),
    scalacOptions in(Compile, doc) ++= Opts.doc.version(version.value),
    apiURL := Some(url("http://scrupal.org/modules/" + normalizedName.value + "/api/")),
    autoAPIMappings := true,
    apiMappings ++= {
      val cp: Seq[Attributed[File]] = (fullClasspath in Compile).value
      def findManagedDependency(organization: String, name: String): File = {
        ( for {
          entry <- cp
          module <- entry.get(moduleID.key)
          if module.organization == organization
          if module.name.startsWith(name)
          jarFile = entry.data
        } yield jarFile
          ).head
      }
      Map(
        findManagedDependency("org.scala-lang", "scala-library") → url(s"http://www.scala-lang.org/api/$scalaVersion/"),
        findManagedDependency("com.typesafe.akka", "akka-actor") → url(s"http://doc.akka.io/api/akka/"),
        findManagedDependency("com.typesafe", "config") → url("http://typesafehub.github.io/config/latest/api/"),
        findManagedDependency("joda-time", "joda-time") → url("http://joda-time.sourceforge.net/apidocs/")
      )
    }
  )
}

object Site extends PluginSettings {
  import com.typesafe.sbt.SbtSite
  override def projectSettings = SbtSite.settings
}

object GhPages extends PluginSettings {
  import com.typesafe.sbt.SbtGhPages

  override def buildSettings = SbtGhPages.buildSettings
  override def projectSettings = SbtGhPages.projectSettings
  override def globalSettings = SbtGhPages.globalSettings
}
