import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import api.MCCApi
import com.typesafe.config.ConfigFactory
import db.{TaskSample, TaskSampleRepository}
import ml.{PredictionModelLocal, PredictionModelRemote}
import service.MCCService

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object Main extends App {

  val config = ConfigFactory.load()
  val systemName = config.getString("actor-system-name")
  val host = config.getString("server.host")
  val port = config.getInt("server.port")

  implicit val system: ActorSystem = ActorSystem(systemName, config)
  implicit val ec: ExecutionContext = system.dispatcher
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val mccApi = new MCCApi(new MCCService())

  Http().bindAndHandle(Route.seal(mccApi.routes), host, port).onComplete {
    case Success(_) => println(s"Listening on $host:$port!")
    case Failure(ex) => ex
  }

  // ML
  val testSample = TaskSample.random(false)
  val deviceModel = testSample.deviceModel

  for {
    _ <- TaskSampleRepository.populateRandom(10)
    local <- TaskSampleRepository.findLocalForDevice(deviceModel)
    remote <- TaskSampleRepository.findAllRemote()
  } yield {
      PredictionModelLocal.train(local)
      println(s"Sample to predict local: $testSample")
      println(s"Local prediction: ${PredictionModelLocal.predict(testSample)}")
      PredictionModelRemote.train(remote)
      println(s"Sample to predict remote: $testSample")
      println(s"Remote prediction: ${PredictionModelRemote.predict(testSample)}")
  }
}
