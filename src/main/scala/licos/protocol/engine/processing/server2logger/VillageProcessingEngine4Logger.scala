package licos.protocol.engine.processing.server2logger

import java.util.Locale

import com.typesafe.scalalogging.Logger
import licos.entity.VillageFactory
import licos.json.element.village.client2server.{
  JsonBoard,
  JsonChatFromClient,
  JsonOnymousAudienceBoard,
  JsonOnymousAudienceScroll,
  JsonScroll,
  JsonStar,
  JsonVote
}
import licos.json.element.village.server2client.{JsonChatFromServer, JsonFlavorText, JsonGameResult, JsonPhase}
import licos.json.element.village.{JsonAnonymousAudienceChat, JsonError, JsonOnymousAudienceChat, JsonVillage}
import licos.json.flow.{FlowController, VillageFlowController}
import licos.knowledge.{Morning, Night, Noon}
import licos.protocol.element.village.VillageMessageProtocol
import licos.protocol.element.village.part.{ChatSettingsProtocol, VillageProtocol}
import licos.protocol.engine.analysis.village.client2server.server2logger.{
  AnonymousAudienceChatFromClientAnalysisEngine,
  BoardAnalysisEngine,
  ChatFromClientAnalysisEngine,
  ErrorFromClientAnalysisEngine,
  OnymousAudienceBoardAnalysisEngine,
  OnymousAudienceChatFromClientAnalysisEngine,
  OnymousAudienceScrollAnalysisEngine,
  ScrollAnalysisEngine,
  StarAnalysisEngine,
  VoteAnalysisEngine
}
import licos.protocol.engine.analysis.village.server2client.server2logger.{
  AnonymousAudienceChatFromServerAnalysisEngine,
  ChatFromServerAnalysisEngine,
  ErrorFromServerAnalysisEngine,
  FirstMorningPhaseAnalysisEngine,
  FlavorTextAnalysisEngine,
  GameResultAnalysisEngine,
  MorningPhaseAnalysisEngine,
  NightPhaseAnalysisEngine,
  NoonPhaseAnalysisEngine,
  OnymousAudienceChatFromServerAnalysisEngine
}
import licos.protocol.engine.processing.village.server2logger.{
  AnonymousAudienceChatFromClientProcessingEngine,
  AnonymousAudienceChatFromServerProcessingEngine,
  BoardProcessingEngine,
  ChatFromClientProcessingEngine,
  ChatFromServerProcessingEngine,
  ErrorFromClientProcessingEngine,
  ErrorFromServerProcessingEngine,
  FirstMorningPhaseProcessingEngine,
  FlavorTextProcessingEngine,
  GameResultProcessingEngine,
  MorningPhaseProcessingEngine,
  NightPhaseProcessingEngine,
  NoonPhaseProcessingEngine,
  OnymousAudienceBoardProcessingEngine,
  OnymousAudienceChatFromClientProcessingEngine,
  OnymousAudienceChatFromServerProcessingEngine,
  OnymousAudienceScrollProcessingEngine,
  ScrollProcessingEngine,
  StarProcessingEngine,
  VoteProcessingEngine
}
import licos.protocol.engine.processing.{NoEngineException, ProcessingEngine, VillageBOX}
import play.api.libs.json.{JsValue, Json}

import scala.util.{Failure, Try}

