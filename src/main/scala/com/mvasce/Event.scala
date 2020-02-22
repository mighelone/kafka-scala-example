package com.mvasce

import com.github.nscala_time.time.Imports.DateTime
import org.apache.kafka.common.serialization.Serializer
import play.api.libs.json.Json

case class Event(match_id: Int, event_type: String, timestamp: Long)


object Event {
    def apply(match_id: Int, event_type: String): Event =  Event(match_id, event_type, DateTime.now().getMillis())
    def apply(match_id: Int, event_type: String, datetime: String): Event = Event(match_id, event_type, DateTime.parse(datetime).getMillis())
}

class EventSerializer extends Serializer[Event] {
        implicit val transactionReads = Json.format[Event] 

        override def configure(configs: java.util.Map[String, _], isKey: Boolean): Unit = {}

        override def serialize(topic: String, event: Event): Array[Byte] = Json.toJson(event).toString().getBytes()

        override def close(): Unit = {}
    }