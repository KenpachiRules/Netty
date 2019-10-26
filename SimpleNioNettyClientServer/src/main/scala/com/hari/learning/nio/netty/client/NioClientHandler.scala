package com.hari.learning.nio.netty.client

import io.netty.channel.{ ChannelHandlerContext, SimpleChannelInboundHandler }
import com.hari.learning.utils.scala.MyLogger.{ logSevere, logInfo }
import io.netty.buffer.{ ByteBuf, Unpooled }
import com.hari.learning.nio.netty.client.NioClientHandler.{ info, error }
import io.netty.util.CharsetUtil

class NioClientHandler extends SimpleChannelInboundHandler[ByteBuf] {

  override def channelRead0(ctx: ChannelHandlerContext, msg: ByteBuf): Unit = {
    info(s"Message received from server")
    Thread.sleep(1000)
    // send message back to server.
    ctx.writeAndFlush(Unpooled.copiedBuffer("Hi", CharsetUtil.UTF_8))
  }

  override def channelActive(ctx: ChannelHandlerContext): Unit = {
    // start sending data once the channel is established.
    ctx.writeAndFlush(Unpooled.copiedBuffer("Handshake", CharsetUtil.UTF_8))
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    error(s"Channel interuppted by exception ${cause}")
    ctx.close
  }

}

object NioClientHandler {
  val info = logInfo(NioClientHandler.getClass.getCanonicalName)
  val error = logSevere(NioClientHandler.getClass.getCanonicalName)
  def apply(): NioClientHandler = new NioClientHandler
}