package actors

import akka.actor.{Actor, ActorRef, Props}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object AutocompleteActor {

  def props(out: ActorRef) = Props(new AutocompleteActor(out))

}

class AutocompleteActor(out: ActorRef) extends Actor {

  var lastSnippet: String = ""

  override def receive: Receive = {
    case snippet: String =>
      lastSnippet = snippet

      Future {
        if (lastSnippet == snippet)
          out ! s"Hi! You searched for $snippet !"
      }
  }
}
