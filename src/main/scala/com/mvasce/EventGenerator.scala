package com.mvasce

import scala.util.Random

case class EventGenerator()
{
    private val r = new Random
    private val min_id: Int = 1000
    private val max_id: Int = 1100
    private val delte_id = max_id- min_id
    val event_types: Seq[String] = Seq("Goal", "Kickoff", "Corner", "Injury")
    
    def get(): Event = {
        val event = event_types(r.nextInt(event_types.length))
        Event(r.nextInt(delte_id) + min_id, event)
    }

}