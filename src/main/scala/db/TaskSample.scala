package db

import org.mongodb.scala.bson.ObjectId

import scala.util.Random

final case class TaskSample(
                             _id: ObjectId = new ObjectId(),
                             taskType: Int,
                             taskSize: Double,
                             isCharging: Boolean,
                             batteryLevel: Double,
                             connectionType: Int,       // todo enum
                             yearDay: Int,
                             weekDay: Int,
                             currentTime: Int,
                             time: Double
                           )

object TaskSample {
  def randomLocal() = TaskSample(
    new ObjectId(),
    Random.nextInt(),
    Random.nextDouble(),
    Random.nextDouble() > 0.5,
    Random.nextDouble(),
    0,
    0,
    0,
    0,
    Random.nextDouble()
  )

  def randomRemote() = TaskSample(
    new ObjectId(),
    Random.nextInt(),
    Random.nextDouble(),
    Random.nextDouble() > 0.5,
    Random.nextDouble(),
    Random.nextInt() % 5,
    Random.nextInt() % 365 + 1,
    Random.nextInt() % 7 + 1,
    Random.nextInt() % 24*60 + 1,
    Random.nextDouble()
  )
}
