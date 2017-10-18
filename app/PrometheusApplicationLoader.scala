import akka.stream.ActorMaterializer
import controllers._
import filters.PerformanceHistogramFilter
import org.lyranthe.prometheus.client.DefaultRegistry
import play.api._
import play.api.ApplicationLoader.Context
import play.api.routing.Router
import router.Routes
import utils.{Authorization, PrometheusMetric}

class PrometheusApplicationLoader extends ApplicationLoader {
  override def load(context: Context) = {
    LoggerConfigurator(context.environment.classLoader).foreach {
      _.configure(context.environment)
    }
    new PrometheusComponents(context).application
  }
}

class PrometheusComponents(context: Context)
  extends BuiltInComponentsFromContext(context) {

  implicit val ec = actorSystem.dispatcher
  implicit val mat = ActorMaterializer()(actorSystem)

  implicit val prometheusRegistry = new DefaultRegistry()

  val metrics = new PrometheusMetric(context.lifecycle)

  val authorization = new Authorization(metrics)

  val performanceFilter = new PerformanceHistogramFilter(metrics)

  override lazy val httpFilters = List(performanceFilter)

  lazy val router: Router =
    new Routes(
      httpErrorHandler,
      new HomepageController(authorization),
      new SessionController(authorization),
      new PrometheusMetricsController(metrics),
      new controllers.Assets(httpErrorHandler)
    )
}
