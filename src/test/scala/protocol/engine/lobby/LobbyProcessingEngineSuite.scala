package protocol.engine.lobby

import java.net.URL
import java.nio.charset.StandardCharsets

import com.typesafe.scalalogging.Logger
import licos.json2protocol.lobby.Json2LobbyMessageProtocol
import licos.protocol.element.lobby.LobbyMessageProtocol
import licos.protocol.engine.processing.lobby.{LobbyProcessingEngine, LobbyProcessingEngineFactory}
import licos.protocol.engine.processing.{LobbyPE, SpecificProcessingEngineFactory}
import org.junit.experimental.theories.{DataPoints, Theories, Theory}
import org.junit.runner.RunWith
import org.scalatest.junit.AssertionsForJUnit
import play.api.libs.json.Json
import protocol.element.LobbyMessageTestProtocol
import protocol.engine.LobbyExample
import protocol.engine.lobby.analysis.client2server.{
  AdvancedSearchAE,
  AuthorizationRequestAcceptedAE,
  BuildVillageAE,
  ChangeLanguageAE,
  ChangeUserEmailAE,
  ChangeUserNameAE,
  ChangeUserPasswordAE,
  CreateHumanPlayerAE,
  CreateOnymousAudienceAE,
  CreateRobotPlayerAE,
  DeleteAvatarAE,
  EnterAvatarSelectionPageAE,
  EnterLobbyAE,
  GetAvatarInfoAE,
  GetSettingsAE,
  IdSearchAE,
  KickOutPlayerAE,
  LeaveWaitingPageAE,
  PlayAE,
  PongAE,
  ReadyAE,
  RenewAvatarTokenAE,
  RunRobotPlayerInTheBackgroundAE,
  SelectVillageAE,
  StopRobotPlayerAE,
  UpdateAvatarAE
}
import protocol.engine.lobby.analysis.server2client.{
  AuthorizationRequestAE,
  AuthorizationRequestAcceptedResponseAE,
  AvatarInfoAE,
  HumanPlayerSelectionPageAE,
  LobbyAE,
  NewAvatarTokenAE,
  OnymousAudienceSelectionPageAE,
  PingAE,
  PlayedAE,
  RobotPlayerSelectionPageAE,
  SearchResultAE,
  SettingsAE,
  WaitingPageAE
}
import protocol.engine.lobby.analysis.server2server.PlayedWithTokenAE
import protocol.engine.lobby.example.client2server.{
  AdvancedSearch,
  AuthorizationRequestAccepted,
  BuildVillage,
  ChangeLanguage,
  ChangeUserEmail,
  ChangeUserName,
  ChangeUserPassword,
  CreateHumanPlayer,
  CreateOnymousAudience,
  CreateRobotPlayer,
  DeleteAvatar,
  EnterAvatarSelectionPage,
  EnterLobby,
  GetAvatarInfo,
  GetSettings,
  IdSearch,
  KickOutPlayer,
  LeaveWaitingPage,
  Play,
  Pong,
  Ready,
  RenewAvatarToken,
  RunRobotPlayerInTheBackground,
  SelectVillage,
  StopRobotPlayer,
  UpdateAvatar
}
import protocol.engine.lobby.example.server2client.{
  AuthorizationRequest,
  AuthorizationRequestAcceptedResponse,
  AvatarInfo,
  HumanPlayerSelectionPage,
  Lobby,
  NewAvatarToken,
  OnymousAudienceSelectionPage,
  Ping,
  Played,
  RobotPlayerSelectionPage,
  SearchResult,
  Settings,
  WaitingPage
}
import protocol.engine.lobby.example.server2server.PlayedWithToken

import scala.io.{Codec, Source}
import scala.util.{Failure, Success}

