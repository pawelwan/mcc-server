package ml

import java.util

import db.TaskSample
import weka.classifiers.trees.J48
import weka.core.{Attribute, DenseInstance, Instance, Instances}

object WekaConfigRemote extends WekaConfig {
  val attrs = new util.ArrayList[Attribute](util.Arrays.asList(
    attrTaskType, attrTaskSize, attrConnectionType,
    attrClass))

  val trainingData = new Instances("train", attrs, 0)
  val testData = new Instances("test", attrs, 0)
  val regressor = new J48()

  trainingData.setClass(attrClass)
  testData.setClass(attrClass)

  def buildInstance(taskSample: TaskSample): Instance = {
    val instance = new DenseInstance(numAttributes)
    instance.setValue(attrTaskType, taskSample.taskType)
    instance.setValue(attrTaskSize, taskSample.taskSize)
    instance.setValue(attrConnectionType, taskSample.connectionType)
    instance
  }
}