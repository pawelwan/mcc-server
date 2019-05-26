package api

import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.directives.FileInfo
import akka.stream.ActorMaterializer
import akka.util.Timeout
import dto.TaskSampleDto
import org.mongodb.scala.Completed
import service.MCCService

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.util.{Failure, Success}

class MCCApi(mccService: MCCService)(implicit actorSystem: ActorSystem) extends RestComponent {

  implicit val materializer: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext = actorSystem.dispatcher
  implicit val timeout: Timeout = Timeout(1.minute)

  private val postConvertPath = path("api" / "convert") & post
  private val postTaskPath = path("api" / "task") & post

  val routes: Route = postConvertRoute ~ postTaskRoute

  private def postConvertRoute: Route =
    postConvertPath {
      storeUploadedFile("file", createTmpFile) { case (fileInfo, file) =>
        withRequestTimeout(3.minutes) {
          onComplete(mccService.convertFile(fileInfo, file)) {
            case Success(out) => complete(HttpEntity.fromFile(ContentTypes.`application/octet-stream`, out))
            case Failure(e) => complete(StatusCodes.BadRequest -> e)
          }
        }
      }
    }

  private def postTaskRoute: Route =
    postTaskPath {
      entity(as[TaskSampleDto]) { dto =>
        onComplete(mccService.insertTaskSample(dto)) {
          case Success(Completed()) => complete(StatusCodes.OK)
          case Failure(e) => complete(StatusCodes.BadRequest -> e)
        }
      }
    }


  private def createTmpFile(fileInfo: FileInfo): File =
    File.createTempFile(fileInfo.fileName, ".tmp")

}
