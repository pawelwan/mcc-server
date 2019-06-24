package service

import java.io.File

import akka.actor.ActorSystem
import akka.http.scaladsl.server.directives.FileInfo
import db.TaskSampleRepository
import dto.TaskSampleDto
import ml.{PredictionModelLocal, PredictionModelRemote}
import org.jcodec.api.transcode._
import org.jcodec.common.{Codec, Format, JCodecUtil, Tuple}
import org.mongodb.scala.Completed

import scala.concurrent.{ExecutionContext, Future}

class MCCService(implicit val ec: ExecutionContext, implicit val system: ActorSystem) {

  private val trainPeriod = 10

  def convertFile(fileInfo: FileInfo, input: File): Future[File] = Future {

    val output: File = File.createTempFile("output", ".tmp")

    val format: Format = JCodecUtil.detectFormat(input)
    val source: Source = new SourceImpl(input.getAbsolutePath, format,
      Tuple._3(0, 0, Codec.H264),
      Tuple._3(0, 0, Codec.AAC)
    )
    val sink: Sink = new SinkImpl(output.getAbsolutePath, Format.MOV, Codec.H264, Codec.AAC)
    sink.setOption(Options.PROFILE, "PROXY")

    val builder = Transcoder.newTranscoder()
    builder.addSource(source)
    builder.addSink(sink)
    builder.setAudioMapping(0, 0, true)
    builder.setVideoMapping(0, 0, false)

    val transcoder: Transcoder = builder.create()
    transcoder.transcode()

    println(fileInfo)
    output
  }

  def insertTaskSample(dto: TaskSampleDto): Future[Completed] = {
    println(s"Inserting $dto")

    if (dto.remote) TaskSampleRepository.insertRemote(dto.toTaskSample)
    else TaskSampleRepository.insertLocal(dto.toTaskSample)
  }

  def trainModel(dto: TaskSampleDto): Future[Completed] =
    if (dto.remote) trainRemoteModel()
    else trainLocalModel(dto.deviceModel)

  private def trainRemoteModel(): Future[Completed] =
    for {
      taskSamples <- TaskSampleRepository.findAllRemote()
      _ = if (taskSamples.size > 20 && taskSamples.size % trainPeriod == 1) PredictionModelRemote.train(taskSamples)
    } yield Completed()

  def trainRemoteModel2(): Future[Completed] =
    for {
      taskSamples <- TaskSampleRepository.findAllRemote()
      _ = PredictionModelRemote.train(taskSamples)
    } yield Completed()

  def evalRemote(): Future[Completed] =
    for {
      taskSamples <- TaskSampleRepository.findAllRemote()
      _ = taskSamples.map(PredictionModelRemote.predict)
    } yield Completed()

  def evalLocal(): Future[Completed] =
    for {
      taskSamples <- TaskSampleRepository.findLocalForDevice("SM-J330F")
      _ = taskSamples.map(PredictionModelLocal.predict)
    } yield Completed()

  private def trainLocalModel(deviceModel: String): Future[Completed] =
    for {
      taskSamples <- TaskSampleRepository.findLocalForDevice(deviceModel)
      _ = if (taskSamples.size > 20 && taskSamples.size % trainPeriod == 1) PredictionModelLocal.train(taskSamples)
    } yield Completed()

  def trainLocalModel2(): Future[Completed] =
    for {
      taskSamples <- TaskSampleRepository.findLocalForDevice("SM-J330F")
      _ = PredictionModelLocal.train(taskSamples)
    } yield Completed()

  def downloadModel(modelType: String, maybeDeviceModel: Option[String]): Option[File] = {
    val fileName = modelType match {
      case "time" =>
        maybeDeviceModel.fold(PredictionModelRemote.modelPath)(PredictionModelLocal.modelPath)
      case _ =>
        ""
    }
    val file = new File(fileName)

    println(file)
    if (file.exists()) Some(file) else None
  }
}
