package ml

import db.TaskSample

object PredictionModelRemote extends PredictionModel {
  def train(taskSamples: Seq[TaskSample]): Unit = {
    print(s"Training a local model with ${taskSamples.size} samples")
    WekaConfigRemote.trainingData.delete()
    taskSamples foreach WekaConfigRemote.addTrainTaskSample
    WekaConfigRemote.regressor.buildClassifier(WekaConfigLocal.trainingData)
  }

  def predict(taskSample: TaskSample): Double = {
    WekaConfigRemote.addTestTaskSample(taskSample)
    WekaConfigRemote.regressor.classifyInstance(WekaConfigRemote.testData.lastInstance())
  }
}
