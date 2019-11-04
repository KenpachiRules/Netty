package com.hari.learning.netty.handler.client

import com.hari.learning.netty.handler.MyHandler
import io.netty.buffer.{ ByteBuf, Unpooled }
import io.netty.util.CharsetUtil

/**
 * Prepends a 'Hello' to the ByteBuf
 * @author harim
 *
 */

class HelloHandler extends MyHandler[ByteBuf] {

  override def transformInboundData(msg: ByteBuf): ByteBuf = {
    if (!msg.isReadable())
      throw new IllegalStateException(" Inbound message is empty/non-consumable")
    val hw: ByteBuf = Unpooled.copiedBuffer("Hello ", CharsetUtil.UTF_8)
    hw.writeBytes(msg)
  }
}

object HelloHandler {
  def apply(): HelloHandler = new HelloHandler()
}