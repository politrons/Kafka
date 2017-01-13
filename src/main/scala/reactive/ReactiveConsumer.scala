package reactive

import akka.actor.{ActorSystem, Props}

/**
  * Created by pabloperezgarcia on 10/01/2017.
  */
object ReactiveConsumer extends App {

  val system = ActorSystem("Politron-Chief")
  system.mailboxes.deadLetterMailbox
  // create the actor
  val consumer = system.actorOf(Props(new ConsumerActor), name = "consumer")
  // start the tasks
  consumer ! ConsumeMsg

}



