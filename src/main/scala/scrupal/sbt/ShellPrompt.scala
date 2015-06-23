package scrupal.sbt

import sbt._

import scala.language.postfixOps

/** Title Of Thing.
  *
  * Description of thing
  */
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
      "%s : %s : %s> ".format( currProject, currBranch, version )
    }
  }
}
