package com.hari.learning.netty.channel.server.handler

import io.netty.channel.{ ChannelHandlerContext, SimpleChannelInboundHandler }
import io.netty.buffer.ByteBuf
import com.hari.learning.utils.scala.MyLogger.{ logSevere }

class EchoServerHandler extends SimpleChannelInboundHandler[ByteBuf] {

  val severe = logSevere("EchoServerHandler")
  override def channelRead0(ctx: ChannelHandlerContext, msg: ByteBuf): Unit = {
    ctx.writeAndFlush(msg)
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, t: Throwable): Unit = {
    severe(s" Channel has thrown an exception ${t.getLocalizedMessage} ")
    ctx.close.sync
  }

}