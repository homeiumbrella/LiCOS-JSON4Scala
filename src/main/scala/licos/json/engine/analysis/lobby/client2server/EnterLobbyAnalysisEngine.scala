package licos.json.engine.analysis.lobby.client2server

import licos.json.element.lobby.JsonEnterLobby
import licos.json.engine.BOX
import licos.json.engine.analysis.AnalysisEngine
import play.api.libs.json.JsValue

/** The analysis engine for entering a lobby.
  *
  * @author Kotaro Sakamoto
  */
trait EnterLobbyAnalysisEngine extends AnalysisEngine {

  /** Returns a play.api.libs.json.JsValue response from a JSON message.
    *
    * @param box a box.
    * @param enterLobby a JSON message.
    * @return a play.api.libs.json.JsValue option.
    */
  def process(box: BOX, enterLobby: JsonEnterLobby): Option[JsValue]
}
