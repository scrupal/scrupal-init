package scrupal.sbt

import sbt._
import sbt.Keys._

/** Commands Added To The Build */
object Commands {

  def aliases : Seq[Def.Setting[(State => State)]] = {
    Seq(
      addCommandAlias("tq", "test-quick"),
      addCommandAlias("to", "test-only"),
      addCommandAlias("cq", "compile-quick")
    ).flatten
  }

  def print_class_path = (target, fullClasspath in Compile, compile in Compile) map { (out, cp, analysis) =>
    println("----- Compile: " + out.getCanonicalPath + ": FILES:")
    println(cp.files.map(_.getCanonicalPath).mkString("\n"))
    println("----- " + out.getCanonicalPath + ": All Binary Dependencies:")
    println(analysis.relations.allBinaryDeps.toSeq.mkString("\n"))
    println("----- END")
    out
  }

  def print_test_class_path = (target, fullClasspath in Test).map { (out, cp) =>
    println("----- Test: " + out.getCanonicalPath + ": FILES:")
    println(cp.files.map(_.getCanonicalPath).mkString("\n"))
    println("----- END")
    out
  }
  def print_runtime_class_path = (target, fullClasspath in Runtime).map { (out, cp) =>
    println("----- Runtime: " + out.getCanonicalPath + ": FILES:")
    println(cp.files.map(_.getCanonicalPath).mkString("\n"))
    println("----- END")
    out
  }

  def compile_only = (target, compile in Compile) map { (out, compile) =>
    println("Not Implemented Yet.")
    out
  }

}
