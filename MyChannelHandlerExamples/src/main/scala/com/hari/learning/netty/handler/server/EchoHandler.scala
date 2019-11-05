package com.hari.learning.netty.handler.server

import io.netty.channel.{ ChannelHandlerContext, SimpleChannelInboundHandler }
import io.netty.buffer.{ ByteBuf, Unpooled }
import io.netty.util.CharsetUtil

class EchoHandler extends SimpleChannelInboundHandler[ByteBuf] {

  override def channelRead0(ctx: ChannelHandlerContext, msg: ByteBuf): Unit = {
    ctx.writeAndFlush(Unpooled.copiedBuffer(msg.toString(CharsetUtil.UTF_8), CharsetUtil.UTF_8))
  }

}