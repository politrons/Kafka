package reactive

import akka.actor.{ActorSystem, Props}

/**
  * Created by pabloperezgarcia on 10/01/2017.
  */
object ReactiveConsumer extends App {

  run()

  def run(): Unit ={
    val system = ActorSystem("Politron-Chief")
    system.mailboxes.deadLetterMailbox
    // create the master
    val consumer = system.actorOf(Props(new ConsumerActor), name = "consumer")
    // start the tasks
    consumer ! ConsumeMsg
  }

}



