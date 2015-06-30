# Scrupal SBT AutoPlugin

[![Build Status](https://travis-ci.org/scrupal/scrupal-sbt.svg)](https://travis-ci.org/scrupal/scrupal-sbt)[![Join the chat at https://gitter.im/scrupal/scrupal-sbt](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/scrupal/scrupal-sbt?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

This [sbt](https://github.com/sbt/sbt) plugin provides all the boilerplate SBT goodness needed for developing with
Scrupal. The plugin includes all of the following SBT plugins and default configurations for them

* [sbt-plugin](https://github.com/playframework/playframework/tree/master/framework/src/sbt-plugin): Play plugin for sbt (includes sbt-web)
* [sbt-bundle](https://github.com/sbt/sbt-bundle): Bundling for Typesafe ConductR
* [sbt-javaversioncheck](https://github.com/sbt/sbt-javaversioncheck): Make sure the right version of JVM is used.
* [sbt-scalariform](https://github.com/sbt/sbt-scalariform): Ensures Scala code style uniformity.
* [sbt-sonatype](https://github.com/sbt/sbt-sonatype): Allows easy publishing to the sonatype repository.
* [sbt-header](https://github.com/sbt/sbt-header): Keep copyright headers up to date. 
* [sbt-unidoc](https://github.com/sbt/sbt-unidoc): Generation of ScalaDoc and JavaDoc into integrated web pages
* [sbt-ghpages](https://github.com/sbt/sbt-ghpages): Generate GitHub pages
* [sbt-site](https://github.com/sbt/sbt-site): Build HTML Web Site from sources, for documentation 
* [sbt-git](https://github.com/sbt/sbt-git): Command line utilities for git from sbt
* [sbt-pgp](https://github.com/sbt/sbt-pgp): PGP signing for your module
* [sbt-buildinfo](https://github.com/sbt/sbt-buildinfo): Incorporation of build info into your project (not enabled)
* [sbt-release](https://github.com/sbt/sbt-release): A release command for better/easier releasing of your project
* [sbt-license-report](https://github.com/sbt/sbt-license-report): Report on open source licenses used.

The ScrupalPlugin also ensures that:
* You are using the correct JVM (8) and Scala (2.11) version for the version of Scrupal you use (matches ScrupalPlugin version)
* Scala compiler options are set to standard values for Scrupal
* SBT uses the standard Scrupal project layout for your project (rather than the Play structure)
* Your project's organization, name, version, title, and URLs are consistent across all tasks
* The prompt is set to <name>:<branch>...<remote_branch>:<version> so you always know where you are
* You have aliases for often used things: tq=test-quick, to=test-only, copmile=compile
* 

## Usage

To use this plugin, you need to include this line in your `plugins.sbt` file in your `project` directory:
```scala
addSbtPlugin("org.scrupal" % "scrupal-sbt" % "0.1.0") // For Latest Released Version
```
or for the latest snapshot version, use this line:
```scala
addSbtPlugin("org.scrupal" % "scrupal-sbt" % "0.2.0-SNAPSHOT") // For Latest Development Version
```

Your project's definition also needs to do a few things to get all this goodness. You can override everything that
scrupal-sbt does, but there are a few things you must do. At a minimum, your project should look like this:
```
  lazy val example_proj = Project("example", file("./example"))
    .enablePlugins(ScrupalPlugin)
    .disablePlugins(PlayLayoutPlugin)
    .settings(
      scrupalTitle := "Example Project",
      scrupalPackage := "example"
      scrupalTitle  := "Title For Your Plugin" //A title for the Scrupal module for use in documentation
      scrupalCopyrightHolder :=  "Reactific Software LLC"  // The name of the copyright holder for the scrupal module
      scrupalCopyrightYears := Seq(2014,2015) // The years in which the copyright was in place
      scrupalDeveloprUrl := Url("https://github.com/reid-spencer") // The URL for the developer's home page
    )
```
The above assumes a `project/Build.scala` style build specification. But, if you're using `build.sbt` the important 
thing to remember is to make all the "scrupal..." settings and to enable the ScrupalPlugin.

That's it. We hope using scrupal-sbt helps make writing your project easier.

## Resources

- [SBT-Plugin Best Practices](http://www.scala-sbt.org/0.13/docs/Plugins-Best-Practices.html)
- [Building Scala/SBT Projects with Travis CI](http://docs.travis-ci.com/user/languages/scala/)
- [SBT AutoPlugins Tutorial](http://mukis.de/pages/sbt-autoplugins-tutorial/) 
- [testing sbt plugins](http://eed3si9n.com/testing-sbt-plugins)
