package licos.json.element.lobby

import licos.json.validation.lobby.LobbyValidation
import licos.json.validation.village.{AvatarValidation, VillageValidation}

/**
  * <pre>
  * Created on 2018/01/04.
  * </pre>
  *
  * @author K.Sakamoto
  */
final case class JsonLeaveWaitingPage(`type`: String, token: String, villageId: Long, lobby: String)
    extends TypeSystem(`type`) {
  override protected def validType: String = JsonLeaveWaitingPage.`type`
}

object JsonLeaveWaitingPage {

  val `type`: String = "leaveWaitingPage"

  import play.api.libs.json._
  import play.api.libs.json.Reads.pattern
  import play.api.libs.functional.syntax._

  implicit val jsonReads: Reads[JsonLeaveWaitingPage] = (
    (JsPath \ "type").read[String](pattern(`type`.r)) and
      (JsPath \ "token").read[String](AvatarValidation.token) and
      (JsPath \ "villageId").read[Long](VillageValidation.id) and
      (JsPath \ "lobby").read[String](LobbyValidation.lobby)
  )(JsonLeaveWaitingPage.apply _)

  implicit val jsonWrites: OWrites[JsonLeaveWaitingPage] = Json.writes[JsonLeaveWaitingPage]

}
