package com.mvasce

import java.util.Properties

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.mvasce.Consumer.{BOOTSTRAP_SERVER, TOPIC}


object Producer {

  val logger = LoggerFactory.getLogger("producer")

  @volatile var keepRunning = true

  def main(args: Array[String]): Unit = {
    val r = scala.util.Random

    val props = new Properties()
    props.put("bootstrap.servers", BOOTSTRAP_SERVER)
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", classOf[EventSerializer])

    val topic = TOPIC
    val producer = new KafkaProducer[String, Event](props)

    // this is for stopping gracefully the producer
    val mainThread = Thread.currentThread();
    Runtime.getRuntime.addShutdownHook(new Thread() {
      override def run = {
        logger.info("inside addShutDownHook handler")
        keepRunning = false
        mainThread.join()
      }
    })

    val generator = EventGenerator()
    while (keepRunning) {
      val event: Event = generator.get
      logger.info(s"Sending message $event")
      
      val record = new ProducerRecord(topic, event.event_type, event)
      producer.send(record)
      Thread.sleep(100)
    }
    producer.close()
  }

}

