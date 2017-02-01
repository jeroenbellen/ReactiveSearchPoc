name := """ReactiveSearchPoc"""

version := "1.2-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, SbtWeb, ElasticBeanstalkPlugin)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1" % Test
)

libraryDependencies += "org.webjars" % "webjars-play_2.11" % "2.5.0-4"
libraryDependencies += "org.webjars.npm" % "react" % "15.4.2"
libraryDependencies += "org.webjars.npm" % "react-dom" % "15.4.2"

libraryDependencies += ("org.elasticsearch.client" % "transport" % "5.1.2").excludeAll(ExclusionRule(organization = "io.netty"))
libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % "2.7"
libraryDependencies += "org.apache.logging.log4j" % "log4j-api" % "2.7"
libraryDependencies += "com.github.pathikrit" % "better-files_2.11" % "2.17.1"


maintainer in Docker := "Jeroen"
dockerExposedPorts := Seq(9000)
dockerBaseImage := "java:latest"




