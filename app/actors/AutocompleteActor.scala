package actors

import akka.actor.{Actor, ActorRef, Props}
import org.elasticsearch.action.search.SearchType
import org.elasticsearch.client.Client
import org.elasticsearch.common.unit.TimeValue
import org.elasticsearch.index.query.QueryBuilders
import play.api.libs.json.{JsObject, JsString, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object AutocompleteActor {

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

        if (lastSnippet == snippet)
          out ! Json.stringify(Json.toJson(list.toList))
      }
  }
}
