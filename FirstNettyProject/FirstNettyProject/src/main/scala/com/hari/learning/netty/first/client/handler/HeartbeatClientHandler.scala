package com.hari.learning.netty.first.client.handler

import io.netty.channel.SimpleChannelInboundHandler
import com.hari.learning.utils.scala.MyLogger._
import com.hari.learning.netty.model.Heartbeat
import io.netty.channel.ChannelHandlerContext
import com.hari.learning.netty.first.client.handler.HeartbeatClientHandler._

class HeartbeatClientHandler(clientId: String) extends SimpleChannelInboundHandler[Heartbeat] {

  override def channelActive(ctx: ChannelHandlerContext): Unit = {
    // send a message to acknowledge.
    ctx.channel().writeAndFlush(Heartbeat.beat(clientId))
    info("Heartbeat sent successully")
  }

  override def channelRead0(ctx: ChannelHandlerContext, msg: Heartbeat): Unit = {
    // currently a one on one relationship between client handler and the
    // server handler
    if (!msg.continue) {
      severe("A terminate signal from Server")
      ctx.close() // close the channel
    }
    info(s"Received hearbeat back from server ${msg}")
    Thread.sleep(2000)
    ctx.write(Heartbeat.beat(clientId))
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    severe(s"Failed with exception ${cause.getLocalizedMessage}")
    ctx.close()
  }

}

object HeartbeatClientHandler {

  def info = logInfo(this.getClass.getCanonicalName)
  def severe = logSevere(this.getClass.getCanonicalName)

  def apply(clientId: String): HeartbeatClientHandler = new HeartbeatClientHandler(clientId)

}