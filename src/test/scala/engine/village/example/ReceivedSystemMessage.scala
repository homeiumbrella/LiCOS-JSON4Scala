package engine.village.example

import engine.Example

case class ReceivedSystemMessage(filePath: String) extends Example(filePath) {
  override val `type`: String = ReceivedSystemMessage.`type`
}

object ReceivedSystemMessage {
  val `type`: String = "ReceivedSystemMessage"
}