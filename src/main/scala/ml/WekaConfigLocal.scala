package ml

import java.util

import db.TaskSample
import weka.classifiers.AbstractClassifier
import weka.classifiers.trees.J48
import weka.core.{Attribute, DenseInstance, Instance, Instances}

object WekaConfigLocal extends WekaConfig {
  val attrs = new util.ArrayList[Attribute](util.Arrays.asList(
    attrTaskType, attrTaskSize,
    attrClass))

  val trainingData = new Instances("train", attrs, 0)
  val testData = new Instances("test", attrs, 0)
  val regressors: scala.collection.mutable.Map[String, J48] =
    scala.collection.mutable.Map[String, J48]()

  trainingData.setClass(attrClass)
  testData.setClass(attrClass)

  def addRegressor(deviceModel: String): AbstractClassifier ={
    val model = new J48()
    regressors += (deviceModel -> model)
    model
  }

  def buildInstance(taskSample: TaskSample): Instance = {
    val instance = new DenseInstance(numAttributes)
    instance.setValue(attrTaskType, taskSample.taskType)
    instance.setValue(attrTaskSize, taskSample.taskSize)
    instance
  }
}