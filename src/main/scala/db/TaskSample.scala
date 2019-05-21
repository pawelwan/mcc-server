package db

import org.mongodb.scala.bson.ObjectId

import scala.util.Random

final case class TaskSample(
                             _id: ObjectId = new ObjectId(),
                             taskType: Int,
                             taskSize: Double,
                             isCharging: Boolean,
                             batteryLevel: Double,
                             time: Double
                           )

object TaskSample {
  def random() = TaskSample(
    new ObjectId(),
    Random.nextInt(),
    Random.nextDouble(),
    Random.nextDouble() > 0.5,
    Random.nextDouble(),
    Random.nextDouble()
  )
}
