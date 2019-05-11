name := "mcc-server"
version := "0.1"

scalaVersion := "2.12.8"

libraryDependencies ++= {
  val akkaVsn = "2.5.22"
  val akkaHttpVsn = "10.1.8"
  val slickVsn = "3.3.0"
  val circeVsn = "0.11.1"

  Seq(
    "com.typesafe"                  % "config"                            % "1.3.4",
    "com.typesafe.akka"            %% "akka-actor"                        % akkaVsn,
    "com.typesafe.akka"            %% "akka-stream"                       % akkaVsn,
    "com.typesafe.akka"            %% "akka-http"                         % akkaHttpVsn,
    "com.typesafe.akka"            %% "akka-http-core"                    % akkaHttpVsn,
    "de.heikoseeberger"            %% "akka-http-circe"                   % "1.25.2",
    "io.circe"                     %% "circe-core"                        % circeVsn,
    "io.circe"                     %% "circe-generic"                     % circeVsn,
    "io.circe"                     %% "circe-parser"                      % circeVsn,
    "org.typelevel"                %% "cats-core"                         % "1.6.0",
    "org.jcodec"                    % "jcodec"                            % "0.2.3"
  )
}

javaOptions ++= Seq("-Xms512M", "-Xmx2048M", "-XX:MaxMetaspaceSize=512M")

run / fork := true
run / outputStrategy := Some(StdoutOutput)

assembly / test := {}
assembly / assemblyJarName := "app-assembly.jar"
assembly / assemblyMergeStrategy := {
  case "application.conf" => MergeStrategy.first
  case "logback.xml" => MergeStrategy.first
  case x: Any => (assembly / assemblyMergeStrategy).value(x)
}
