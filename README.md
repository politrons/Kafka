Author  Pablo Perez Garcia

![My image](src/main/resources/img/kafka.png)

Example of Producer/Consumer with Kafka

This example will require you install Kafka, follow the next steps to install/run

```
$ brew search kafka
$ brew install kafka
```

Run Zookeeper:

```
$ zkserver start
```

Run Kafka with default configuration:

```
$ cd /usr/local/Cellar/kafka/0.10.1.0/bin
$ kafka-server-start.sh /usr/local/etc/kafka/server.properties
```

Once that you have Kafka running you can just run the Producer app and the ConsumerRun

#Reactive Kafka

![My image](src/main/resources/img/reactive.png)

Nowasdays we should embrace reactive programing as much as we can, and that include distributed messages system

Thanks to Akka framework, we can use Akka streams Kafka ![Documentation](http://doc.akka.io/docs/akka-stream-kafka/current/home.html)

Here in the project you can use reactive consumer ![here](src/main/scala/reactive)


