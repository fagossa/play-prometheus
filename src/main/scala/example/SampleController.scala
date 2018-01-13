package example

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpRequest, HttpResponse}
import akka.http.scaladsl.model.StatusCodes.InternalServerError
import akka.http.scaladsl.server.Directives.{complete, get, path}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

import akka.stream.Materializer
import com.typesafe.config.Config
import kamon.akka.http.KamonTraceDirectives

class SampleController(config: Config)(implicit system: ActorSystem, mat: Materializer)
    extends KamonTraceDirectives
    with SampleMonitoringDirectives {

  val routes: Route = {
    get {
      pathSingleSlash {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`, "<h1>Your new application is ready.</h1>"))
      } ~
        path("login") {
          increaseCurrentUsers {
            complete {
              "Logged in..."
            }
          }
        } ~
        path("logout") {
          decreaseCurrentUsers {
            complete {
              "Logged out..."
            }
          }
        } ~
        path("go-to-outside") {
          traceName("outside") {
            complete {
              Http().singleRequest(
                HttpRequest(
                  uri =
                    s"http://${config.getString("services.ip-api.host")}:${config.getString("services.ip-api.port")}/"
                )
              )
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
