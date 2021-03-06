package licos.knowledge

sealed abstract class RobotPlayerStatus(val label: String)

case object Connected extends RobotPlayerStatus("connected")
case object AwaitingAuthorization extends RobotPlayerStatus("awaiting authorization")
case object AwaitingCommunicationTest extends RobotPlayerStatus("awaiting communication test")
case object RunningInTheBackground extends RobotPlayerStatus("running in the background")
case object RunningInTheForeground extends RobotPlayerStatus("running in the foreground")
