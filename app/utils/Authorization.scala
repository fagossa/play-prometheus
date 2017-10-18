package utils

import play.api.mvc.{ActionBuilder, Request, Result}

import scala.concurrent.Future

class Authorization(metrics: PrometheusMetric) {
  def increaseLoggedUsers = new ActionBuilder[Request] {
    override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
      metrics.gauge.inc()
      block(request)
    }
  }

  def decreaseLoggedUsers = new ActionBuilder[Request] {
    override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
      metrics.gauge.dec()
      block(request)
    }
  }

  def increaseVisitors = new ActionBuilder[Request] {
    override def invokeBlock[A](request: Request[A], block: (Request[A]) => Future[Result]) = {
      metrics.counter.inc()
      block(request)
    }
  }
}
