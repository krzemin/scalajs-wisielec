import scala.scalajs.js.JSApp
import org.scalajs.dom
import dom.document

import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._

object HangmanJS extends JSApp {

  val game = new hangman.Game("biedronka")

  @JSExport
  def move(letter: Char): Unit = {
    println(letter)
    game.move(letter.toLower)
    updateView()
  }

  def updateView(): Unit = {
    val layout = div(
      h1("Wisielec"),
      p(s"Skuch: ${game.mistakes}"),
      p(style := "font-size: 32px")(game.visible.map(_.toUpper).mkString(" ")),
      br,
      game.alphabet.grouped(8).toList.map { lettersPortion =>
        ul(style := "list-style-type: none; margin: 0; padding: 0")(
          lettersPortion.map { letter =>
            li(style := "display: inline")(
              button(
                `type` := "button",
                style := "width: 2em",
                onclick := s"""HangmanJS().move('${letter.toString}')""")(
                  letter.toString
                )
            )
          }
        )
      }
    )

    val gameDiv = document.getElementById("game")

    gameDiv.appendChild(layout.render)
  }


  def main(): Unit = {

    updateView()
  }

}
