package licos.json.engine.analysis.lobby.client2server

import licos.json.element.lobby.JsonGetAvatarInfo
import licos.json.engine.BOX
import licos.json.engine.analysis.AnalysisEngine
import play.api.libs.json.JsValue

/** The analysis engine for getting an avatar information.
  *
  * @author Kotaro Sakamoto
  */
trait GetAvatarInfoAnalysisEngine extends AnalysisEngine {

  /** Returns a play.api.libs.json.JsValue response from a JSON message.
    *
    * @param box a box.
    * @param getAvatarInfo a JSON message.
    * @return a play.api.libs.json.JsValue option.
    */
  def process(box: BOX, getAvatarInfo: JsonGetAvatarInfo): Option[JsValue]
}