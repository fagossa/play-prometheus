package example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, HttpResponse}
import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

import akka.stream.Materializer
import com.typesafe.config.Config
import kamon.akka.http.KamonTraceDirectives

class SampleController(config: Config)
                      (implicit system: ActorSystem, mat: Materializer)
  extends KamonTraceDirectives with SampleMonitoringDirectives {

  val metricCurrentUsers = "play_current_users"

  val routes: Route = {
    get {
      path("login") {
        increaseMetricNamed(metricCurrentUsers) {
          complete {
            "Logged in..."
          }
        }
      } ~
        path("logout") {
          decreaseMetricNamed(metricCurrentUsers) {
            complete {
              "Logged out..."
            }
          }
        } ~
        path("go-to-outside") {
          traceName("outside") {
            complete {
              Http().singleRequest(HttpRequest(uri = s"http://${config.getString("services.ip-api.host")}:${config.getString("services.ip-api.port")}/"))
            }
          }
        } ~
        path("internal-error") {
          complete(HttpResponse(InternalServerError))
        } ~
        path("fail-with-exception") {
          throw new RuntimeException("Failed!")
        }
    }
  }

}
