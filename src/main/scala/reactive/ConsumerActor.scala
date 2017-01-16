package reactive

import akka.Done
import akka.actor.{Actor, ActorSystem}
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by pabloperezgarcia on 11/01/2017.
  */
class ConsumerActor extends Actor {

  implicit val materializer = ActorMaterializer()

  implicit val ec: ExecutionContext = ActorSystem().dispatcher

  private val TOPIC1 = "politrons_topic"
  private val TOPIC2 = "politrons_topic1"

  val system = ActorSystem("Politrons")

  val consumerSettings: ConsumerSettings[Array[Byte], String] =
    ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer)
      .withBootstrapServers("localhost:9092")
      .withGroupId("group1")
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

  val consume: Future[Done] =
    Consumer.committableSource(consumerSettings, Subscriptions.topics(TOPIC1))
      .mapAsync(1) { msg => Future.successful(Done).map(_ => msg.record.value()) }
      .filter(msg => !msg.isEmpty)
      .map(msg => msg.toUpperCase)
      .runForeach(msg => println(s"Message received:$msg"))

  val multipleConsumer: Future[Done] =
    Consumer.committableSource(consumerSettings, Subscriptions.topics(TOPIC1))
      .flatMapConcat(msg => Consumer.committableSource(consumerSettings, Subscriptions.topics(TOPIC2))
        .map(msg1 => msg.record.value().concat(" || ") concat msg1.record.value()))
      .filter(msg => !msg.isEmpty)
      .map(msg => msg.toUpperCase)
      .runForeach(msg => println(s"Merge consumers:$msg"))


  override def receive: Receive = {
    case ConsumeMsg =>
      multipleConsumer.onComplete(res => {
        println(s"All messages emitted:$res")
      })
//      Await.ready(consume, 10.minutes).value.get
//      Await.ready(multipleConsumer, 10.minutes).value.get
  }

}