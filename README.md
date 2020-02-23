# Scala Kafka example

## Start kafka

```console
> docker run -p 2181:2181 -p 9092:9092 --env ADVERTISED_HOST=localhost --env ADVERTISED_PORT=9092 spotify/kafka
```

## Build

```console
> sbt assembly
```

## Run producer

```console
> java -cp ./target/scala-2.12/kafkaConsumer-assembly-0.1.jar com.mvasce.Producer
```

## Run consumer

```console
java -cp ./target/scala-2.12/kafkaConsumer-assembly-0.1.jar com.mvasce.Producer
```
