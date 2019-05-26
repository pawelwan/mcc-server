package ml

import db.TaskSample

object PredictionModelRemote extends PredictionModel {
  def train(taskSamples: Seq[TaskSample]): Unit = {
    println(s"Training a remote model with ${taskSamples.size} samples")
    WekaConfigRemote.trainingData.delete()
    taskSamples foreach WekaConfigRemote.addTrainTaskSample
    WekaConfigRemote.regressor.buildClassifier(WekaConfigLocal.trainingData)
    saveClassifier(WekaConfigRemote.regressor, modelPath)
  }

  def predict(taskSample: TaskSample): Double = {
    WekaConfigRemote.addTestTaskSample(taskSample)
    WekaConfigRemote.regressor.classifyInstance(WekaConfigRemote.testData.lastInstance())
  }

  val modelPath: String =
    "remote.weka"

}
