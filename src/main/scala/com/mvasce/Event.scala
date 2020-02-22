package com.mvasce

import com.github.nscala_time.time.Imports.DateTime
import org.apache.kafka.common.serialization.Serializer
import play.api.libs.json.Json
import org.apache.kafka.common.serialization.Deserializer
import play.api.libs.json.JsValue
import java.util

/**
  * Class Event
  *
  * @param match_id ID of the match
  * @param event_type Type of event
  * @param timestamp Timestamp in Unix epoch milliseconds
  */
case class Event(match_id: Int, event_type: String, timestamp: Long)

object Event {
  def apply(match_id: Int, event_type: String): Event =
    Event(match_id, event_type, DateTime.now().getMillis())
  def apply(match_id: Int, event_type: String, datetime: String): Event =
    Event(match_id, event_type, DateTime.parse(datetime).getMillis())
}

class EventSerializer extends Serializer[Event] {
  implicit val transactionReads = Json.format[Event]

  override def configure(
      configs: java.util.Map[String, _],
      isKey: Boolean
  ): Unit = {}

  override def serialize(topic: String, event: Event): Array[Byte] =
    Json.toJson(event).toString().getBytes()

  override def close(): Unit = {}
}

/**
  * Event Deserializer class
  *
  */
class EventDeserializer extends Deserializer[Event] {
  implicit val eventFormat = Json.format[Event]

  override def deserialize(topic: String, bytes: Array[Byte]): Event = {
    val json: JsValue = Json.parse(bytes)
    Json.fromJson[Event](json).get
  }

  override def configure(configs: util.Map[String, _], isKey: Boolean): Unit = {
    // nothing to do
  }

  override def close(): Unit = {
    //nothing to do
  }
}
