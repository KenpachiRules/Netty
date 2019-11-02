package com.hari.learning.netty.channel.client.handler

import com.hari.learning.netty.myhandler.MyHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.buffer.{ ByteBuf, Unpooled }
import io.netty.util.CharsetUtil

class UpperCaseHandler extends MyHandler[ByteBuf] {

  override def transformInboundData(ctx: ChannelHandlerContext, msg: ByteBuf): ByteBuf = {
    if (!msg.isReadable)
      return Unpooled.EMPTY_BUFFER
    val strVal = msg.toString(CharsetUtil.UTF_8)
    Unpooled.copiedBuffer(strVal.toUpperCase, CharsetUtil.UTF_8)
  }
  
}