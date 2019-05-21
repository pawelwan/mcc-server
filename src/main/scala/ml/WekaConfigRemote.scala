package ml

import java.util

import db.TaskSample
import weka.classifiers.functions.LinearRegression
import weka.core.{Attribute, Instance, Instances}

object WekaConfigRemote {
//  val attrs = new util.ArrayList[Attribute](util.Arrays.asList(
//    attrTaskType, attrTaskSize, attrIsCharging, attrBatteryLevel, attrConnectionType, attrYear, attrWeekDay, attrTime,
//    attrClass))
//
//  val trainingData = new Instances("train", attrs, 0)
//  val testData = new Instances("test", attrs, 0)
//  val regressor = new LinearRegression()
//
//  trainingData.setClass(attrClass)
//  testData.setClass(attrClass)

//  override def buildInstance(taskSample: TaskSample): Instance =
}