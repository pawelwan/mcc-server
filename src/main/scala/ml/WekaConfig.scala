package ml

import java.util

import db.TaskSample
import weka.core.{Attribute, Instance, Instances}

abstract class WekaConfig {
  val boolValues = new util.ArrayList[String]()
  boolValues.add("true")
  boolValues.add("false")

  val attrTaskSize = new Attribute("attrTaskSize")
  val attrTaskType = new Attribute("attrTaskType")

  val attrIsCharging = new Attribute("attrIsCharging", boolValues)
  val attrBatteryLevel = new Attribute("attrBatteryLevel")
  val attrConnectionType = new Attribute("attrConnectionType")

  val attrYear = new Attribute("attrYear")
  val attrWeekDay = new Attribute("attrWeekDay")
  val attrTime = new Attribute("attrTime")

  val attrClass = new Attribute("class")

  val trainingData: Instances
  val testData: Instances

  def buildInstance(taskSample: TaskSample): Instance

  def addTrainInstance(instance: Instance, time: Double) {
    instance.setDataset(trainingData)
    instance.setClassValue(time)
    trainingData.add(instance)
  }

  def addTrainTaskSample(taskSample: TaskSample): Unit =
    addTrainInstance(buildInstance(taskSample), taskSample.time)

  def addTestInstance(instance: Instance) {
    instance.setDataset(testData)
    testData.add(instance)
  }

  def addTestTaskSample(taskSample: TaskSample): Unit =
    addTestInstance(buildInstance(taskSample))

  def numAttributes: Int = trainingData.numAttributes()
}