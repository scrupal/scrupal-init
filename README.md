# Scrupal SBT AutoPlugin

[![Join the chat at https://gitter.im/scrupal/scrupal-sbt](https://badges.gitter.im/Join%20Chat.svg)](https://gitter.im/scrupal/scrupal-sbt?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

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


The ScrupalPlugin also ensures that:
* You are using the correct JVM and Scala version for the version of Scrupal you use (matches ScrupalPlugin version)
* Scala compiler options are set to standard values for Scrupal
* SBT uses the standard Scrupal project layout for projects
* Your project's organization, name, version, title, and URLs are consistent across all tasks

## Resources

- [SBT-Plugin Best Practices](http://www.scala-sbt.org/0.13/docs/Plugins-Best-Practices.html)
- [Building Scala/SBT Projects with Travis CI](http://docs.travis-ci.com/user/languages/scala/)
- [SBT AutoPlugins Tutorial](http://mukis.de/pages/sbt-autoplugins-tutorial/) 
- [testing sbt plugins](http://eed3si9n.com/testing-sbt-plugins)
