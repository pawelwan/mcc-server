package db

import org.mongodb.scala.{MongoClient, MongoDatabase}

object DbConnection {

  private val client = MongoClient()

  val db: MongoDatabase = client.getDatabase("task_db")

}
