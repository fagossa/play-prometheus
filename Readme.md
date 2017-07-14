# playframework application + prometheus

This is just a example of how to monitor a playframework application using:
- prometheus client
- prometheus
- grafana

## Prerequisites

To run everything you just need `docker`.

## Starting everything

```
docker-compose up -d
```

....and everything should be up and running. Something similar to this image

![Containers](https://github.com/fagossa/play-prometheus/blob/version-2/images/containers.png)

## The play application

The play application is available at `http://localhost:9000/`.

The general idea is that the application has a _count metric_

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
The application inside the container is no able to start as the play! app always has the same PID.

## Prometheus targets

In order to verify that prometheus was able to connect to out application you can check `http://localhost:9090/targets`

![Grafana Dashboard](https://github.com/fagossa/play-prometheus/blob/master/images/targets.png)

Theoretically you should have:
- a target for our play application
- a target for prometheus itself

## Grafana

The Dashboard is now available at `http://localhost:3000` using the following
credentials

- username: admin
- password: admin

The password is stored in the `config.monitoring` env file

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
