package main.scala
package com.colobu.kafka

import java.util.Properties
import java.util.concurrent._

import org.apache.kafka.clients.consumer.KafkaConsumer

import scala.collection.JavaConversions._

class Consumer(val brokers: String,
               val groupId: String,
               val topic: String) {

  val consumer = new KafkaConsumer[String, String](createConsumerConfig())

  def createConsumerConfig(): Properties = {
    val props = new Properties()
    props.put("bootstrap.servers", brokers)
    props.put("group.id", groupId)
    props.put("enable.auto.commit", "true")
    props.put("auto.commit.interval.ms", "1000")
    props.put("session.timeout.ms", "30000")
    props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer")
    props
  }

  def run() = {
    consumer.subscribe(List(topic))
    Executors.newSingleThreadExecutor.execute(new Runnable {
      override def run(): Unit = {
        while (true) {
          val records = consumer.poll(2000)
          if (records != null) {
            for (record <- records) {
              System.out.println("Received message: (" + record.key() + ", " + record.value() + ") at offset " + record.offset())
            }
          }
          Thread.sleep(1000)
        }
      }
    })
  }
}

object ConsumerRun extends App {
  val example = new Consumer("localhost:9092", "group1", "test_topic")
  example.run()
}
