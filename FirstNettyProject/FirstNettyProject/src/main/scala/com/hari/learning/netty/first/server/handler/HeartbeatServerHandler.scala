package com.hari.learning.netty.first.server.handler

import io.netty.channel.{ ChannelInboundHandlerAdapter, ChannelHandlerContext, ChannelFutureListener }
import io.netty.buffer.Unpooled
import com.hari.learning.netty.model.Heartbeat
import com.hari.learning.netty.model.Heartbeat.beat
import com.hari.learning.netty.first.server.handler.HeartbeatServerHandler._

/**
 * Handler to listen to heart beats sent from client to server.
 *
 * @author harim
 *
 */

class HeartbeatServerHandler extends ChannelInboundHandlerAdapter {

  override def channelRead(ctx: ChannelHandlerContext, msg: Object): Unit = {
    if (!msg.isInstanceOf[Heartbeat]) {
      // log it and do not respond.
      severe(s"This is not a Hearbeat $msg")
      return
    }
    // check if the beat is meant to continue further or just terminate
    val receivedBeat = msg.asInstanceOf[Heartbeat]
    if (!receivedBeat.continue) {
      info("Terminated the channel on the event of receiving abort Heartbeat")
      ctx.close
    }
    val beat = Heartbeat.beat(msg.asInstanceOf[Heartbeat].clientId)
    info(s"Received a heart beat from the client ${beat.clientId}")
    Thread.sleep(1000)
    ctx.write(beat)
  }

  override def channelReadComplete(ctx: ChannelHandlerContext): Unit = {
    ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE)
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    severe(s"Heartbeat channel failed with $cause")
    ctx.close
  }
}

object HeartbeatServerHandler {

  import com.hari.learning.utils.scala.MyLogger._

  val severe = logSevere(this.getClass.getCanonicalName)
  val info = logInfo(this.getClass.getCanonicalName)

  def apply(): HeartbeatServerHandler = new HeartbeatServerHandler()
}