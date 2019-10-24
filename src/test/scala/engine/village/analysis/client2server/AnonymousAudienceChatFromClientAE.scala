package engine.village.analysis.client2server

import element.JsonTest
import engine.village.VillageBox
import engine.village.example.client2server.AnonymousAudienceChat
import licos.json.element.village.JsonAnonymousAudienceChat
import licos.json.engine.BOX
import licos.json.engine.analysis.village.client2server
import play.api.libs.json.{JsValue, Json}

class AnonymousAudienceChatFromClientAE extends client2server.AnonymousAudienceChatAnalysisEngine {
  override def process(box: BOX, anonymousAudienceChat: JsonAnonymousAudienceChat): Option[JsValue] = {
    box match {
      case _: VillageBox => Option(Json.toJson(JsonTest(AnonymousAudienceChat.`type`)))
      case _ => None
    }
  }
}