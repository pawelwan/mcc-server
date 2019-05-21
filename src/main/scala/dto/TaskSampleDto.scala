package dto

import db.TaskSample
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}


final case class TaskSampleDto(
                                taskType: Int,
                                taskSize: Double,
                                isCharging: Boolean,
                                batteryLevel: Double,
                                time: Double
                              ) {

  def toTaskSample: TaskSample =
    TaskSample(taskType=taskType, taskSize=taskSize, isCharging=isCharging, batteryLevel=batteryLevel, time = time)

}

object TaskSampleDto {
  implicit val decoder: Decoder[TaskSampleDto] = deriveDecoder[TaskSampleDto]
  implicit val encoder: Encoder[TaskSampleDto] = deriveEncoder[TaskSampleDto]
}
