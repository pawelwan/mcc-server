package db

import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.{Completed, MongoCollection}
import scala.concurrent.Future

object TaskSampleRepository {

  private val codecRegistry = fromRegistries(fromProviders(classOf[TaskSample]), DEFAULT_CODEC_REGISTRY)

  private val collectionLocal: MongoCollection[TaskSample] = DbConnection.db
    .withCodecRegistry(codecRegistry)
    .getCollection("local_task_samples")
  private val collectionRemote: MongoCollection[TaskSample] = DbConnection.db
    .withCodecRegistry(codecRegistry)
    .getCollection("remote_task_samples")


  def insertLocal(taskSample: TaskSample): Future[Completed] =
    collectionLocal.insertOne(taskSample).toFuture()

  def insertRemote(taskSample: TaskSample): Future[Completed] =
    collectionRemote.insertOne(taskSample).toFuture()

  def findLocalForDevice(deviceModel: String): Future[Seq[TaskSample]] =
    collectionLocal.find(equal("deviceModel", deviceModel)).toFuture()

  def findAllRemote(): Future[Seq[TaskSample]] =
    collectionRemote.find().toFuture()

  // debug only
  def populateRandom(n: Int): Unit = {
    for(_ <- 1 to n) {
      insertLocal(TaskSample.random(false))
      insertRemote(TaskSample.random(false))
    }
  }
}
