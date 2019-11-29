package licos.protocol.element.village.server2client

import licos.entity.Village
import licos.json.element.village.server2client.JsonChatFromServer
import licos.knowledge.Data2Knowledge
import licos.protocol.PlayerChatChannel
import licos.protocol.element.village.part.character.SimpleCharacterProtocol
import play.api.libs.json.{JsValue, Json}

final case class ChatFromServerProtocol(
    village:   Village,
    channel:   PlayerChatChannel,
    character: SimpleCharacterProtocol,
    isMine:    Boolean,
    id:        Int,
    counter:   Int,
    interval:  Int,
    text:      String,
    isOver:    Boolean
) extends Server2ClientVillageMessageProtocol {

  val json: Option[JsonChatFromServer] = {
    server2logger
      .ChatFromServerProtocol(village, channel, character, isMine, id, counter, interval, text, isOver, Nil)
      .json
  }

  override def toJsonOpt: Option[JsValue] = {
    json map { j: JsonChatFromServer =>
      Json.toJson(j)
    }
  }

}

object ChatFromServerProtocol {

  @SuppressWarnings(Array[String]("org.wartremover.warts.OptionPartial"))
  def read(json: JsonChatFromServer, village: Village): Option[ChatFromServerProtocol] = {

    val channelOpt: Option[PlayerChatChannel] =
      Data2Knowledge.playerChatChannelOpt(json.base.intensionalDisclosureRange)

    if (channelOpt.nonEmpty) {
      Some(
        ChatFromServerProtocol(
          village,
          channelOpt.get,
          SimpleCharacterProtocol(
            village.myCharacter,
            village.id,
            village.language
          ),
          json.isMine,
          json.id,
          json.counter,
          json.interval,
          json.text.`@value`,
          json.isOver
        )
      )
    } else {
      None
    }
  }

}
