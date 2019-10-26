package com.hari.learning.nio.netty.client.test

import com.hari.learning.utils.scala.MyLogger.{ logSevere, logInfo }
import io.netty.channel.{ ChannelHandlerContext, SimpleChannelInboundHandler }
import io.netty.buffer.{ ByteBuf, Unpooled }
import io.netty.util.CharsetUtil
import com.hari.learning.nio.netty.client.test.TestNettyClientHandler.{ info, error }

class TestNettyClientHandler(iter: Iterator[ByteBuf]) extends SimpleChannelInboundHandler[ByteBuf] {

  override def channelRead0(ctx: ChannelHandlerContext, msg: ByteBuf): Unit = {
    info(s"Message received from server")
    Thread.sleep(1000)
    if (iter.hasNext)
      ctx.writeAndFlush(iter.next)
    else
      ctx.close().sync()
  }

  override def channelActive(ctx: ChannelHandlerContext): Unit = {
    // start sending data once the channel is established.
    ctx.writeAndFlush(Unpooled.copiedBuffer("First msg from client", CharsetUtil.UTF_8))
  }

  override def exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable): Unit = {
    error(s"Channel interuppted by exception ${cause}")
    ctx.close
  }

}

object TestNettyClientHandler {
  val info = logInfo(this.getClass.getCanonicalName)
  val error = logSevere(this.getClass.getCanonicalName)
  def apply(iter: Iterator[ByteBuf]): TestNettyClientHandler = new TestNettyClientHandler(iter)
}