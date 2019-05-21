package ml

import db.TaskSample
import weka.classifiers.AbstractClassifier

object PredictionModelLocal extends PredictionModel {
  def train(taskSamples: Seq[TaskSample]): Unit = {
    println(s"Training a local model with ${taskSamples.size} samples")
    val deviceModel = "B"   // todo
    WekaConfigLocal.trainingData.delete()
    taskSamples foreach WekaConfigLocal.addTrainTaskSample

    val model = WekaConfigLocal.regressors.get(deviceModel) match {
      case Some(clf: AbstractClassifier) => clf
      case None => WekaConfigLocal.addRegressor(deviceModel)
    }
    model.buildClassifier(WekaConfigLocal.trainingData)

  }

  def predict(taskSample: TaskSample): Double = {
    WekaConfigLocal.addTestTaskSample(taskSample)

    val model = WekaConfigLocal.regressors.get(taskSample.deviceModel)
    println(WekaConfigLocal.regressors.keys)
    println(taskSample.deviceModel)
    println(model)
    model match {
      case Some(clf: AbstractClassifier) => clf.classifyInstance(WekaConfigLocal.testData.lastInstance())
      case None => -1.0
    }
  }
}
