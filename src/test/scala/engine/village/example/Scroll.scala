package engine.village.example

import engine.Example

case class Scroll(filePath: String) extends Example(filePath) {
  override val `type`: String = Scroll.`type`
}

object Scroll {
  val `type`: String = "Scroll"
}