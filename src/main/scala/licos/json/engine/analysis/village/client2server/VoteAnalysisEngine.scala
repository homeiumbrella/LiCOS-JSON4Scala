package licos.json.engine.analysis.village.client2server

import licos.json.engine.BOX
import licos.json.engine.analysis.AnalysisEngine
import licos.json.element.village.JsonVote
import play.api.libs.json.JsValue

/** The analysis engine for a vote.
  *
  * @author Kotaro Sakamoto
  */
trait VoteAnalysisEngine extends AnalysisEngine {

  /** Returns a play.api.libs.json.JsValue response from a JSON message.
    *
    * @param box a box.
    * @param vote a JSON message.
    * @return a play.api.libs.json.JsValue option.
    */
  def process(box: BOX, vote: JsonVote): Option[JsValue]
}