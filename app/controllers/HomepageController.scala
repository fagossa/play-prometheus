package controllers

import play.api.mvc._
import utils.Authorization

class HomepageController(authorization: Authorization)
  extends Controller {

  def index = authorization.increaseVisitors { implicit request =>
    Ok(views.html.index("Your new application is ready."))
  }

}
