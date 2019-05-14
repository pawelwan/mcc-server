package ml

import java.util
import weka.classifiers.functions.LinearRegression
import weka.core.{Attribute, Instance, Instances}

object WekaConfig {
  val attrSize = new Attribute("attrSize")
  val attrClass = new Attribute("class")
  val attrs = new util.ArrayList[Attribute](util.Arrays.asList(attrSize, attrClass))

  val trainingData = new Instances("train", attrs, 0)
  val testData = new Instances("test", attrs, 0)
  val regressor = new LinearRegression()

  trainingData.setClass(attrClass)
  testData.setClass(attrClass)

  def addTrainInstance(instance: Instance, time: Double) {
    instance.setDataset(trainingData)
    instance.setClassValue(time)
    trainingData.add(instance)
  }

  def addTestInstance(instance: Instance) {
    instance.setDataset(testData)
    testData.add(instance)
  }

  def numAttributes: Int = trainingData.numAttributes()
}