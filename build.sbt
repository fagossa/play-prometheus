import Dependencies._

val commonSettings = Seq(
  scalacOptions ++= Seq("-unchecked", "-feature", "-explaintypes", "-deprecation")
)

lazy val root = (project in file("."))
  .settings(name := "kamon-akka-http")
  .settings(commonSettings: _*)
  .settings(Seq(
    scalaVersion := "2.11.8",
    testGrouping in Test := singleTestPerJvm((definedTests in Test).value, (javaOptions in Test).value)
  ))
  .settings(libraryDependencies ++= Seq(
    kamon.Core,
    kamon.JMX,
    kamon.KamonAutoweave,
    kamon.Akka,
    kamon.AkkaHttp,
    kamon.SystemMetrics,
    kamon.Scala,

    akka.http,
    akka.slf4j,
    akka.httpCore,

    log.logbackClassic,
    log.logbackCore,
    log.log4jOverSlf4j,
    log.logstashLogback,
    log.janino,
    log.scalaLogging
  ))
  .settings(
    parallelExecution in Test := false
  )


import sbt.Tests._
def singleTestPerJvm(tests: Seq[TestDefinition], jvmSettings: Seq[String]): Seq[Group] =
  tests map { test =>
    Group(
      name = test.name,
      tests = Seq(test),
      runPolicy = SubProcess(ForkOptions(runJVMOptions = jvmSettings)))
  }

//bashScriptExtraDefines += s"""addJava "-Dkamon.auto-start=true""""