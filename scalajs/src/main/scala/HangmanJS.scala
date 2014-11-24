import scala.scalajs.js.JSApp
import org.scalajs.dom
import org.scalajs.dom.{HTMLButtonElement, document}

import scala.scalajs.js.annotation.JSExport
import scalatags.JsDom.all._

import hangman.Game

object HangmanJS extends JSApp {

  val game: Game = new Game("biedronka")

  def visibleText: String = game.visible.map(_.toUpper).mkString(" ")

  val visiblePar = p(id := "visible-word")(visibleText).render
  val mistakesPar = p(s"Skuch: ${game.mistakes}").render

  @JSExport
  def move(letter: Char): Unit = {
    game.move(letter.toLower)
    visiblePar.textContent = visibleText
    mistakesPar.textContent = s"Skuch: ${game.mistakes}"
  }

  val btnsMap: Map[Char, HTMLButtonElement] = game.alphabet.map { letter =>
    val btn = button(
      `type` := "button",
      `class` := "btn btn-primary letter-button"
    )(letter.toString).render

    btn.onclick = (e: dom.MouseEvent) => {
      btn.classList.add("disabled")
      move(letter)
    }

    letter -> btn
  }.toMap

  val alphabetView =
    game.alphabet.grouped(8).toList.map { lettersPortion =>
      ul(style := "list-style-type: none; margin: 0; padding: 0")(
        lettersPortion.map { letter =>
          li(style := "display: inline")(btnsMap(letter))
        }
      )
    }

  val layout = div(
    h1("Wisielec"),
    mistakesPar,
    visiblePar,
    br,
    alphabetView
  )

  def main(): Unit = {
    val gameDiv = document.getElementById("game")
    gameDiv.innerHTML = ""
    gameDiv.appendChild(layout.render)
  }

}
