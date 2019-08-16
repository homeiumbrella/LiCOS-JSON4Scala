package licos.json.engine.analysis.lobby.server2client

import licos.json.element.lobby.JsonLobby
import licos.json.engine.BOX
import licos.json.engine.analysis.AnalysisEngine
import play.api.libs.json.JsValue

/** The analysis engine for a lobby.
  *
  * @author Kotaro Sakamoto
  */
trait LobbyAnalysisEngine extends AnalysisEngine {

  /** Returns a play.api.libs.json.JsValue response from a JSON message.
    *
    * @param box a box.
    * @param lobby a JSON message.
    * @return a play.api.libs.json.JsValue option.
    */
  def process(box: BOX, lobby: JsonLobby): Option[JsValue]
}
