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

package scrupal.sbt

import com.typesafe.sbt.SbtScalariform._
import scalariform.formatter.preferences.AlignSingleLineCaseStatements.MaxArrowIndent
import scalariform.formatter.preferences._

/** Settings For Scalariform */
object Scalariform extends PluginSettings {


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
