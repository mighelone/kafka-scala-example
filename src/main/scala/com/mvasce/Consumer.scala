package com.mvasce

import java.util

import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.Properties
import play.api.libs.json.{JsValue,Json}

import org.apache.kafka.common.serialization.Deserializer

import scala.collection.JavaConverters._
import com.github.nscala_time.time.Imports.DateTime
import org.slf4j.LoggerFactory


object Consumer {

  val BOOTSTRAP_SERVER = "localhost:9092"
  val TOPIC = "event"

  val logger = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {
    val (topic, bootstrap_server) = args.length match {
      case 2 => (args(0), args(1))
      case 1 => (args(0), BOOTSTRAP_SERVER)
      case 0 => (TOPIC, BOOTSTRAP_SERVER)
    }
    logger.info(s"Connecting to $bootstrap_server topic: $topic")
    consumeFromKafka(topic, bootstrap_server)
  }

  /**
    * Consumer messages from Kafka
    *
    * @param topic : name of the topic
    * @param bootstrap_server : name of the bootstrap servers
    */
  def consumeFromKafka(topic: String, bootstrap_server: String = "localhost:9092") = {
    val props = getKafkaProperties(topic, bootstrap_server)
    val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)
    consumer.subscribe(util.Arrays.asList(topic))
    while (true) {
      val record = consumer.poll(1000).asScala
      for (data <- record.iterator)
        println(data.value())
      consumer.commitAsync()
    }
  }

  /**
    * Get kafka properties object
    *
    * @param topic
    * @param bootstrap_server
    * @return Kafka properties object
    */
  def getKafkaProperties(topic: String, bootstrap_server: String): Properties = {
    val props = new Properties()
    props.put("bootstrap.servers", bootstrap_server)
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("auto.offset.reset", "earliest")
    props.put("group.id", "consumer-group")
    // props.put("value.deserializer", classOf[EventDeserializer])
    props
  }
  
  /**
    * Event data type
    *
    * @param timestamp : timestamp string using iso
    * @param event_type : type of event
    * @param match_id : ID of the match
    */
  case class Event(timestamp: String, event_type: String, match_id: Int)
  {
    val ts: DateTime = DateTime.parse(timestamp)

    /**
      * Get the timestamp in milliseconds
      *
      * @return timestamp in Unix milliseconds
      */
    def getCreationTime(): Long = {
      ts.getMillis
    }
  }

  implicit val eventFormat = Json.format[Event]


  class EventDeserializer extends Deserializer[Event] {
    override def deserialize(topic: String, bytes: Array[Byte]): Event = {
      val json: JsValue = Json.parse(bytes)
      Json.fromJson[Event](json).get
    }

    override def configure(configs: util.Map[String, _], isKey: Boolean):
    Unit = {
      // nothing to do
    }

    override def close(): Unit = {
      //nothing to do
    }
  }

}