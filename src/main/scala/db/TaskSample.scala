package db

import org.mongodb.scala.bson.ObjectId

import scala.util.Random

final case class TaskSample(
                             _id: ObjectId = new ObjectId(),
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
                             time: Double
                           )

object TaskSample {
  val connectionTypes = Seq("?", "None", "Wi-Fi", "EDGE", "HSPA", "HSPAP", "LTE")

  def random(remote: Boolean) = TaskSample(
    new ObjectId(),
    remote,
    Seq("A", "B", "C")(Random.nextInt(3)),
    Random.nextInt(),
    Random.nextDouble(),
    Random.nextDouble() > 0.5,
    Random.nextDouble(),
    connectionTypes(Random.nextInt(connectionTypes.length)),
    Random.nextInt(365),
    Random.nextInt(7),
    Random.nextInt(24*60),
    Random.nextDouble()
  )
}
