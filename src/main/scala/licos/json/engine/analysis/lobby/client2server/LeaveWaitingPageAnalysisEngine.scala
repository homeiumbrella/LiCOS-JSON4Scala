package licos.json.engine.analysis.lobby.client2server

import licos.json.element.lobby.JsonLeaveWaitingPage
import licos.json.engine.BOX
import licos.json.engine.analysis.AnalysisEngine
import play.api.libs.json.JsValue

/** The analysis engine for leaving a waiting page.
  *
  * @author Kotaro Sakamoto
  */
trait LeaveWaitingPageAnalysisEngine extends AnalysisEngine {

  /** Returns a play.api.libs.json.JsValue response from a JSON message.
    *
    * @param box a box.
    * @param leaveWaitingPage a JSON message.
    * @return a play.api.libs.json.JsValue option.
    */
  def process(box: BOX, leaveWaitingPage: JsonLeaveWaitingPage): Option[JsValue]
}
