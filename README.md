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


