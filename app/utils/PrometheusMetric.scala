package utils

import org.lyranthe.prometheus.client._
import play.api.inject.ApplicationLifecycle

import scala.concurrent.Future

class PrometheusMetric(
  lifecycle: ApplicationLifecycle
)(implicit val prometheusRegistry: Registry) {

  def buildOutputText: String = prometheusRegistry.outputText

  val counter = Counter(metric"play_requests_total", "Total requests.").labels().register
  val gauge = Gauge(metric"play_current_users", "Actual connected users").labels().register

  private val httpHistogramBuckets = {
    val buckets = for (p <- Vector[Double](0.0001, 0.001, 0.01, 0.1, 1, 10);
                       s <- Vector(1, 2, 5)) yield (p * s)
    HistogramBuckets(buckets: _*)
  }

  val httpRequestLatency =
    Histogram(metric"http_request_duration_seconds",
      "Duration of HTTP request in seconds")(httpHistogramBuckets)
      .labels(label"method", label"path", label"status")
      .register

  val httpRequestMismatch =
    Counter(metric"http_request_mismatch_total", "Number mismatched routes")
      .labels()
      .register

  lifecycle.addStopHook { () => Future.successful(gauge.dec()) }

}