object LobbyProcessingEngineSuite {
  @DataPoints
  def exampleSeq: Array[LobbyExample] = Array[LobbyExample](
    AdvancedSearch("advancedSearch.json"),
    BuildVillage("buildVillage.json"),
    ChangeLanguage("changeLanguage.json"),
    ChangeUserEmail("changeUserEmail.json"),
    ChangeUserName("changeUserName.json"),
    ChangeUserPassword("changeUserPassword.json"),
    EnterLobby("enterLobbyForAnonymousAudience.json"),
    EnterLobby("enterLobbyForHumanPlayer.json"),
    EnterLobby("enterLobbyForOnymousAudience.json"),
    EnterLobby("enterLobbyForRobotPlayer.json"),
    GetAvatarInfo("getAvatar.json"),
    GetSettings("getSettings.json"),
    IdSearch("idSearch.json"),
    KickOutPlayer("kickOutPlayer.json"),
    LeaveWaitingPage("leaveWaitingPage.json"),
    Play("play.json"),
    Pong("pong.json"),
    Ready("ready.json"),
    SelectVillage("selectVillage.json"),
    AuthorizationRequest("authorizationRequest.json"),
    AuthorizationRequestAcceptedResponse("authorizationRequestAcceptedResponse.json"),
    AvatarInfo("avatar.json"),
    Lobby("lobbyForAnonymousAudience.json"),
    Lobby("lobbyForHumanPlayer.json"),
    Lobby("lobbyForOnymousAudience.json"),
    Lobby("lobbyForRobotPlayer.json"),
    Ping("ping.json"),
    Played("played.json"),
    SearchResult("searchResult.json"),
    Settings("settings.json"),
    WaitingPage("waitingPage.json"),
    PlayedWithToken("playedWithToken.json"),
    AuthorizationRequestAccepted("authorizationRequestAccepted.json"),
    RenewAvatarToken("renewAvatarToken.json"),
    NewAvatarToken("newAvatarToken.json"),
    CreateHumanPlayer("createHumanPlayer.json"),
    CreateOnymousAudience("createOnymousAudience.json"),
    CreateRobotPlayer("createRobotPlayer.json"),
    DeleteAvatar("deleteAvatar.json"),
    RunRobotPlayerInTheBackground("runRobotPlayerInTheBackground.json"),
    StopRobotPlayer("stopRobotPlayer.json"),
    HumanPlayerSelectionPage("humanPlayerSelectionPage.json"),
    OnymousAudienceSelectionPage("onymousAudienceSelectionPage.json"),
    RobotPlayerSelectionPage("robotPlayerSelectionPage.json"),
    UpdateAvatar("updateAvatarName.json"),
    UpdateAvatar("updateAvatarImage.json"),
    UpdateAvatar("updateAvatarLanguage.json"),
    EnterAvatarSelectionPage("enterAvatarSelectionPage.json")
  )
}

@RunWith(classOf[Theories])
final class LobbyProcessingEngineSuite extends AssertionsForJUnit {

  private val log: Logger = Logger[LobbyProcessingEngineSuite]

  private val processingEngineFactory: LobbyProcessingEngineFactory = SpecificProcessingEngineFactory
    .create(LobbyPE)
    .asInstanceOf[LobbyProcessingEngineFactory]
    .set(new AdvancedSearchAE())
    .set(new AvatarInfoAE())
    .set(new BuildVillageAE())
    .set(new ChangeLanguageAE())
    .set(new ChangeUserEmailAE())
    .set(new ChangeUserNameAE())
    .set(new ChangeUserPasswordAE())
    .set(new EnterLobbyAE())
    .set(new GetAvatarInfoAE())
    .set(new GetSettingsAE())
    .set(new IdSearchAE())
    .set(new KickOutPlayerAE())
    .set(new LeaveWaitingPageAE())
    .set(new LobbyAE())
    .set(new PingAE())
    .set(new PlayAE())
    .set(new PlayedAE())
    .set(new PlayedWithTokenAE())
    .set(new PongAE())
    .set(new ReadyAE())
    .set(new SearchResultAE())
    .set(new SelectVillageAE())
    .set(new SettingsAE())
    .set(new WaitingPageAE())
    .set(new AuthorizationRequestAE())
    .set(new AuthorizationRequestAcceptedResponseAE())
    .set(new AuthorizationRequestAcceptedAE())
    .set(new RenewAvatarTokenAE())
    .set(new NewAvatarTokenAE())
    .set(new CreateHumanPlayerAE())
    .set(new CreateOnymousAudienceAE())
    .set(new CreateRobotPlayerAE())
    .set(new DeleteAvatarAE())
    .set(new RunRobotPlayerInTheBackgroundAE())
    .set(new StopRobotPlayerAE())
    .set(new HumanPlayerSelectionPageAE())
    .set(new OnymousAudienceSelectionPageAE())
    .set(new RobotPlayerSelectionPageAE())
    .set(new UpdateAvatarAE())
    .set(new EnterAvatarSelectionPageAE())

  private val processingEngine: LobbyProcessingEngine = processingEngineFactory.create

  @Theory
  def process(jsonExample: LobbyExample): Unit = {
    val jsonType:       String = jsonExample.`type`
    val url:            URL    = jsonExample.path
    implicit val codec: Codec  = Codec(StandardCharsets.UTF_8)
    log.info(url.toString)
    val source = Source.fromURL(url)
    val msg: String = source.getLines.mkString("\n")
    source.close()
    log.debug(msg)
    Json2LobbyMessageProtocol.toProtocolOpt(Json.parse(msg)) match {
      case Some(protocol: LobbyMessageProtocol) =>
        processingEngine.process(new LobbyBox(), protocol) match {
          case Success(protocol: LobbyMessageProtocol) =>
            protocol match {
              case p: LobbyMessageTestProtocol =>
                assert(p.text == jsonType)
              case _ =>
                fail("No LobbyMessageTestProtocol")
            }
          case Failure(error: Throwable) =>
            fail(
              Seq[String](
                "No response is generated.",
                error.getMessage,
                msg
              ).mkString("\n")
            )
        }
      case _ =>
        fail("No protocol")
    }
  }
}
