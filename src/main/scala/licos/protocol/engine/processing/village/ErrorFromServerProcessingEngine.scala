package licos.protocol.engine.processing.village

import java.time.OffsetDateTime
import java.util.UUID

import licos.entity.{PhaseException, Village, VillageCreationException, VillageFactory}
import licos.json.element.village.JsonError
import licos.knowledge.{Data2Knowledge, Phase}
import licos.protocol.element.village.VillageMessageProtocol
import licos.protocol.element.village.part.VillageProtocol
import licos.protocol.element.village.server2client.ErrorFromServerProtocol
import licos.protocol.engine.analysis.village.server2client.ErrorFromServerAnalysisEngine
import licos.protocol.engine.processing.{JSON2ProtocolException, VillageBOX}

import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

object ErrorFromServerProcessingEngine {

  @SuppressWarnings(Array[String]("org.wartremover.warts.Nothing"))
  def process(
      box:             VillageBOX,
      villageFactory:  VillageFactory,
      villageProtocol: VillageProtocol,
      engine:          ErrorFromServerAnalysisEngine,
      json:            JsonError
  ): Try[VillageMessageProtocol] = {
    Data2Knowledge.phaseOpt(json.base.phase) match {
      case Some(phase: Phase) =>
        villageFactory
          .setVillageProtocol(villageProtocol)
          .setToken(UUID.fromString(json.base.token))
          .setDay(json.base.day)
          .setPhaseStartTime(OffsetDateTime.parse(json.base.phaseStartTime))
          .setPhaseTimeLimit(json.base.phaseTimeLimit.seconds)
          .setPhase(phase)
          .create match {
          case Success(village: Village) =>
            ErrorFromServerProtocol.read(json, village) match {
              case Some(protocol) =>
                engine.process(box, protocol)
              case None => Failure(new JSON2ProtocolException(ErrorFromServerAnalysisEngine.name))
            }
          case Failure(_: Throwable) => Failure(new VillageCreationException(ErrorFromServerAnalysisEngine.name))
        }
      case None => Failure(new PhaseException(ErrorFromServerAnalysisEngine.name))
    }
  }
}
