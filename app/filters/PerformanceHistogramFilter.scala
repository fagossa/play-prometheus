package filters

import akka.stream.Materializer
import com.google.inject.{Inject, Singleton}
import org.lyranthe.prometheus.client._
import play.api.mvc._
import utils.PrometheusMetric

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

private case class RouteDetails(method: String, route: String)

class PerformanceHistogramFilter(metrics: PrometheusMetric)
                                (implicit ec: ExecutionContext, val mat: Materializer) extends Filter {

  private final val ServerErrorClass = "5xx"

  def apply(nextFilter: RequestHeader => Future[Result])
           (requestHeader: RequestHeader): Future[Result] = {
    val timer = Timer()
    val future = nextFilter(requestHeader)

    getRouteDetails(requestHeader) match {
      case Some(details) =>
        future.onComplete {
          time(timer) { statusCode =>
            metrics.httpRequestLatency.labelValues(details.method,
              details.route,
              statusCode)
          }
        }

      case None =>
        metrics.httpRequestMismatch.inc()
    }

    future
  }

  private def getRouteDetails(requestHeader: RequestHeader): Option[RouteDetails] = {
    for {
      method <- requestHeader.tags.get("ROUTE_VERB")
      routePattern <- requestHeader.tags.get("ROUTE_PATTERN")
      route = routePattern.replaceAll("<.*?>", "").replaceAll("\\$", ":")
    } yield RouteDetails(method, route)
  }

  private def statusCodeLabel(result: Result) =
    (result.header.status / 100) + "xx"

  private def time(timer: Timer)(
    templatedHistogram: String => LabelledHistogram): Try[Result] => Unit = {
    case Success(result) =>
      templatedHistogram(statusCodeLabel(result)).observeDuration(timer)
    case Failure(_) =>
      templatedHistogram(ServerErrorClass).observeDuration(timer)
  }

}