package protocol.engine.async.lobby.analysis.client2server

import licos.protocol.element.lobby.LobbyMessageProtocol
import licos.protocol.element.lobby.client2server.UpdateAvatarProtocol
import licos.protocol.engine.async.analysis.lobby.client2server.UpdateAvatarAnalysisEngine
import licos.protocol.engine.processing.lobby.{LobbyBOX, LobbyBOXNotFoundException}
import protocol.element.LobbyMessageTestProtocol
import protocol.engine.lobby.LobbyBox
import protocol.engine.lobby.example.client2server.UpdateAvatar

import scala.concurrent.{ExecutionContext, Future}

final class UpdateAvatarAE extends UpdateAvatarAnalysisEngine {
  override def process(box: LobbyBOX, updateAvatarProtocol: UpdateAvatarProtocol)(
      implicit ec:          ExecutionContext
  ): Future[LobbyMessageProtocol] = {
    box match {
      case _: LobbyBox => Future.successful(LobbyMessageTestProtocol(UpdateAvatar.`type`))
      case _ => Future.failed(new LobbyBOXNotFoundException())
    }
  }
}
