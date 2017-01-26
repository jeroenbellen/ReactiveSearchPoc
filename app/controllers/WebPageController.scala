package controllers

import javax.inject.Inject

import play.api.mvc._

class WebPageController @Inject()(webJarAssets: WebJarAssets) extends Controller {

  def index = Action {
    Ok(views.html.index(webJarAssets))
  }
}
