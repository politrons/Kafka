package main.scala

/**
  * Created by pabloperezgarcia on 08/01/2017.
  */

import java.util.{Date, Properties}

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import scala.util.Random

object Producer extends App {
  val events = 10000
  val topic = "test_topic"
  val brokers = "localhost:9092"
  val rnd = new Random()
  val props = new Properties()
  props.put("bootstrap.servers", brokers)
  props.put("client.id", "Producer")
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer")


  val producer = new KafkaProducer[String, String](props)
  val t = System.currentTimeMillis()
  val runtime = new Date().getTime()
  val idKey = "192.168.2." + rnd.nextInt(255)
  val msg = s"$runtime www.example.com $idKey"
  val data = new ProducerRecord[String, String]("test_topic", idKey, msg)

  //async
  //producer.send(data, (m,e) => {})
  //sync
  producer.send(data)
//  producer.close()
}