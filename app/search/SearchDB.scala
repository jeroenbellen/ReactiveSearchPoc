package search

import javax.inject.{Inject, Singleton}

import better.files.File
import org.elasticsearch.client.Client
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.node.Node
import play.api.{Configuration, Logger}


@Singleton
class SearchDB @Inject()(playConfiguration: Configuration) {

  private val dataDir = File.newTemporaryDirectory("reactiveSearchPoc").pathAsString

  Logger.info("Starting elastic search")
  Logger.info(s"Using data dir -> $dataDir")

  private val settings = Settings.builder()
    .put("node.name", "embedded-search")
    .put("http.enabled", "false")
    .put("path.home", s"$dataDir")
    .put("transport.type", "local")
    .put("node.max_local_storage_nodes", "1")
    .build()

  private val node = new Node(settings)
    .start()


  def createClient(): Client = node.client()

}
