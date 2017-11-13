package example

import akka.http.scaladsl.server.Directive0
import akka.http.scaladsl.server.directives.BasicDirectives
import akka.http.scaladsl.server.Directives._
import kamon.Kamon
import scala.concurrent.duration._

trait SampleMonitoringDirectives {

  def increaseMetricNamed(name: String): Directive0 = mapRequest { req =>
    val myMMCounter = Kamon.metrics.minMaxCounter(name, refreshInterval = 500 milliseconds)
    myMMCounter.increment()
    req
  }

  def decreaseMetricNamed(name: String): Directive0 = mapRequest { req =>
    val myMMCounter = Kamon.metrics.minMaxCounter(name, refreshInterval = 500 milliseconds)
    myMMCounter.decrement()
    req
  }

}
