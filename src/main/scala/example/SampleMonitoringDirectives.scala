package example

import akka.http.scaladsl.server.Directive0
import akka.http.scaladsl.server.directives.BasicDirectives
import akka.http.scaladsl.server.Directives._
import kamon.Kamon
import scala.concurrent.duration._

trait SampleMonitoringDirectives {

  implicit val value = scala.language.postfixOps

  val myMMCounter = Kamon.metrics.minMaxCounter("play_current_users", refreshInterval = 500 milliseconds)

  def increaseCurrentUsers: Directive0 = mapRequest { req =>
    myMMCounter.increment()
    req
  }

  def decreaseCurrentUsers: Directive0 = mapRequest { req =>
    myMMCounter.decrement()
    req
  }

}
