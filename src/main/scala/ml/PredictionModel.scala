package ml

import db.TaskSample
import weka.classifiers.AbstractClassifier

abstract class PredictionModel {
  def train(taskSamples: Seq[TaskSample])

  def predict(taskSample: TaskSample): Double

  def saveClassifier(cls: AbstractClassifier, path: String): Unit = {
    weka.core.SerializationHelper.write(path, cls)
  }
}
