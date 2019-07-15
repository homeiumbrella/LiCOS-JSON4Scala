package licos.json.lobby

import play.api.libs.json.{Json, OFormat}

case class JsonChangeUserName(`type`: String,
                              userName: String) extends TypeSystem(`type`) {
  override protected def validType: String = JsonChangeUserName.`type`
}

object JsonChangeUserName {
  implicit val jsonFormat: OFormat[JsonChangeUserName] = Json.format[JsonChangeUserName]

  val `type`: String = "changeUserName"
}