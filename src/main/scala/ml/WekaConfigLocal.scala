package ml

import java.util

import db.TaskSample
import weka.classifiers.functions.LinearRegression
import weka.core.{Attribute, DenseInstance, Instance, Instances}

object WekaConfigLocal extends WekaConfig {
  val attrs = new util.ArrayList[Attribute](util.Arrays.asList(
    attrTaskType, attrTaskSize, attrIsCharging, attrBatteryLevel,
    attrClass))

  val trainingData = new Instances("train", attrs, 0)
  val testData = new Instances("test", attrs, 0)
  val regressor = new LinearRegression()

  trainingData.setClass(attrClass)
  testData.setClass(attrClass)

  def buildInstance(taskSample: TaskSample): Instance = {
    val instance = new DenseInstance(numAttributes)
    instance.setValue(attrTaskType, taskSample.taskType)
    instance.setValue(attrTaskSize, taskSample.taskSize)
    instance.setValue(attrIsCharging, taskSample.isCharging.toString)
    instance.setValue(attrBatteryLevel, taskSample.batteryLevel)
    instance
  }
}