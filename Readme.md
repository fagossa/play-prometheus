# Akka-http + influxdb + grafana

This repository shows how to monitor an `akka-http` application.

## Using JMX
- kamon-jmx
- jmc (present in the jdk)

## Using influxdb

The following tools are used
- kamon-influxdb
- influxdb
- grafana

### Prerequisites

To run everything you need `docker` and `docker-compose`

```
docker-compose up -d
```

### Influxdb

It seems that influxdb needs the db to exist before it starts accepting the metrics
sent by kamon. So, in order to create them execute the following command

```
curl -POST http://localhost:8086/query --data-urlencode "q=CREATE DATABASE mydb"
```

Let's check the contents of our db:

* Connect to the influxdb container

```
docker exec -it $(docker ps -aqf "name=docker_influxdb_1") /bin/bash
```
* Connect to influxdb

```
influx
show databases
use mydb
show series
```

### The panel in grafana

The following instruction allows to add the metric `play_current_users` in a new panel

```
SELECT "mean" FROM "kamon-timers" WHERE "category" = 'min-max-counter' AND "entity" = 'play_current_users' AND "hostname" =~ /^$entity$/ AND $timeFilter
```
