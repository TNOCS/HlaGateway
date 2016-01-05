// sbt issue on Windows: http://stackoverflow.com/questions/20289147/error-during-sbt-execution-java-nio-channels-overlappingfilelockexception-from
lazy val root = (project in file(".")).
  settings(
    name := "hlaGateway",
    version := "1.0",
    scalaVersion := "2.11.4"
  )