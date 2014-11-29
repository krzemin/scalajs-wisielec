package controllers

import play.api.mvc._
import hangman.Game

object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  def word = Action {
    Ok("skakanka")
  }

}
