import scrupal.sbt.ScrupalProjectPlugin.autoImport._

name := "hello-test"

version := "0.1"

scalaVersion := "2.11.6"

enablePlugins(ScrupalProjectPlugin)

codePackage := "scrupal.sbt"

copyrightHolder := "Reactific Software LLC"

copyrightYears := Seq(2014,2015)

developerUrl := new URL("https://github.com/reid-spencer")

titleForDocs := "Scrupal SBT Test For Projects"
