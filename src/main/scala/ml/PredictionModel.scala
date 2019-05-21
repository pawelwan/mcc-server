package ml

import db.TaskSample

abstract class PredictionModel {
  def train(taskSamples: Seq[TaskSample])

  def predict(taskSample: TaskSample): Double
}
