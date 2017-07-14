package example

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import com.typesafe.config.ConfigFactory
import kamon.Kamon

object WebServer extends App {
  Kamon.start()

  val config = ConfigFactory.load()

  val port: Int = config.getInt("http.port")
  val interface: String = config.getString("http.interface")

  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val logger = Logging(system, getClass)

  val routes = { // logRequestResult("akka-http-with-kamon") {
    get {
      path("ok") {
        complete {
          "ok"
        }
      } ~
        path("go-to-outside") {
          complete {
            Http().singleRequest(HttpRequest(uri = s"http://${config.getString("services.ip-api.host")}:${config.getString("services.ip-api.port")}/"))
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

  Http().bindAndHandle(routes, interface, port).foreach { _ =>
    logger.info(s"Server online at http://$interface:$port/...")
  }

  scala.sys.addShutdownHook {
    logger.info("Terminating...")
    Kamon.shutdown()
    logger.info("Terminated... Bye")
    system.terminate()
  }

}
