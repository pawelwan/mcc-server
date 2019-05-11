package api

import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.FileInfo
import akka.stream.ActorMaterializer
import akka.util.Timeout
import service.MCCService

import scala.concurrent.duration._

class MCCApi(mccService: MCCService)(implicit actorSystem: ActorSystem) extends RestComponent {

  implicit val materializer = ActorMaterializer()
  implicit val timeout: Timeout = Timeout(1.minute)

  private val postConvertPath = path("api" / "convert") & post

  val routes: Route = postConvertRoute

  private def postConvertRoute: Route =
    postConvertPath {
      storeUploadedFile("file", createTmpFile) { case (fileInfo, file) =>
        withRequestTimeout(3.minutes) {
          val out = mccService.convertFile(fileInfo, file)
          complete(HttpEntity.fromFile(ContentTypes.`application/octet-stream`, out))
        }
      }
    }

  private def createTmpFile(fileInfo: FileInfo): File =
    File.createTempFile(fileInfo.fileName, ".tmp")

}
