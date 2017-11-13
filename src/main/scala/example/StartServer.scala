package example

import akka.actor.ActorSystem
import akka.event.Logging
import akka.http.scaladsl._
import akka.stream.ActorMaterializer
import com.typesafe.config.{Config, ConfigFactory}
import kamon.Kamon
import kamon.metric.instrument.Time

import scala.util.{Failure, Success}

object StartServer extends App {
  Kamon.start()

  val config: Config = ConfigFactory.load()

  val port: Int = config.getInt("http.port")
  val interface: String = config.getString("http.interface")

  implicit val system = ActorSystem()
  implicit val executor = system.dispatcher
  implicit val materializer = ActorMaterializer()

  val logger = Logging(system, getClass)

  Http().bindAndHandle(new SampleController(config).routes, interface, port).onComplete {
    case Success(_) =>
      logger.info(s"Server online at http://$interface:$port/...")
      val start = System.currentTimeMillis()
      Kamon.metrics.gauge("runningtime", Time.Milliseconds)(() => System.currentTimeMillis() - start)

    case Failure(error) =>
      logger.error(error, s"Impossible to bind to $interface:$port")
  }

  sys.addShutdownHook {
    logger.info("Terminating...")
    Kamon.shutdown()
    logger.info("Bye")
    system.terminate()
  }

}
