package service

import java.io.File
import akka.actor.ActorSystem
import akka.http.scaladsl.server.directives.FileInfo
import db.TaskSampleRepository
import dto.TaskSampleDto
import org.jcodec.api.transcode._
import org.jcodec.common.{Codec, Format, JCodecUtil, Tuple}
import org.mongodb.scala.Completed
import scala.concurrent.{ExecutionContext, Future}

class MCCService(implicit val ec: ExecutionContext, implicit val system: ActorSystem) {

  def convertAndInsert(fileInfo: FileInfo, input: File): Future[File] =     // todo
    for {
      file <- convertFile(input)
      _ = println(fileInfo)
    } yield file

  def convertFile(input: File): Future[File] = Future {

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

    output
  }

  def insertTaskSample(dto: TaskSampleDto): Future[Completed] =
    dto.deviceModel match {
      case m if m.isEmpty => TaskSampleRepository.insertRemote(dto.toTaskSample)
      case _ => TaskSampleRepository.insertLocal(dto.toTaskSample)
    }
}
