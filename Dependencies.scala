import sbt._

object DependenciesCommon {
  val backendDeps = Seq(
      "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
      "ch.qos.logback" % "logback-classic" % "1.1.2")
  val resolvers = Seq(
    "Akka Snapshot Repository" at "http://repo.akka.io/snapshots/")
}

object Dependencies {
  lazy val scalatraVersion = "2.3.0"
  val backendDeps = Seq(
    "org.scalatra" %% "scalatra" % scalatraVersion,
    "org.scalatra" %% "scalatra-json" % scalatraVersion,
    "org.scalatra" %% "scalatra-commands" % scalatraVersion,
    "org.json4s"   %% "json4s-jackson" % "3.2.9",
    "ch.qos.logback"    %  "logback-classic"   % "1.1.3"            ,
    "org.eclipse.jetty" %  "jetty-webapp"      % "9.2.10.v20150310",
    "com.typesafe.akka" %% "akka-actor" % "2.3.4"
  )
}
