package com.hari.learning.netty.handler.client

import io.netty.buffer.{ ByteBuf, Unpooled }
import com.hari.learning.netty.handler.MyHandler
import io.netty.util.CharsetUtil

class ToUpperHandler extends MyHandler[ByteBuf] {

  override def transformInboundData(msg: ByteBuf): ByteBuf = {
    Unpooled.copiedBuffer(msg.toString(CharsetUtil.UTF_8).toUpperCase, CharsetUtil.UTF_8)
  }

}

object ToUpperHandler {
  def apply(): ToUpperHandler = new ToUpperHandler()
}