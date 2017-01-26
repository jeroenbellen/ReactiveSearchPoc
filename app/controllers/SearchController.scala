package controllers

import javax.inject.{Inject, Singleton}

import actors.AutocompleteActor
import akka.actor.ActorSystem
import akka.stream.Materializer
import play.api.libs.streams.ActorFlow
import play.api.mvc.{Controller, WebSocket}

@Singleton
class SearchController @Inject()(implicit actorSystem: ActorSystem, materializer: Materializer)
  extends Controller {

  def searchSocket = WebSocket.accept[String, String] {
    _ => ActorFlow.actorRef(out => AutocompleteActor.props(out))
  }
}
