package controllers

import play.api.mvc._
import play.api.libs.ws._
import scala.util.Random
import scala.concurrent.ExecutionContext.Implicits.global

object Application extends Controller {

  def index = Action {
    Ok(views.html.index())
  }

  val rand = new Random()

  def word = Action.async {
    implicit val app = play.api.Play.current

    val conf = play.api.Play.configuration(app)
    val wordsUrl: String = conf.getString("wordsFileUrl").get
    val wordsSize: Int = conf.getInt("wordsFileSize").get

    val buffSize = 200
    val offset = rand.nextInt(wordsSize - buffSize)

    WS.url(wordsUrl)
      .withHeaders("Range" -> s"bytes=$offset-${offset+buffSize}")
      .execute().map { resp =>
      if(resp.status == 206) {
        val words = resp.body.split("\r\n")
        Ok(words(1))
      } else {
        InternalServerError("Some problem with determining a word")
      }
    }
  }

}
