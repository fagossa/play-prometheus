package controllers

import play.api.mvc._
import utils.Authorization

class SessionController(authorization: Authorization) extends Controller {

  def login = authorization.increaseLoggedUsers { implicit request =>
    Ok("Logged in ...")
  }

  def logout = authorization.decreaseLoggedUsers { implicit request =>
    Ok("Logged out!")
  }

}
