name := """ReactiveSearchPoc"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala, SbtWeb)

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



