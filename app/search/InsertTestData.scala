package search

import javax.inject.{Inject, Singleton}

import actors.TestDataActor
import actors.TestDataActor.Insert
import akka.actor.ActorSystem
import better.files.File
import play.api.Application

@Singleton
class InsertTestData @Inject()(application: Application, akkaSystem: ActorSystem, searchDB: SearchDB) {

  private val testDataActor = akkaSystem.actorOf(TestDataActor.props(searchDB.createClient()))

  application.getFile("/conf/testData").listFiles()
    .map(_.getAbsolutePath)
    .map(File(_))
    .map(_.unzip())
    .flatMap(_.children)
    .foreach(testDataActor ! Insert(_))


}
