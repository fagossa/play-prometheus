logLevel := Level.Warn

resolvers ++= Seq(
  Resolver.bintrayIvyRepo("kamon-io", "sbt-plugins")
)

addSbtPlugin("com.typesafe.sbt" % "sbt-aspectj" % "0.10.0")

addSbtPlugin("com.lucidchart" % "sbt-scalafmt" % "1.10")

addSbtPlugin("io.gatling" % "gatling-sbt" % "2.2.1")

addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.4")

addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")
