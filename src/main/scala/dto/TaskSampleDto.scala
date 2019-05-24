package dto

import db.TaskSample
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}


final case class TaskSampleDto(
                                deviceModel: String,
                                taskType: Int,
                                taskSize: Double,
                                charging: Boolean,
                                batteryLevel: Double,
                                connectionType: String,
                                yearDay: Int,
                                weekDay: Int,
                                currentTime: Int,
                                time: Double
                              ) {

  def toTaskSample: TaskSample =
    TaskSample(
      deviceModel=deviceModel,
      taskType=taskType,
      taskSize=taskSize,
      isCharging=charging,
      batteryLevel=batteryLevel,
      connectionType=connectionType,
      yearDay=yearDay,
      weekDay=weekDay,
      currentTime=currentTime,
      time=time)

}

object TaskSampleDto {
  implicit val decoder: Decoder[TaskSampleDto] = deriveDecoder[TaskSampleDto]
  implicit val encoder: Encoder[TaskSampleDto] = deriveEncoder[TaskSampleDto]
}
