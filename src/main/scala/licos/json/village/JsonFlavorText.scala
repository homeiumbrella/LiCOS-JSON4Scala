package licos.json.village

import java.util.{List => JList}

import licos.bson.village.{BsonBase, BsonChatFromServer, BsonFlavorText}
import org.bson.types.ObjectId
import play.api.libs.functional.syntax.{unlift, _}
import play.api.libs.json.{Format, JsPath, Json, OFormat}

import scala.collection.JavaConverters._

case class JsonFlavorText private (base: JsonBase, sub: JsonSubFlavorText) extends JsonElement {
  def this(base: JsonBase, flavorText: JList[JsonChatFromServer]) = {
    this(
      base: JsonBase,
      new JsonSubFlavorText(
        flavorText: JList[JsonChatFromServer]
      )
    )
  }
  override def toBson: BsonFlavorText = {
    new BsonFlavorText(
      new ObjectId(),
      base.toBson: BsonBase,
      sub.flavorText.map(_.toBson).asJava: JList[BsonChatFromServer]
    )
  }
}

object JsonFlavorText {
  implicit val jsonFormat: Format[JsonFlavorText] = (
    JsPath.format[JsonBase] and
      JsPath.format[JsonSubFlavorText]
  )(JsonFlavorText.apply, unlift(JsonFlavorText.unapply))
}

case class JsonSubFlavorText(flavorText: Seq[JsonChatFromServer]) {
  def this(flavorText: JList[JsonChatFromServer]) = {
    this(
      flavorText.asScala: Seq[JsonChatFromServer]
    )
  }
}

object JsonSubFlavorText {
  implicit val jsonFormat: OFormat[JsonSubFlavorText] = Json.format[JsonSubFlavorText]
}