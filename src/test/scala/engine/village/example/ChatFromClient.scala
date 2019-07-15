package engine.village.example

import engine.Example

case class ChatFromClient(filePath: String) extends Example(filePath) {
  override val `type`: String = ChatFromClient.`type`
}

object ChatFromClient {
  val `type`: String = "ChatFromClient"
}