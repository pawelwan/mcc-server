package dto

import db.TaskSample
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}
import io.circe.{Decoder, Encoder}


final case class TaskSampleDto(
                                remote: Boolean,
                                deviceModel: String,
                                taskType: Int,
                                taskSize: Double,
                                isCharging: Boolean,
                                batteryLevel: Double,
                                connectionType: String,
                                yearDay: Int,
                                weekDay: Int,
                                currentTime: Int,
                                time: Double,
                                timeRange: String
                              ) {

  def toTaskSample: TaskSample =
    TaskSample(
      remote=remote,
      deviceModel=deviceModel,
      taskType=taskType,
      taskSize=taskSize,
      isCharging=isCharging,
      batteryLevel=batteryLevel,
      connectionType=connectionType,
      yearDay=yearDay,
      weekDay=weekDay,
      currentTime=currentTime,
      time=time,
      timeRange=timeRange)

}

object TaskSampleDto {
  implicit val decoder: Decoder[TaskSampleDto] = deriveDecoder[TaskSampleDto]
  implicit val encoder: Encoder[TaskSampleDto] = deriveEncoder[TaskSampleDto]
}
