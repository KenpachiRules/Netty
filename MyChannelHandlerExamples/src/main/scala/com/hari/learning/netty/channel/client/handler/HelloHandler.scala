package com.hari.learning.netty.channel.client.handler

import io.netty.buffer.{ ByteBuf, Unpooled }
import com.hari.learning.netty.myhandler.MyHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.util.CharsetUtil

class HelloHandler extends MyHandler[ByteBuf] {

  override def transformInboundData(ctx: ChannelHandlerContext, msg: ByteBuf): ByteBuf = {
    if (!msg.isReadable)
      return Unpooled.EMPTY_BUFFER
    val helloBytes: ByteBuf = Unpooled.copiedBuffer("hello ", CharsetUtil.UTF_8)
    helloBytes.writeBytes(msg)
    helloBytes
  }
}