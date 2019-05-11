import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import api.MCCApi
import com.typesafe.config.ConfigFactory
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

}
