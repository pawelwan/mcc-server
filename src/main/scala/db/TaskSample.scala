package db

import ml.WekaConfig
import org.mongodb.scala.bson.ObjectId
import weka.core.{DenseInstance, Instance}

final case class TaskSample(
                             _id: ObjectId = new ObjectId(),
                             size: Int,
                             battery: Float,
                             time: Float
                           ) {
  def toInstance: Instance = {
    val instance = new DenseInstance(WekaConfig.numAttributes)
    instance.setValue(WekaConfig.attrSize, size.toDouble)
    instance
  }
}