class VillageProcessingEngine4Logger(
    anonymousAudienceChatFromClientAnalysisEngine: Option[AnonymousAudienceChatFromClientAnalysisEngine],
    boardAnalysisEngine:                           Option[BoardAnalysisEngine],
    chatFromClientAnalysisEngine:                  Option[ChatFromClientAnalysisEngine],
    errorFromClientAnalysisEngine:                 Option[ErrorFromClientAnalysisEngine],
    onymousAudienceBoardAnalysisEngine:            Option[OnymousAudienceBoardAnalysisEngine],
    onymousAudienceChatFromClientAnalysisEngine:   Option[OnymousAudienceChatFromClientAnalysisEngine],
    onymousAudienceScrollAnalysisEngine:           Option[OnymousAudienceScrollAnalysisEngine],
    scrollAnalysisEngine:                          Option[ScrollAnalysisEngine],
    starAnalysisEngine:                            Option[StarAnalysisEngine],
    voteAnalysisEngine:                            Option[VoteAnalysisEngine],
    anonymousAudienceChatFromServerAnalysisEngine: Option[AnonymousAudienceChatFromServerAnalysisEngine],
    chatFromServerAnalysisEngine:                  Option[ChatFromServerAnalysisEngine],
    errorFromServerAnalysisEngine:                 Option[ErrorFromServerAnalysisEngine],
    firstMorningPhaseAnalysisEngine:               Option[FirstMorningPhaseAnalysisEngine],
    flavorTextAnalysisEngine:                      Option[FlavorTextAnalysisEngine],
    gameResultAnalysisEngine:                      Option[GameResultAnalysisEngine],
    morningPhaseAnalysisEngine:                    Option[MorningPhaseAnalysisEngine],
    nightPhaseAnalysisEngine:                      Option[NightPhaseAnalysisEngine],
    noonPhaseAnalysisEngine:                       Option[NoonPhaseAnalysisEngine],
    onymousAudienceChatFromServerAnalysisEngine:   Option[OnymousAudienceChatFromServerAnalysisEngine]
) extends ProcessingEngine {

  override protected val flowController: FlowController = new VillageFlowController()

  private final val logger: Logger = Logger[VillageProcessingEngine4Logger]

  @SuppressWarnings(Array[String]("org.wartremover.warts.Nothing"))
  def process(box: VillageBOX, msg: String): Try[VillageMessageProtocol] = {

    val villageFactory = VillageFactory(box.villageInfoFromLobby)

    val jsValue: JsValue = Json.parse(msg)

    def log(label: String): Unit = {
      val format: String = "process %s"
      logger.info(format.format(label))
    }

    def jsonVillageToProtocol(json: JsonVillage): VillageProtocol = {
      VillageProtocol(
        json.id,
        json.name,
        json.totalNumberOfPlayers,
        new Locale(json.language),
        ChatSettingsProtocol(
          json.id,
          json.chatSettings.maxNumberOfChatMessages,
          json.chatSettings.maxLengthOfUnicodeCodePoints
        )
      )
    }

    flowController.flow(jsValue) match {
      case Right(json: JsonAnonymousAudienceChat) =>
        if (json.isFromServer) {
          log("JsonAnonymousAudienceChatFromServer")
          anonymousAudienceChatFromServerAnalysisEngine match {
            case Some(engine: AnonymousAudienceChatFromServerAnalysisEngine) =>
              log("AnonymousAudienceChatFromServerAnalysisEngine")
              AnonymousAudienceChatFromServerProcessingEngine
                .process(box, villageFactory, jsonVillageToProtocol(json.base.village), engine, json)
            case None => Failure(new NoEngineException(AnonymousAudienceChatFromServerAnalysisEngine.name))

          }
        } else {
          log("JsonAnonymousAudienceChatFromClient")
          anonymousAudienceChatFromClientAnalysisEngine match {
            case Some(engine: AnonymousAudienceChatFromClientAnalysisEngine) =>
              log("AnonymousAudienceChatFromClientAnalysisEngine")
              AnonymousAudienceChatFromClientProcessingEngine
                .process(box, villageFactory, jsonVillageToProtocol(json.base.village), engine, json)
            case None => Failure(new NoEngineException(AnonymousAudienceChatFromClientAnalysisEngine.name))
          }
        }
      case Right(json: JsonBoard) =>
        log("JsonBoard")
        boardAnalysisEngine match {
          case Some(engine: BoardAnalysisEngine) =>
            log("BoardAnalysisEngine")
            BoardProcessingEngine.process(box, villageFactory, jsonVillageToProtocol(json.base.village), engine, json)
          case None => Failure(new NoEngineException(BoardAnalysisEngine.name))
        }
      case Right(json: JsonChatFromClient) =>
        log("JsonChatFromClient")
        chatFromClientAnalysisEngine match {
          case Some(engine: ChatFromClientAnalysisEngine) =>
            log("ChatFromClientAnalysisEngine")
            ChatFromClientProcessingEngine
              .process(box, villageFactory, jsonVillageToProtocol(json.base.village), engine, json)
          case None => Failure(new NoEngineException(ChatFromClientAnalysisEngine.name))
        }
      case Right(json: JsonError) =>
        if (json.isFromServer) {
          log("JsonErrorFromServer")
          errorFromServerAnalysisEngine match {
            case Some(engine: ErrorFromServerAnalysisEngine) =>
              log("ErrorFromServerAnalysisEngine")
              ErrorFromServerProcessingEngine
                .process(box, villageFactory, jsonVillageToProtocol(json.base.village), engine, json)
            case None => Failure(new NoEngineException(ErrorFromServerAnalysisEngine.name))
          }
        } else {
          log("JsonErrorFromClient")
          errorFromClientAnalysisEngine match {
            case Some(engine: ErrorFromClientAnalysisEngine) =>
              log("ErrorFromClientAnalysisEngine")
              ErrorFromClientProcessingEngine
                .process(box, villageFactory, jsonVillageToProtocol(json.base.village), engine, json)
            case None => Failure(new NoEngineException(ErrorFromClientAnalysisEngine.name))
          }
        }
      case Right(json: JsonOnymousAudienceBoard) =>
        log("JsonOnymousAudienceBoard")
        onymousAudienceBoardAnalysisEngine match {
          case Some(engine: OnymousAudienceBoardAnalysisEngine) =>
            log("OnymousAudienceBoardAnalysisEngine")
            OnymousAudienceBoardProcessingEngine
              .process(box, villageFactory, jsonVillageToProtocol(json.base.village), engine, json)
          case None => Failure(new NoEngineException(OnymousAudienceBoardAnalysisEngine.name))
        }
      case Right(json: JsonOnymousAudienceChat) =>
        if (json.isFromServer) {
          log("JsonOnymousAudienceChatFromServer")
          onymousAudienceChatFromServerAnalysisEngine match {
            case Some(engine: OnymousAudienceChatFromServerAnalysisEngine) =>
              log("OnymousAudienceChatFromServerAnalysisEngine")
              OnymousAudienceChatFromServerProcessingEngine
                .process(box, villageFactory, jsonVillageToProtocol(json.base.village), engine, json)
            case None => Failure(new NoEngineException(OnymousAudienceChatFromServerAnalysisEngine.name))
          }
        } else {
          log("JsonOnymousAudienceChatFromClient")
          onymousAudienceChatFromClientAnalysisEngine match {
            case Some(engine: OnymousAudienceChatFromClientAnalysisEngine) =>
              log("OnymousAudienceChatFromClientAnalysisEngine")
              OnymousAudienceChatFromClientProcessingEngine
                .process(box, villageFactory, jsonVillageToProtocol(json.base.village), engine, json)
            case None => Failure(new NoEngineException(OnymousAudienceChatFromClientAnalysisEngine.name))
          }
        }
      case Right(json: JsonOnymousAudienceScroll) =>
        log("JsonOnymousAudienceScroll")
        onymousAudienceScrollAnalysisEngine match {
          case Some(engine: OnymousAudienceScrollAnalysisEngine) =>
            log("OnymousAudienceScrollAnalysisEngine")
            OnymousAudienceScrollProcessingEngine
              .process(box, villageFactory, jsonVillageToProtocol(json.base.village), engine, json)
          case None => Failure(new NoEngineException(OnymousAudienceScrollAnalysisEngine.name))
        }
      case Right(json: JsonScroll) =>
        log("JsonScroll")
        scrollAnalysisEngine match {
          case Some(engine: ScrollAnalysisEngine) =>
            log("ScrollAnalysisEngine")
            ScrollProcessingEngine.process(box, villageFactory, jsonVillageToProtocol(json.base.village), engine, json)
          case None => Failure(new NoEngineException(ScrollAnalysisEngine.name))
        }
      case Right(json: JsonStar) =>
        log("JsonStar")
        starAnalysisEngine match {
          case Some(engine: StarAnalysisEngine) =>
            log("StarAnalysisEngine")
            StarProcessingEngine.process(box, villageFactory, jsonVillageToProtocol(json.base.village), engine, json)
          case None => Failure(new NoEngineException(StarAnalysisEngine.name))
        }
      case Right(json: JsonVote) =>
        log("JsonVote")
        voteAnalysisEngine match {
          case Some(engine: VoteAnalysisEngine) =>
            log("VoteAnalysisEngine")
            VoteProcessingEngine.process(box, villageFactory, jsonVillageToProtocol(json.base.village), engine, json)
          case None => Failure(new NoEngineException(VoteAnalysisEngine.name))
        }
      case Right(json: JsonChatFromServer) =>
        log("JsonChatFromServer")
        chatFromServerAnalysisEngine match {
          case Some(engine: ChatFromServerAnalysisEngine) =>
            log("ChatFromServerAnalysisEngine")
            ChatFromServerProcessingEngine
              .process(box, villageFactory, jsonVillageToProtocol(json.base.village), engine, json)
          case None => Failure(new NoEngineException(ChatFromServerAnalysisEngine.name))
        }
      case Right(json: JsonPhase) =>
        log("JsonPhase")
        json.base.phase match {
          case Morning.label =>
            import cats.implicits._
            if (json.base.day === 1) {
              firstMorningPhaseAnalysisEngine match {
                case Some(engine: FirstMorningPhaseAnalysisEngine) =>
                  log("FirstMorningPhaseAnalysisEngine")
                  FirstMorningPhaseProcessingEngine
                    .process(box, villageFactory, jsonVillageToProtocol(json.base.village), engine, json)
                case None => Failure(new NoEngineException(FirstMorningPhaseAnalysisEngine.name))
              }
            } else {
              morningPhaseAnalysisEngine match {
                case Some(engine: MorningPhaseAnalysisEngine) =>
                  log("MorningPhaseAnalysisEngine")
                  MorningPhaseProcessingEngine
                    .process(box, villageFactory, jsonVillageToProtocol(json.base.village), engine, json)
                case None => Failure(new NoEngineException(MorningPhaseAnalysisEngine.name))
              }
            }
          case Noon.label =>
            noonPhaseAnalysisEngine match {
              case Some(engine: NoonPhaseAnalysisEngine) =>
                log("NoonPhaseAnalysisEngine")
                NoonPhaseProcessingEngine
                  .process(box, villageFactory, jsonVillageToProtocol(json.base.village), engine, json)
              case None => Failure(new NoEngineException(NoonPhaseAnalysisEngine.name))
            }
          case Night.label =>
            nightPhaseAnalysisEngine match {
              case Some(engine: NightPhaseAnalysisEngine) =>
                log("NightPhaseAnalysisEngine")
                NightPhaseProcessingEngine
                  .process(box, villageFactory, jsonVillageToProtocol(json.base.village), engine, json)
              case None => Failure(new NoEngineException(NightPhaseAnalysisEngine.name))
            }
          case _ => Failure(new NoEngineException("PhaseAnalysisEngine"))
        }
      case Right(json: JsonFlavorText) =>
        log("JsonFlavorText")
        flavorTextAnalysisEngine match {
          case Some(engine: FlavorTextAnalysisEngine) =>
            log("FlavorTextAnalysisEngine")
            FlavorTextProcessingEngine
              .process(box, villageFactory, jsonVillageToProtocol(json.base.village), engine, json)
          case None => Failure(new NoEngineException(FlavorTextAnalysisEngine.name))
        }
      case Right(json: JsonGameResult) =>
        log("JsonGameResult")
        gameResultAnalysisEngine match {
          case Some(engine: GameResultAnalysisEngine) =>
            log("GameResultAnalysisEngine")
            GameResultProcessingEngine
              .process(box, villageFactory, jsonVillageToProtocol(json.base.village), engine, json)
          case None => Failure(new NoEngineException(GameResultAnalysisEngine.name))
        }
      case _ =>
        Failure(new NoEngineException("AnalysisEngine"))
    }
  }
}
