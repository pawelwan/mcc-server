package ml

import db.TaskSample

object PredictionModelLocal extends PredictionModel {
  def train(taskSamples: Seq[TaskSample]): Unit = {
    print(s"Training a local model with ${taskSamples.size} samples")
    WekaConfigLocal.trainingData.delete()
    taskSamples foreach WekaConfigLocal.addTrainTaskSample
    WekaConfigLocal.regressor.buildClassifier(WekaConfigLocal.trainingData)
  }

  def predict(taskSample: TaskSample): Double = {
    WekaConfigLocal.addTestTaskSample(taskSample)
    WekaConfigLocal.regressor.classifyInstance(WekaConfigLocal.testData.lastInstance())
  }
}
