package db

import org.bson.codecs.configuration.CodecRegistries.{fromProviders, fromRegistries}
import org.mongodb.scala.bson.codecs.DEFAULT_CODEC_REGISTRY
import org.mongodb.scala.bson.codecs.Macros._
import org.mongodb.scala.{Completed, MongoCollection}

import scala.concurrent.Future

object TaskSampleRepository {

  private val codecRegistry = fromRegistries(fromProviders(classOf[TaskSample]), DEFAULT_CODEC_REGISTRY)

  private val collection: MongoCollection[TaskSample] = DbConnection.db
    .withCodecRegistry(codecRegistry)
    .getCollection("task_samples")


  def insert(taskSample: TaskSample): Future[Completed] =
    collection.insertOne(taskSample).toFuture()

  def findAll(): Future[Seq[TaskSample]] =
    collection.find().toFuture()

}
