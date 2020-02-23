package com.mvasce

import org.scalatest._
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.must.Matchers
import org.joda.time.DateTime

class EventSpec extends FlatSpec with Matchers {
  val ts: DateTime = new DateTime(2020, 2, 2, 12, 0)
  val event = Event(10, "example", ts)

  "An Event instance" should "contains a timestamp" in {
    event.event_type must equal("example")
    event.match_id must equal(10)
    event.timestamp must equal(ts)
  }

  "An Event instance" should "have a method getMillis" in {
    event.getMillis must equal(ts.toInstant().getMillis())
  }

  "An EventSerializer instance" should "convert an event into bytes" in {
    val serializer = new Event.EventSerializer
    val eventBytes = serializer.serialize("topic", event)
    val eventString = eventBytes.map(_.toChar).mkString

    eventString must equal(
      "{\"match_id\":10,\"event_type\":\"example\",\"timestamp\":\"2020-02-02T12:00:00.000+0000\"}"
    )
  }
}
