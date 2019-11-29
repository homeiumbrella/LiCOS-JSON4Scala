package licos.protocol.element.village.client2server.server2logger

import java.time.OffsetDateTime

import licos.entity.Village
import licos.json.element.village.character.JsonStatusCharacter
import licos.json.element.village.client2server.JsonStar
import licos.json.element.village.iri.{Contexts, StarMessage}
import licos.knowledge.{Character, ClientToServer, Data2Knowledge, PrivateChannel, Role, Status}
import licos.protocol.element.village.part.character.{RoleCharacterProtocol, StatusCharacterProtocol}
import licos.protocol.element.village.part.{BaseProtocol, ChatSettingsProtocol, StarInfoProtocol, VillageProtocol}
import licos.util.TimestampGenerator
import play.api.libs.json.{JsValue, Json}

import scala.collection.mutable.ListBuffer

@SuppressWarnings(Array[String]("org.wartremover.warts.OptionPartial"))
final case class StarProtocol(
    village:                    Village,
    serverTimestamp:            OffsetDateTime,
    clientTimestamp:            OffsetDateTime,
    isMarked:                   Boolean,
    extensionalDisclosureRange: Seq[StatusCharacterProtocol]
) extends Client2ServerVillageMessageProtocolForLogging {

  val json: Option[JsonStar] = {
    if (village.isAvailable) {
      Some(
        new JsonStar(
          BaseProtocol(
            Contexts.get(StarMessage),
            StarMessage,
            VillageProtocol(
              village.id,
              village.name,
              village.cast.totalNumberOfPlayers,
              village.language,
              ChatSettingsProtocol(
                village.id,
                village.maxNumberOfChatMessages,
                village.maxLengthOfUnicodeCodePoints
              )
            ),
            village.token,
            village.phase,
            village.day,
            village.phaseTimeLimit,
            village.phaseStartTime,
            None,
            Option(TimestampGenerator.now),
            ClientToServer,
            PrivateChannel,
            extensionalDisclosureRange,
            None,
            None
          ).json,
          RoleCharacterProtocol(
            village.myCharacter,
            village.myRole,
            village.id,
            village.language
          ).json,
          StarInfoProtocol(
            village.id,
            village.token,
            serverTimestamp,
            clientTimestamp,
            isMarked
          ).json
        )
      )
    } else {
      None
    }
  }

  override def toJsonOpt: Option[JsValue] = {
    json map { j: JsonStar =>
      Json.toJson(j)
    }
  }

}

object StarProtocol {

  @SuppressWarnings(
    Array[String](
      "org.wartremover.warts.Any",
      "org.wartremover.warts.MutableDataStructures",
      "org.wartremover.warts.OptionPartial"
    )
  )
  def read(json: JsonStar, village: Village): Option[StarProtocol] = {

    val statusCharacterBuffer = ListBuffer.empty[StatusCharacterProtocol]
    json.base.extensionalDisclosureRange foreach { jsonStatusCharacter: JsonStatusCharacter =>
      val characterOpt: Option[Character] =
        Data2Knowledge.characterOpt(jsonStatusCharacter.name.en, jsonStatusCharacter.id)
      val roleOpt:   Option[Role]   = village.cast.parse(jsonStatusCharacter.role.name.en)
      val statusOpt: Option[Status] = Data2Knowledge.statusOpt(jsonStatusCharacter.status)
      if (characterOpt.nonEmpty && roleOpt.nonEmpty && statusOpt.nonEmpty) {
        statusCharacterBuffer += StatusCharacterProtocol(
          characterOpt.get,
          roleOpt.get,
          statusOpt.get,
          jsonStatusCharacter.isHumanPlayer,
          village.id,
          village.language
        )
      }
    }

    Some(
      StarProtocol(
        village,
        OffsetDateTime.parse(json.star.serverTimestamp),
        OffsetDateTime.parse(json.star.clientTimestamp),
        json.star.isMarked,
        statusCharacterBuffer.result
      )
    )
  }

}
