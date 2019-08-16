package licos.json.engine.processing

import licos.json.element.lobby.{JsonBuildVillage, JsonLeaveWaitingPage, JsonReady}
import licos.json.engine.BOX
import licos.json.engine.analysis.lobby.client2server._
import licos.json.engine.analysis.village
import licos.json.engine.analysis.village.client2server._
import licos.json.engine.analysis.village.server2client._
import licos.json.flow.{FlowController, VillageFlowController}
import licos.json.lobby.{JsonLeaveWaitingPage, JsonReady}
import licos.json.village._
import licos.json.village.invite.{JsonNextGameInvitation, JsonNextGameInvitationIsClosed}
import licos.json.village.receipt.{JsonReceivedFlavorTextMessage, JsonReceivedPlayerMessage, JsonReceivedSystemMessage}
import play.api.libs.json.{JsValue, Json}

/** This class implements the processing engine that aggregates and runs analysis engines for village.
  *
  * @param readyEngine the analysis engine for Ready JSON.
  * @param receivedPlayerMessageEngine the analysis engine for Received-player-message JSON.
  * @param receivedSystemMessageEngine the analysis engine for Received-system-message JSON.
  * @param receivedFlavorTextMessageEngine the analysis engine for Flavor-text-message JSON.
  * @param chatFromClientEngine the analysis engine for Chat-from-client JSON.
  * @param chatFromServerEngine the analysis engine for Chat-from-server JSON.
  * @param audienceChatFromClientEngine the analysis engine for Audience-chat-from-client JSON.
  * @param audienceChatFromServerEngine the analysis engine for Audience-chat-from-server JSON.
  * @param boardEngine the analysis engine for Board JSON.
  * @param voteEngine the analysis engine for Vote JSON.
  * @param scrollEngine the analysis engine for Scroll JSON.
  * @param starEngine the analysis engine for Star JSON.
  * @param phaseEngine the analysis engine for Phase JSON.
  * @param flavorTextEngine the analysis engine for Flavor-text JSON.
  * @param gameResultEngine the analysis engine for Game-result JSON.
  * @param buildVillageEngine the analysis engine for Build-village JSON.
  * @param leaveWaitingPageEngine the analysis engine for Leave-waiting-page JSON.
  * @param errorFromClientEngine the analysis engine for Error-from-client JSON.
  * @param errorFromServerEngine the analysis engine for Error-from-server JSON.
  * @author Kotaro Sakamoto
  */
