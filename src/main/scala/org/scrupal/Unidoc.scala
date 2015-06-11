package org.scrupal

import sbt._
import sbt.Keys._
import scrupal.sbt.ScrupalPlugin
import ScrupalPlugin.autoImport._

/** Title Of Thing.
  *
  * Description of thing
  */
object Unidoc extends PluginSettings {
  import sbtunidoc.{Plugin => UnidocPlugin}
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
