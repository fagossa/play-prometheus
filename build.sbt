enablePlugins(DockerPlugin)

name := """play-prometheus"""

version := "1.4-SNAPSHOT"

lazy val root = (project in file("."))
  .enablePlugins(PlayScala)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  jdbc,
  cache,
  ws,
  "org.lyranthe.prometheus" %% "client" % "0.9.0-M1",
  "org.lyranthe.prometheus" % "play25_2.11" % "0.8.4"
) ++ testDependencies

def testDependencies = Seq(
  "org.scalatestplus.play" %% "scalatestplus-play" % "1.5.1"
).map(_ % Test)

javaOptions in Universal ++= Seq(
  "-Dpidfile.path=/dev/null"
)

lazy val pushDockerTask = TaskKey[Unit]("pushDockerTask", "push to docker hub")

pushDockerTask := {
  import sys.process._
  val anotherVersion = version.value

  println(s"tagging image... $anotherVersion")
  s"docker tag play-prometheus:$anotherVersion fagossa/play-prometheus:latest".!

  println("pushing image...")
  s"docker push fagossa/play-prometheus:latest".!

  println("done!")
}
