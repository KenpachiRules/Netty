package com.hari.learning.netty.first.tests

import io.netty.channel.{ SimpleChannelInboundHandler, ChannelHandlerContext }
import com.hari.learning.netty.model.Heartbeat
import com.hari.learning.utils.scala.MyLogger.{ logInfo, logSevere }
import java.util.UUID

class TestHeartbeatClientHandler extends SimpleChannelInboundHandler[Heartbeat] {

  val info = logInfo(new StringBuilder(this.getClass.getCanonicalName).append("_").append("info").toString)
  val severe = logSevere(new StringBuilder(this.getClass.getCanonicalName).append("_").append("severe").toString)

  val beats: Iterator[Heartbeat] = Range(0, 10).map(i => Heartbeat.beat(UUID.randomUUID().toString)).toIterator

  override def channelRead0(ctx: ChannelHandlerContext, msg: Heartbeat): Unit = {
    info(s"Received msg is ${msg}")
    beats.foreach(ctx.writeAndFlush)
    ctx.writeAndFlush(Heartbeat.abortBeat(UUID.randomUUID().toString))
  }

  override def channelActive(ctx: ChannelHandlerContext): Unit = {
    if (beats.hasNext)
      ctx.writeAndFlush(beats.next)
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    // An exception has been thrown , lets close the channel.
    severe(s"Failed due to ${cause}")
    ctx.close
  }
}