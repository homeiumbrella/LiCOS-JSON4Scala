package licos.json.engine.processing

import licos.json.engine.analysis.auth.robot2server.AuthenticationAndAuthorizationRequestAnalysisEngine
import licos.json.engine.analysis.auth.server2robot.{
  AuthenticationRequestResponseAnalysisEngine,
  AuthorizationRequestResponseAnalysisEngine
}

@SuppressWarnings(Array[String]("org.wartremover.warts.Var", "org.wartremover.warts.Overloading"))
final class AuthProcessingEngineFactory extends ProcessingEngineFactory {
  private var authenticationAndAuthorizationRequestEngine: Option[AuthenticationAndAuthorizationRequestAnalysisEngine] =
    None
  private var authenticationRequestResponseEngine: Option[AuthenticationRequestResponseAnalysisEngine] = None
  private var authorizationRequestResponseEngine:  Option[AuthorizationRequestResponseAnalysisEngine]  = None

  override def create: AuthProcessingEngine = {
    new AuthProcessingEngine(
      authenticationAndAuthorizationRequestEngine: Option[AuthenticationAndAuthorizationRequestAnalysisEngine],
      authenticationRequestResponseEngine:         Option[AuthenticationRequestResponseAnalysisEngine],
      authorizationRequestResponseEngine:          Option[AuthorizationRequestResponseAnalysisEngine]
    )
  }

  def set(
      authenticationAndAuthorizationRequestEngine: AuthenticationAndAuthorizationRequestAnalysisEngine
  ): AuthProcessingEngineFactory = {
    this.authenticationAndAuthorizationRequestEngine = Option(authenticationAndAuthorizationRequestEngine)
    this
  }

  def set(
      authenticationRequestResponseEngine: AuthenticationRequestResponseAnalysisEngine
  ): AuthProcessingEngineFactory = {
    this.authenticationRequestResponseEngine = Option(authenticationRequestResponseEngine)
    this
  }

  def set(
      authorizationRequestResponseEngine: AuthorizationRequestResponseAnalysisEngine
  ): AuthProcessingEngineFactory = {
    this.authorizationRequestResponseEngine = Option(authorizationRequestResponseEngine)
    this
  }
}
