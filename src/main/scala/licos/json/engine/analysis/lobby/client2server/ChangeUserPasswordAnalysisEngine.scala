package licos.json.engine.analysis.lobby.client2server

import licos.json.element.lobby.JsonChangeUserPassword
import licos.json.engine.BOX
import licos.json.engine.analysis.AnalysisEngine
import play.api.libs.json.JsValue

/** The analysis engine for changing a user password.
  *
  * @author Kotaro Sakamoto
  */
trait ChangeUserPasswordAnalysisEngine extends AnalysisEngine {

  /** Returns a play.api.libs.json.JsValue response from a JSON message.
    *
    * @param box a box.
    * @param changeUserPassword a JSON message.
    * @return a play.api.libs.json.JsValue option.
    */
  def process(box: BOX, changeUserPassword: JsonChangeUserPassword): Option[JsValue]
}