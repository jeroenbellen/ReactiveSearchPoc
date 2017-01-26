package actors

import akka.actor.{Actor, ActorRef, Props}

object AutocompleteActor {

  def props(out: ActorRef) = Props(new AutocompleteActor(out))

}

class AutocompleteActor(out: ActorRef) extends Actor {

  override def receive: Receive = {
    case snippet: String =>
      out ! s"Hi! You searched for $snippet !"
  }
}
