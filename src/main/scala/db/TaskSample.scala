package db

import org.mongodb.scala.bson.ObjectId

final case class TaskSample(
                             _id: ObjectId = new ObjectId(),
                             size: Int,
                             battery: Float,
                             time: Float
                           )
