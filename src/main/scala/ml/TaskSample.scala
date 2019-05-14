package ml

import weka.core.{DenseInstance, Instance}

case class TaskSample(size: Int, time: Double) {
  def toInstance: Instance = {
    val instance = new DenseInstance(WekaConfig.numAttributes)
    instance.setValue(WekaConfig.attrSize, size.toDouble)
    instance
  }
}
