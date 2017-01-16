package main.scala

/**
  * Created by pabloperezgarcia on 08/01/2017.
  */

import java.util.{Date, Properties, UUID}

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord, RecordMetadata}

import scala.util.Random

object Producer extends App {
  val brokers = "localhost:9092"
  val rnd = new Random()
  val props = new Properties()
  val topics = List("politrons_topic", "politrons_topic1")
  createProperty()
  sendData()

  private def createProperty() = {
    props.put("auto.create.topics.enable", "true") //Create the topic when it´s publish if does not exist
    props.put("bootstrap.servers", brokers)
    props.put("client.id", "Producer")
    props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  }

  private def sendData() = {
    while (true) {
      topics.foreach(topic => {
        val producer = new KafkaProducer[String, String](props)
        producer.send(createMessage(topic), (metadata: RecordMetadata, exception: Exception) => {
          println(s"Message sent to topic:${metadata.topic()} partition:${metadata.partition()}")
        })
        Thread.sleep(1000)
      })
    }
  }

  private def createMessage(topic: String) = {
    val runtime = new Date().getTime
    val idKey = UUID.randomUUID().toString
    val msg = s"$runtime message topic:$topic"
    new ProducerRecord[String, String](topic, idKey, msg)
  }
}