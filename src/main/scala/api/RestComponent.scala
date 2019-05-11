package api

import java.util.UUID

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.unmarshalling.Unmarshaller
import de.heikoseeberger.akkahttpcirce.FailFastCirceSupport
import io.circe.Printer

trait RestComponent extends FailFastCirceSupport {

  implicit val printer: Printer = Printer.noSpaces.copy(dropNullValues = true)

  implicit val stringToUUID: Unmarshaller[String, UUID] = Unmarshaller.strict[String, UUID](UUID.fromString)

  def routes: Route

}
