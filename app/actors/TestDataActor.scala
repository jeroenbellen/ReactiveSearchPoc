package actors


import actors.TestDataActor.Insert
import akka.actor.{Actor, Props}
import better.files.File
import org.elasticsearch.client.Client
import play.api.Logger
import play.libs.Json

import scala.collection.JavaConversions._

object TestDataActor {

  case class Insert(file: File)

  def props(client: Client) = Props(new TestDataActor(client))

}

class TestDataActor(client: Client) extends Actor {

  override def receive: Receive = {
    case Insert(file: File) =>
      Logger.info(s"Inserting data from file ${file.pathAsString}")

      val bulk = client.prepareBulk()
      for (person <- asJson(file).elements()) {

        bulk.add(
          client.prepareIndex("people", "people").setSource(Json.stringify(person))
        )
      }

      bulk.execute().get()
      Logger.info(s"Done inserting data from file ${file.pathAsString}")
  }

  private def asJson(file: File) = {
    Json.parse(
      file.lines.mkString
    )
  }
}
