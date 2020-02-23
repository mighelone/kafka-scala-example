package com.mvasce

// import com.github.nscala_time.time.Imports.DateTime
import org.joda.time.{DateTime, Instant}
import org.apache.kafka.common.serialization.Serializer
import play.api.libs.json.Json
import org.apache.kafka.common.serialization.Deserializer
import play.api.libs.json.JsValue
import java.util
import play.api.libs.json.Format
import play.api.libs.json.JodaReads
import play.api.libs.json.JodaWrites
// import com.github.nscala_time.time.Imports

trait EventTrait {
  def getMillis: Long
}

/**
  * Class Event
  *
  * @param match_id ID of the match
  * @param event_type Type of event
  * @param timestamp Timestamp in Unix epoch milliseconds
  */
case class Event(match_id: Int, event_type: String, timestamp: DateTime)
    extends EventTrait {
  def getMillis: Long = timestamp.toInstant().getMillis()
}

object Event {
  val pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ"
  implicit val dateFormat = Format[DateTime](
    JodaReads.jodaDateReads(pattern),
    JodaWrites.jodaDateWrites(pattern)
  )
  implicit val eventFormat = Json.format[Event]

  class EventSerializer extends Serializer[Event] {
    // implicit val transactionReads = Json.format[Event]

    override def configure(
        configs: java.util.Map[String, _],
        isKey: Boolean
    ): Unit = {}

    override def serialize(topic: String, event: Event): Array[Byte] =
      Json.toJson(event).toString().getBytes()

    override def close(): Unit = {}
  }
  class EventDeserializer extends Deserializer[Event] {

    override def deserialize(topic: String, bytes: Array[Byte]): Event = {
      val json: JsValue = Json.parse(bytes)
      Json.fromJson[Event](json).get
    }

    override def configure(
        configs: util.Map[String, _],
        isKey: Boolean
    ): Unit = {
      // nothing to do
    }

    override def close(): Unit = { //nothing to do}
    }

  }
}


object Test {
    def main(args: Array[String]): Unit = {
        val event = Event(10, "kickoff", DateTime.now())
        println(event)
        
        val serializer = new Event.EventSerializer
        val deserializer = new Event.EventDeserializer

        val event_byte: Array[Byte] = serializer.serialize("test", event)
        
        val result = deserializer.deserialize("test", event_byte)

        println(result)


    }
}
