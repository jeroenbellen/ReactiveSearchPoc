package actors

import actors.AutocompleteActor.Reply
import akka.actor.{Actor, ActorRef, Props}
import org.elasticsearch.action.search.SearchType
import org.elasticsearch.client.Client
import org.elasticsearch.common.unit.TimeValue
import org.elasticsearch.index.query.QueryBuilders
import play.api.libs.json.{JsObject, JsString, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object AutocompleteActor {

  case class Reply(snippet: String, json: String)

  def props(out: ActorRef, client: Client) = Props(new AutocompleteActor(out, client))

}

class AutocompleteActor(out: ActorRef, client: Client) extends Actor {

  var lastSnippet: String = ""

  override def receive: Receive = {
    case snippet: String =>
      lastSnippet = snippet

      Future {
        val response = client
          .prepareSearch("people")
          .setSearchType(SearchType.DEFAULT)
          .setQuery(
            QueryBuilders.multiMatchQuery(snippet)
              .field("Full Name", 3)
              .field("Country", 2)
              .field("Email", 1)
              .`type`("cross_fields")
          )
          .setSize(10)
          .get(TimeValue.timeValueSeconds(1))


        val list = response.getHits.hits()
          .map(_.getSource)
          .map {
            obj =>
              JsObject(
                List(
                  "id" -> JsString(obj.get("Id").toString),
                  "fullName" -> JsString(obj.get("Full Name").toString),
                  "email" -> JsString(obj.get("Email").toString),
                  "country" -> JsString(obj.get("Country").toString)
                )
              )
          }

        Reply(
          snippet,
          Json.stringify(JsObject(
            List(
              "snippet" -> JsString(snippet),
              "result" -> Json.toJson(list.toList)
            )
          ))
        )
      }
        .onComplete {
          f => self ! f.get
        }

    case Reply(snippet: String, json: String) => {
      if (lastSnippet == snippet)
        out ! json
    }
  }
}
