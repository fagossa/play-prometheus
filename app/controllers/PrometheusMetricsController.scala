package controllers

import java.io.Writer

import akka.util.ByteString
import play.api.http.HttpEntity
import play.api.mvc._
import utils.PrometheusMetric

class PrometheusMetricsController(metrics: PrometheusMetric)
  extends Controller {

  def metricsOutput = Action {
    val samples = new StringBuilder()
    val writer = new WriterAdapter(samples)

    writer.write(metrics.buildOutputText)
    writer.close()

    Result(
      header = ResponseHeader(200, Map.empty),
      body = HttpEntity.Strict(ByteString(samples.toString), Some("text/plain"))
    )
  }
}

private[controllers] class WriterAdapter(buffer: StringBuilder) extends Writer {

  override def write(charArray: Array[Char], offset: Int, length: Int): Unit = {
    buffer ++= new String(new String(charArray, offset, length).getBytes("UTF-8"), "UTF-8")
  }

  override def flush(): Unit = {}

  override def close(): Unit = {}
}