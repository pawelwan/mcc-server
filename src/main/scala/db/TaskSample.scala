package db

import org.mongodb.scala.bson.ObjectId

case class TaskSample(
                       _id: ObjectId = new ObjectId(),
                       size: Int,
                       battery: Float,
                       time: Float
                     )
