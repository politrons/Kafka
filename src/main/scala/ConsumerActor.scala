import akka.Done
import akka.actor.{Actor, ActorSystem}
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by pabloperezgarcia on 11/01/2017.
  */
class ConsumerActor extends Actor {

  implicit val materializer = ActorMaterializer()

  implicit val ec: ExecutionContext = ActorSystem().dispatcher

  val system = ActorSystem("Politron-Chief")

  val topic: Set[String] = Set[String]("politrons_topic")

  val consumerSettings: ConsumerSettings[Array[Byte], String] =
    ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer)
      .withBootstrapServers("localhost:9092")
      .withGroupId("group1")
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

  val done: Future[Done] =
    Consumer.committableSource(consumerSettings, Subscriptions.topics("politrons_topic"))
      .mapAsync(1) { msg =>
        Future.successful(Done).map(_ => {
          val message = msg.record.value()
          println(s"Message received:$message")
          message
        })
      }
      .runWith(Sink.ignore)

  override def receive: Receive = {
    case ConsumeMsg =>
      done.onComplete( res => {
        println(s"All messages emitted:$res")
      })
  }

}