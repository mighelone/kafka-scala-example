name := "kafkaConsumer"

version := "0.1"

scalaVersion := "2.12.10"


libraryDependencies ++= Seq(
  "org.apache.kafka" %% "kafka" % "2.1.0",
  "com.typesafe.play" %% "play-json" % "2.8.1",
  // "org.slf4j" % "slf4j-jdk14" % "1.7.30",
  "org.slf4j" % "slf4j-api" % "1.7.30",
  "org.slf4j" % "slf4j-simple" % "1.7.30",
  "com.github.nscala-time" %% "nscala-time" % "2.22.0"
)


//https://stackoverflow.com/a/39058507
assemblyMergeStrategy in assembly := {
 case PathList("META-INF", xs @ _*) => MergeStrategy.discard
 case x => MergeStrategy.first
}