name := "kafkaConsumer"

version := "0.1"

scalaVersion := "2.12.10"


libraryDependencies ++= Seq(
  "org.apache.kafka" %% "kafka" % "2.1.0",
  "com.typesafe.play" %% "play-json" % "2.8.1",
  "org.slf4j" % "slf4j-jdk14" % "1.7.30"
)

