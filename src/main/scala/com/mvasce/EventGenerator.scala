package com.mvasce

import scala.util.Random
import org.joda.time.DateTime
import java.sql.Date

/**
  * Event Generator class
  * gnerate random events
  *
  * @param min_id
  * @param max_id
  */
case class EventGenerator(min_id: Int=1000, max_id: Int=1100)
{
    private val r = new Random
    private val delte_id = max_id- min_id
    val event_types: Seq[String] = Seq("Goal", "Kickoff", "Corner", "Injury")
    
    def get(): Event = {
        val event = event_types(r.nextInt(event_types.length))
        Event(r.nextInt(delte_id) + min_id, event, DateTime.now())
    }

}