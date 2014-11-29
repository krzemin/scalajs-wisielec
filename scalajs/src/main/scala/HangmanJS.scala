import scala.scalajs.js.JSApp
import org.scalajs.dom
import dom.extensions._
import org.scalajs.dom.{HTMLButtonElement, document}

import scala.scalajs.js.annotation.JSExport
import scala.util.{Failure, Success}
import scalatags.JsDom.all._

import scalajs.concurrent.JSExecutionContext.Implicits.runNow

import hangman._

class HangmanUI(game: Game) {

  def visibleText: String = game.visible.map(_.toUpper).mkString(" ")

  val visiblePar = p(id := "visible-word")(visibleText).render
  val mistakesPar = p(s"Skuch: ${game.mistakes}").render

  @JSExport
  def move(letter: Char): Unit = {
    game.move(letter.toLower)
    visiblePar.textContent = visibleText
    mistakesPar.textContent = s"Skuch: ${game.mistakes}"

    game.state match {
      case Won(tries) =>
        dom.alert(s"Hasło zgadnięte po $tries próbach!")
        dom.location.reload()
      case Lost =>
        dom.alert(s"Nie udało się zgadnąć hasła!")
        dom.location.reload()
      case _ =>
    }

  }

  val btnsMap: Map[Char, HTMLButtonElement] = game.alphabet.map { letter =>
    val btn = button(
      `type` := "button",
      `class` := "btn btn-primary letter-button"
    )(letter.toString).render

    btn.onclick = (e: dom.MouseEvent) => {
      if(game.state.isInstanceOf[InProgress]) {
        btn.classList.add("disabled")
        move(letter)
      }
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


  val gameDiv = document.getElementById("game")
  gameDiv.innerHTML = ""
  gameDiv.appendChild(layout.render)
}


object HangmanJS extends JSApp {

  def main(): Unit = {
    Ajax.get("/word").onComplete {
      case Success(resp) =>
        val word = resp.responseText
        val game = new Game(word)
        new HangmanUI(game)
      case Failure(why) =>
        dom.alert("Error in determining a word. Please refresh your browser to try again.")
    }
  }

}
