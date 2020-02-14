package com.mvasce

import java.util

import org.apache.kafka.clients.consumer.KafkaConsumer
import java.util.Properties
import play.api.libs.json.{JsValue,Json}

import org.apache.kafka.common.serialization.Deserializer

import scala.collection.JavaConverters._

object Consumer {
  def main(args: Array[String]): Unit = {
    consumeFromKafka("event")
  }
  
  def consumeFromKafka(topic: String) = {
    val props = new Properties()
    props.put("bootstrap.servers", "localhost:9092")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("auto.offset.reset", "earliest")
    props.put("group.id", "consumer-group")
//    props.put("key.deserializer", classOf[StringDeserializer])
    props.put("value.deserializer", classOf[CustomTypeDeserializer])
    val consumer: KafkaConsumer[String, String] = new KafkaConsumer[String, String](props)
    consumer.subscribe(util.Arrays.asList(topic))
    while (true) {
      val record = consumer.poll(1000).asScala
      for (data <- record.iterator)
        println(data.value())
        consumer.commitAsync()
    }
  }

  case class Event(timestamp: String, event_type: String, match_id: Int)

  implicit val eventFormat = Json.format[Event]


  class CustomTypeDeserializer extends Deserializer[Event] {
//    private val gson: Gson = new Gson()

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