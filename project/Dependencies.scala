import sbt._

object Dependencies {

  object akka {
    private val akkaVersion = "2.4.16"
    private val akkaHttpVersion = "10.0.3"
    val Http      = "com.typesafe.akka" %% "akka-http"      % akkaHttpVersion
    val Slf4j     = "com.typesafe.akka" %% "akka-slf4j"     % akkaVersion
    val HttpCore  = "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion
  }

  object kamon {
    private val version = "0.6.6"
    val Core           = "io.kamon" %% "kamon-core"            % version
    val JMX            = "io.kamon" %% "kamon-jmx"             % version
    val KamonAutoweave = "io.kamon" %% "kamon-autoweave"       % "0.6.5"
    val Akka           = "io.kamon" %% "kamon-akka-2.4"        % version
    val AkkaHttp       = "io.kamon" %% "kamon-akka-http"       % version
    val SystemMetrics  = "io.kamon" %% "kamon-system-metrics"  % version
    val Scala          = "io.kamon" %% "kamon-scala"           % version
  }

  object log {
    val LogbackClassic  = "ch.qos.logback"             %  "logback-classic"  % "1.1.7"
    val LogbackCore     = "ch.qos.logback"             %  "logback-core"     % "1.1.7"
    val Log4jOverSlf4j  = "org.slf4j"                  %  "log4j-over-slf4j" % "1.7.21"
    val LogstashLogback = "net.logstash.logback"       %  "logstash-logback-encoder" % "4.7"
    val Janino          = "org.codehaus.janino"        %  "janino"           % "2.6.1"
    val ScalaLogging    = "com.typesafe.scala-logging" %% "scala-logging"    % "3.5.0"
  }

  object aspectj {
    private val version = "1.8.10"
    val Aspectjweaver = "org.aspectj" % "aspectjweaver" % version
  }

}
