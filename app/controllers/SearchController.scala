package controllers

import javax.inject.{Inject, Singleton}

import actors.AutocompleteActor
import akka.actor.ActorSystem
import akka.stream.{Materializer, OverflowStrategy}
import play.api.libs.streams.ActorFlow
import play.api.mvc.{Controller, WebSocket}
import search.SearchDB

@Singleton
class SearchController @Inject()(searchDB: SearchDB)(implicit actorSystem: ActorSystem, materializer: Materializer)
  extends Controller {

  def searchSocket = WebSocket.accept[String, String] {
    _ =>
      ActorFlow.actorRef(
        out =>
          AutocompleteActor.props(
            out,
            searchDB.createClient()),
        bufferSize = 1,
        overflowStrategy = OverflowStrategy.dropHead
      )
  }
}
