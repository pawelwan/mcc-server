package dto

import db.TaskSample
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}


final case class TaskSampleDto(
                                size: Int,
                                battery: Float,
                                time: Float
                              ) {

  def toTaskSample: TaskSample =
    TaskSample(size = size, battery = battery, time = time)

}

object TaskSampleDto {
  implicit val decoder: Decoder[TaskSampleDto] = deriveDecoder[TaskSampleDto]
  implicit val encoder: Encoder[TaskSampleDto] = deriveEncoder[TaskSampleDto]
}
