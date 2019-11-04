package com.hari.learning.netty.handler.server

import io.netty.channel.{ ChannelHandlerContext, SimpleChannelInboundHandler }
import io.netty.buffer.ByteBuf

class EchoHandler extends SimpleChannelInboundHandler[ByteBuf] {

  override def channelRead0(ctx: ChannelHandlerContext, msg: ByteBuf): Unit = {
    ctx.writeAndFlush(msg)
  }

}