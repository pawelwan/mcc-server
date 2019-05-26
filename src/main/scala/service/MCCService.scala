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

  def convertFile(fileInfo: FileInfo, input: File): Future[File] = Future {

    val output: File = File.createTempFile("output", ".tmp")

    val format: Format = JCodecUtil.detectFormat(input)
    val source: Source = new SourceImpl(input.getAbsolutePath, format,
      Tuple._3(0, 0, Codec.H264),
      Tuple._3(0, 0, Codec.AAC)
    )
    val sink: Sink = new SinkImpl(output.getAbsolutePath, Format.MOV, Codec.PRORES, Codec.AAC)
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

    if(dto.remote) TaskSampleRepository.insertRemote(dto.toTaskSample)
    else TaskSampleRepository.insertLocal(dto.toTaskSample)
  }

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