class VillageProcessingEngine(readyEngine: Option[ReadyAnalysisEngine],
                              receivedPlayerMessageEngine: Option[ReceivedPlayerMessageAnalysisEngine],
                              receivedSystemMessageEngine: Option[ReceivedSystemMessageAnalysisEngine],
                              receivedFlavorTextMessageEngine: Option[ReceivedFlavorTextMessageAnalysisEngine],
                              chatFromClientEngine: Option[village.client2server.ChatAnalysisEngine],
                              chatFromServerEngine: Option[village.server2client.ChatAnalysisEngine],
                              audienceChatFromClientEngine: Option[licos.json.engine.analysis.village.client2server.AudienceChatAnalysisEngine],
                              audienceChatFromServerEngine: Option[licos.json.engine.analysis.village.server2client.AudienceChatAnalysisEngine],
                              boardEngine: Option[BoardAnalysisEngine],
                              voteEngine: Option[VoteAnalysisEngine],
                              scrollEngine: Option[ScrollAnalysisEngine],
                              starEngine: Option[StarAnalysisEngine],
                              phaseEngine: Option[PhaseAnalysisEngine],
                              flavorTextEngine: Option[FlavorTextAnalysisEngine],
                              gameResultEngine: Option[GameResultAnalysisEngine],
                              buildVillageEngine: Option[BuildVillageAnalysisEngine],
                              leaveWaitingPageEngine: Option[LeaveWaitingPageAnalysisEngine],
                              nextGameInvitationEngine: Option[NextGameInvitationAnalysisEngine],
                              nextGameInvitationIsClosedEngine: Option[NextGameInvitationIsClosedAnalysisEngine],
                              errorFromClientEngine: Option[licos.json.engine.analysis.village.client2server.ErrorAnalysisEngine],
                              errorFromServerEngine: Option[licos.json.engine.analysis.village.server2client.ErrorAnalysisEngine]) extends ProcessingEngine {

  override protected val flowController: FlowController = new VillageFlowController()

  /** Returns a play.api.libs.json.JsValue response from a JSON message.
    *
    * @param box a box.
    * @param msg a JSON message.
    * @return a play.api.libs.json.JsValue option.
    */
  override def process(box: BOX, msg: String): Option[JsValue] = {

    val jsValue: JsValue = Json.parse(msg)

    def log(label: String): Unit = {
      val format: String = "VillageProcessingEngine.process %s%n"
      System.err.printf(format, label)
    }

    flowController.flow(jsValue) match {
      case Some(ready: JsonReady) =>
        log("JsonReady")
        readyEngine.flatMap(_.process(box, ready))
      case Some(receivedPlayerMessage: JsonReceivedPlayerMessage) =>
        log("JsonReceivedPlayerMessage")
        receivedPlayerMessageEngine.flatMap(_.process(box, receivedPlayerMessage))
      case Some(receivedSystemMessage: JsonReceivedSystemMessage) =>
        log("JsonReceivedSystemMessage")
        receivedSystemMessageEngine.flatMap(_.process(box, receivedSystemMessage))
      case Some(receivedFlavorTextMessage: JsonReceivedFlavorTextMessage) =>
        log("JsonReceivedFlavorTestMessage")
        receivedFlavorTextMessageEngine.flatMap(_.process(box, receivedFlavorTextMessage))
      case Some(chatFromClient: JsonChatFromClient) =>
        log("JsonChatFromClient")
        chatFromClientEngine.flatMap(_.process(box, chatFromClient))
      case Some(chatFromServer: JsonChatFromServer) =>
        log("JsonChatFromServer")
        chatFromServerEngine.flatMap(_.process(box, chatFromServer))
      case Some(audienceChat: JsonAudienceChat) =>
        log("JsonAudienceChat")
        if (audienceChat.isFromServer) {
          log("JsonAudienceChatFromServer")
          audienceChatFromServerEngine.flatMap(_.process(box, audienceChat))
        } else {
          log("JsonAudienceChatFromClient")
          audienceChatFromClientEngine.flatMap(_.process(box, audienceChat))
        }
      case Some(board: JsonBoard) =>
        log("JsonBoard")
        boardEngine.flatMap(_.process(box, board))
      case Some(vote: JsonVote) =>
        log("JsonVote")
        voteEngine.flatMap(_.process(box, vote))
      case Some(scroll: JsonScroll) =>
        log("JsonScroll")
        scrollEngine.flatMap(_.process(box, scroll))
      case Some(star: JsonStar) =>
        log("JsonStar")
        starEngine.flatMap(_.process(box, star))
      case Some(phase: JsonPhase) =>
        log("JsonPhase")
        phaseEngine.flatMap(_.process(box, phase))
      case Some(flavorText: JsonFlavorText) =>
        log("JsonFlavorText")
        flavorTextEngine.flatMap(_.process(box, flavorText))
      case Some(gameResult: JsonGameResult) =>
        log("JsonGameResult")
        gameResultEngine.flatMap(_.process(box, gameResult))
      case Some(buildVillage: JsonBuildVillage) =>
        log("JsonBuildVillage")
        buildVillageEngine.flatMap(_.process(box, buildVillage))
      case Some(leaveWaitingPage: JsonLeaveWaitingPage) =>
        log("JsonLeaveWaitingPage")
        leaveWaitingPageEngine.flatMap(_.process(box, leaveWaitingPage))
      case Some(nextGameInvitation: JsonNextGameInvitation) =>
        log("JsonNextGameInvitation")
        nextGameInvitationEngine.flatMap(_.process(box, nextGameInvitation))
      case Some(nextGameInvitationIsClosed: JsonNextGameInvitationIsClosed) =>
        log("JsonNextGameInvitationIsClosed")
        nextGameInvitationIsClosedEngine.flatMap(_.process(box, nextGameInvitationIsClosed))
      case Some(error: JsonError) =>
        log("JsonError")
        if (error.isFromServer) {
          log("JsonErrorFromServer")
          errorFromServerEngine.flatMap(_.process(box, error))
        } else {
          log("JsonErrorFromClient")
          errorFromClientEngine.flatMap(_.process(box, error))
        }
      case _ =>
        log("return None")
        None
    }
  }
}