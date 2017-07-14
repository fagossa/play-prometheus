# Playframework application + prometheus + grafana

This is just a example of how to monitor a playframework application using:
- prometheus client
- prometheus
- grafana

## Prerequisites

To run everything you just need `docker`.

## How to starting everything?

> Before running this command be aware that the docker files are quite heavy, so this could take a while

```
docker-compose up -d
```

....and everything should be up and running. Something similar to this image

![Containers](https://github.com/fagossa/play-prometheus/blob/master/images/containers.png)

Which means that we have now 4 running containers:
- Grafana
- CAdvisor
- Our play application
- Prometheus

## The play application

The play application is available at `http://localhost:9000/`.

The general idea is that the application has a _count metric_ for a particular url

```
val requests = Counter.build()
  .name("play_requests_total")
  .help("Total requests.")
  .register()
```

which gets increased each time a user navigates to _/_

```
@Singleton
class HomeController @Inject()(visitsCounter: IndexAccessCounter) extends Controller {

  def index = Action {
    visitsCounter.requests.inc()
    Ok(views.html.index("Your new application is ready."))
  }

}
```

Just in case, don't forget to add the following script to your `build.sbt`

```
javaOptions in Universal ++= Seq(
  "-Dpidfile.path=/dev/null"
)
```

This is a workaroung as the application inside the container is no able to restart because the play app always has the same PID.

## Prometheus targets

In order to verify that prometheus was able to connect to out application you can check `http://localhost:9090/targets`

![Grafana Dashboard](https://github.com/fagossa/play-prometheus/blob/master/images/targets.png)

Theoretically you should have:
- a target for our play application
- a target for prometheus itself

Before testing the integration between prometheus and grafana you can verify your metrics by going to `http://localhost:9090/graph`.

You should be able to get something like this

![graph](https://github.com/fagossa/play-prometheus/blob/master/images/graph.png)

## Grafana

The Dashboard is now available at `http://localhost:3000` using the following credentials

- username: admin
- password: admin

The password is stored in the `config.monitoring` env file.

Once logged in you need to:

### Create a data source

Now we need to create the Prometheus Datasource in order to connect Grafana to Prometheus

  - Go to Grafana Menu at the top left corner (looks like a fireball)
  - Go to Data Sources
  - Enter the following data

![AddDataSource](https://github.com/fagossa/play-prometheus/blob/master/images/datasource.png)

### Import the Dashboard

Import the file `Grafana_Dashboard.json`


## Result

Your resulting Dashboard should be similar to this

![AddDataSource](https://github.com/fagossa/play-prometheus/blob/master/images/result.png)

Don't forget to go to `http://localhost:9000/` and check how your dashboards changes.
