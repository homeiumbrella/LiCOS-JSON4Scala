package licos.protocol.engine.async.processing.lobby

import com.typesafe.scalalogging.Logger
import licos.protocol.element.lobby.LobbyMessageProtocol
import licos.protocol.element.lobby.client2server._
import licos.protocol.element.lobby.server2client._
import licos.protocol.element.lobby.server2server.PlayedWithTokenProtocol
import licos.protocol.engine.async.analysis.lobby.client2server._
import licos.protocol.engine.async.analysis.lobby.server2client._
import licos.protocol.engine.async.analysis.lobby.server2server.PlayedWithTokenAnalysisEngine
import licos.protocol.engine.async.processing.ProcessingEngine
import licos.protocol.engine.processing.lobby.LobbyBOX
import licos.protocol.engine.processing.{JSON2ProtocolException, NoEngineException}

import scala.concurrent.{ExecutionContext, Future}

class LobbyProcessingEngine(
    pongEngine:                                 Option[PongAnalysisEngine],
    pingEngine:                                 Option[PingAnalysisEngine],
    waitingPageEngine:                          Option[WaitingPageAnalysisEngine],
    lobbyEngine:                                Option[LobbyAnalysisEngine],
    enterLobbyEngine:                           Option[EnterLobbyAnalysisEngine],
    getAvatarInfoEngine:                        Option[GetAvatarInfoAnalysisEngine],
    avatarInfoEngine:                           Option[AvatarInfoAnalysisEngine],
    selectVillageEngine:                        Option[SelectVillageAnalysisEngine],
    leaveWaitingPageEngine:                     Option[LeaveWaitingPageAnalysisEngine],
    kickOutPlayerEngine:                        Option[KickOutPlayerAnalysisEngine],
    buildVillageEngine:                         Option[BuildVillageAnalysisEngine],
    advancedSearchEngine:                       Option[AdvancedSearchAnalysisEngine],
    idSearchEngine:                             Option[IdSearchAnalysisEngine],
    playEngine:                                 Option[PlayAnalysisEngine],
    playedEngine:                               Option[PlayedAnalysisEngine],
    playedWithTokenEngine:                      Option[PlayedWithTokenAnalysisEngine],
    readyEngine:                                Option[ReadyAnalysisEngine],
    searchResultEngine:                         Option[SearchResultAnalysisEngine],
    changeLangEngine:                           Option[ChangeLangAnalysisEngine],
    changeUserEmailEngine:                      Option[ChangeUserEmailAnalysisEngine],
    changeUserNameEngine:                       Option[ChangeUserNameAnalysisEngine],
    changeUserPasswordEngine:                   Option[ChangeUserPasswordAnalysisEngine],
    getSettingsEngine:                          Option[GetSettingsAnalysisEngine],
    settingsEngine:                             Option[SettingsAnalysisEngine],
    authorizationRequestEngine:                 Option[AuthorizationRequestAnalysisEngine],
    authorizationRequestAcceptedResponseEngine: Option[AuthorizationRequestAcceptedResponseAnalysisEngine],
    authorizationRequestAcceptedEngine:         Option[AuthorizationRequestAcceptedAnalysisEngine]
) extends ProcessingEngine {

  private final val logger = Logger[LobbyProcessingEngine]

  @SuppressWarnings(
    Array[String](
      "org.wartremover.warts.ImplicitParameter",
      "org.wartremover.warts.Nothing",
      "org.wartremover.warts.Overloading"
    )
  )
  def process(box: LobbyBOX, msg: LobbyMessageProtocol)(implicit ec: ExecutionContext): Future[LobbyMessageProtocol] = {

    def log(label: String): Unit = {
      val format: String = "process %s"
      logger.info(format.format(label))
    }

    msg match {
      case protocol: PongProtocol =>
        pongEngine match {
          case Some(engine) =>
            log(PongAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(PongAnalysisEngine.name))
        }
      case protocol: PingProtocol =>
        pingEngine match {
          case Some(engine) =>
            log(PingAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(PingAnalysisEngine.name))
        }
      case protocol: WaitingPageProtocol =>
        waitingPageEngine match {
          case Some(engine) =>
            log(WaitingPageAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(WaitingPageAnalysisEngine.name))
        }
      case protocol: LobbyProtocol =>
        lobbyEngine match {
          case Some(engine) =>
            log(LobbyAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(LobbyAnalysisEngine.name))
        }
      case protocol: EnterLobbyProtocol =>
        enterLobbyEngine match {
          case Some(engine) =>
            log(EnterLobbyAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(EnterLobbyAnalysisEngine.name))
        }
      case protocol: GetAvatarInfoProtocol =>
        getAvatarInfoEngine match {
          case Some(engine) =>
            log(GetAvatarInfoAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(GetAvatarInfoAnalysisEngine.name))
        }
      case protocol: AvatarInfoProtocol =>
        avatarInfoEngine match {
          case Some(engine) =>
            log(AvatarInfoAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(AvatarInfoAnalysisEngine.name))
        }
      case protocol: SelectVillageProtocol =>
        selectVillageEngine match {
          case Some(engine) =>
            log(SelectVillageAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(SelectVillageAnalysisEngine.name))
        }
      case protocol: LeaveWaitingPageProtocol =>
        leaveWaitingPageEngine match {
          case Some(engine) =>
            log(LeaveWaitingPageAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(LeaveWaitingPageAnalysisEngine.name))
        }
      case protocol: KickOutPlayerProtocol =>
        kickOutPlayerEngine match {
          case Some(engine) =>
            log(KickOutPlayerAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(KickOutPlayerAnalysisEngine.name))
        }
      case protocol: BuildVillageProtocol =>
        buildVillageEngine match {
          case Some(engine) =>
            log(BuildVillageAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(BuildVillageAnalysisEngine.name))
        }
      case protocol: AdvancedSearchProtocol =>
        advancedSearchEngine match {
          case Some(engine) =>
            log(AdvancedSearchAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(AdvancedSearchAnalysisEngine.name))
        }
      case protocol: IdSearchProtocol =>
        idSearchEngine match {
          case Some(engine) =>
            log(IdSearchAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(IdSearchAnalysisEngine.name))
        }
      case protocol: PlayProtocol =>
        playEngine match {
          case Some(engine) =>
            log(PlayAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(PlayAnalysisEngine.name))
        }
      case protocol: PlayedProtocol =>
        playedEngine match {
          case Some(engine) =>
            log(PlayedAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(PlayedAnalysisEngine.name))
        }
      case protocol: PlayedWithTokenProtocol =>
        playedWithTokenEngine match {
          case Some(engine) =>
            log(PlayedWithTokenAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(PlayedWithTokenAnalysisEngine.name))
        }
      case protocol: ReadyProtocol =>
        readyEngine match {
          case Some(engine) =>
            log(ReadyAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(ReadyAnalysisEngine.name))
        }
      case protocol: SearchResultProtocol =>
        searchResultEngine match {
          case Some(engine) =>
            log(SearchResultAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(SearchResultAnalysisEngine.name))
        }
      case protocol: ChangeLangProtocol =>
        changeLangEngine match {
          case Some(engine) =>
            log(ChangeLangAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(ChangeLangAnalysisEngine.name))
        }
      case protocol: ChangeUserEmailProtocol =>
        changeUserEmailEngine match {
          case Some(engine) =>
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(ChangeUserEmailAnalysisEngine.name))
        }
      case protocol: ChangeUserNameProtocol =>
        changeUserNameEngine match {
          case Some(engine) =>
            log(ChangeUserNameAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(ChangeUserNameAnalysisEngine.name))
        }
      case protocol: ChangeUserPasswordProtocol =>
        changeUserPasswordEngine match {
          case Some(engine) =>
            log(ChangeUserPasswordAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(ChangeUserPasswordAnalysisEngine.name))
        }
      case protocol: GetSettingsProtocol =>
        getSettingsEngine match {
          case Some(engine) =>
            log(GetSettingsAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(GetSettingsAnalysisEngine.name))
        }
      case protocol: SettingsProtocol =>
        settingsEngine match {
          case Some(engine) =>
            log(SettingsAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(SettingsAnalysisEngine.name))
        }
      case protocol: AuthorizationRequestProtocol =>
        authorizationRequestEngine match {
          case Some(engine) =>
            log(AuthorizationRequestAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(AuthorizationRequestAnalysisEngine.name))
        }
      case protocol: AuthorizationRequestAcceptedResponseProtocol =>
        authorizationRequestAcceptedResponseEngine match {
          case Some(engine) =>
            log(AuthorizationRequestAcceptedResponseAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(AuthorizationRequestAcceptedResponseAnalysisEngine.name))
        }
      case protocol: AuthorizationRequestAcceptedProtocol =>
        authorizationRequestAcceptedEngine match {
          case Some(engine) =>
            log(AuthorizationRequestAcceptedAnalysisEngine.name)
            engine.process(box, protocol)
          case None => Future.failed(new NoEngineException(AuthorizationRequestAcceptedAnalysisEngine.name))
        }
      case _ =>
        Future.failed(new JSON2ProtocolException("No protocol"))
    }
  }
}