package ml

import db.TaskSample
import weka.classifiers.AbstractClassifier

object PredictionModelLocal extends PredictionModel {
  def train(taskSamples: Seq[TaskSample]): Unit = {
    println(s"Training a local model with ${taskSamples.size} samples")
    val deviceModel = taskSamples.head.deviceModel
    assert(taskSamples.forall(_.deviceModel == deviceModel))
    WekaConfigLocal.trainingData.delete()
    taskSamples foreach WekaConfigLocal.addTrainTaskSample

    val model = WekaConfigLocal.regressors.get(deviceModel) match {
      case Some(clf: AbstractClassifier) => clf
      case None => WekaConfigLocal.addRegressor(deviceModel)
    }
    model.buildClassifier(WekaConfigLocal.trainingData)
    saveClassifier(model, modelPath(deviceModel))
  }

  def predict(taskSample: TaskSample): Double = {
    WekaConfigLocal.addTestTaskSample(taskSample)

    val model = WekaConfigLocal.regressors.get(taskSample.deviceModel)
    println(model)
    model match {
      case Some(clf: AbstractClassifier) => clf.classifyInstance(WekaConfigLocal.testData.lastInstance())
      case None => -1.0
    }
  }

  def modelPath(deviceModel: String): String =
    s"local_$deviceModel.weka"
}
