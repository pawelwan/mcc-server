import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import api.MCCApi
import com.typesafe.config.ConfigFactory
import dto.TaskSampleDto
import ml.WekaConfig
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
  val tasks = List(
    TaskSampleDto(42, 0.43.toFloat, 40.5.toFloat).toTaskSample,
    TaskSampleDto(52, 0.27.toFloat, 53.0.toFloat).toTaskSample,
    TaskSampleDto(62, 0.99.toFloat, 67.5.toFloat).toTaskSample
  )
  for(t <- tasks) {
    WekaConfig.addTrainInstance(t.toInstance, t.time)
  }
  WekaConfig.regressor.buildClassifier(WekaConfig.trainingData)

  val testInstance = TaskSampleDto(48, 0.55.toFloat, 0.0.toFloat).toTaskSample.toInstance
  WekaConfig.addTestInstance(testInstance)
  val prediction: Double = WekaConfig.regressor.classifyInstance(WekaConfig.testData.firstInstance())
  print(s"Prediction: $prediction")
}
